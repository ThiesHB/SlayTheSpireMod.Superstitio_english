package superstitioapi.cardModifier

import basemod.ClickableUIElement
import basemod.ReflectionHacks
import basemod.helpers.TooltipInfo
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager
import com.evacipated.cardcrawl.mod.stslib.patches.RenderStackedBlockInstances.RenderStackedIcons
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.FontHelper
import com.megacrit.cardcrawl.helpers.PowerTip
import com.megacrit.cardcrawl.helpers.TipHelper
import com.megacrit.cardcrawl.powers.AbstractPower
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import java.util.*
import kotlin.math.min

/**
 * 此类用于修改游戏中的渲染逻辑，以更准确地显示堆叠的格挡效果。
 * 它修改了如何显示生物的格挡数量，确保所有格挡修饰符都被考虑在内。
 */
object RenderStackedBlockInstancesPatch
{
    // 常量定义，用于确定格挡图标的位置和大小
    internal val BLOCK_ICON_X: Float = ReflectionHacks.getPrivateStatic(AbstractCreature::class.java, "BLOCK_ICON_X")
    internal val BLOCK_ICON_Y: Float = ReflectionHacks.getPrivateStatic(AbstractCreature::class.java, "BLOCK_ICON_Y")
    private val blankTex = Texture("images/blank.png")
    private val dx = 50.0f * Settings.scale
    private val dy = 54.0f * Settings.scale

    /**
     * 生成一个列表，其中包含给定生物的所有格挡修饰符对应的绘制元素。
     * 这些元素稍后将用于渲染格挡图标。
     *
     * @param creature 给定的生物
     * @return 包含BlockStackDividedElement的列表，每个元素代表一个格挡实例
     */
    private fun makeAllBlockModifierIntoDrawElement(creature: AbstractCreature): List<BlockStackDividedElement>
    {
        // 获取所有格挡实例并创建对应的绘制元素
        val instances: List<BlockInstance?> = getAllBlockInstances(creature)
        val list: MutableList<BlockStackDividedElement> = ArrayList()
        for (blockInstance in instances)
        {
            val blockElement = BlockStackDividedElement(creature, blockInstance)
            list.add(blockElement)
        }
        list.reverse() // 反转列表以确保正确的渲染顺序
        return list
    }

    /**
     * 获取给定生物的所有格挡实例，包括那些由可以渲染为格挡效果的力量产生的实例。
     *
     * @param creature 目标生物
     * @return 包含所有格挡效果的BlockInstance列表
     */
    private fun getAllBlockInstances(creature: AbstractCreature): MutableList<BlockInstance>
    {
        // 合并生物的格挡实例和可以渲染为格挡效果的Power
        val blockInstances = BlockModifierManager.blockInstances(creature)
        val newBlockInstances = ArrayList<BlockInstance>()
        newBlockInstances.addAll(blockInstances)
        creature.powers
            .filter { power: AbstractPower? -> power is RenderAsBlockPower }
            .map { power: AbstractPower -> (power as RenderAsBlockPower).getBlockInstance() }
            .let(newBlockInstances::addAll)
        return newBlockInstances
    }

    /**
     * 渲染给定生物的格挡图标，考虑到所有的格挡实例和修饰符。
     *
     * @param creature 目标生物
     * @param sb       用于渲染的SpriteBatch对象
     * @param x        开始渲染的x坐标
     * @param y        开始渲染的y坐标
     */
    private fun DrawBlocks(creature: AbstractCreature, sb: SpriteBatch, x: Float, y: Float)
    {
        // 获取渲染格挡所需的设置和颜色
        val blockOffset = ReflectionHacks.getPrivate<Float>(creature, AbstractCreature::class.java, "blockOffset")
        val blockScale = ReflectionHacks.getPrivate<Float>(creature, AbstractCreature::class.java, "blockScale")
        val blockOutlineColor =
            ReflectionHacks.getPrivate<Color>(creature, AbstractCreature::class.java, "blockOutlineColor")
        val blockTextColor = ReflectionHacks.getPrivate<Color>(creature, AbstractCreature::class.java, "blockTextColor")

        // 检查是否需要重新生成绘制元素
        val allBlocks: List<BlockInstance?> = getAllBlockInstances(creature)
        var elements: List<BlockStackDividedElement>? = BlockStackElementField.element[creature]
        if (allBlocks.stream().allMatch { obj: BlockInstance? -> obj!!.defaultBlock() }) return
        if (elements == null || elements.isEmpty() || allBlocks.size != elements.size || !elements.stream()
                .map { element: BlockStackDividedElement? -> element!!.blockInstance }
                .allMatch(allBlocks::contains)
        )
        {
            elements = makeAllBlockModifierIntoDrawElement(creature)
        }

        if (elements.isEmpty()) return

        // 遍历并渲染每个格挡元素
        var offsetY = 0
        for (element in elements)
        {
            element.move(x + BLOCK_ICON_X - 32.0f, y + BLOCK_ICON_Y - 32.0f + blockOffset + offsetY)
            element.update()
            element.renderBlockIcon(sb, blockScale, blockOutlineColor, blockTextColor)
            offsetY = (offsetY.toFloat() + dy).toInt()
        }

        // 更新生物的格挡元素字段
        BlockStackElementField.element.set(creature, elements)
    }

    class BlockStackDividedElement //            this.tips.addAll(Arrays.asList(tips));
        (private val owner: AbstractCreature, val blockInstance: BlockInstance?) :
        ClickableUIElement(blankTex, 0.0f, 0.0f, baseWidth, baseHeight)
    {
        private val tip: PowerTip? = null
        private var HoveredTimer = HOVERED_TIMER_INIT

        fun move(x: Float, y: Float)
        {
            this.move(x, y, 0.0f)
        }

        fun setHitboxHeight(height: Float)
        {
            hitbox.resize(baseWidth, baseHeight + height)
        }

        fun renderBlockIcon(sb: SpriteBatch, blockScale: Float, blockOutlineColor: Color?, blockTextColor: Color?)
        {
            this.render(sb)

            val IconColor = if (blockInstance!!.blockColor != null) blockInstance.blockColor else blockOutlineColor!!
            val TextColor = if (blockInstance.blockColor != null) blockInstance.blockColor else blockTextColor!!

            val tmpIcon = IconColor.a
            val tmpText = TextColor.a
            if (hitbox.hovered)
            {
                IconColor.a *= HoveredTimer
                TextColor.a *= HoveredTimer
            }

            sb.color = IconColor


            sb.draw(
                blockInstance.blockImage, x, y,
                32.0f, 32.0f, 64.0f, 64.0f, Settings.scale, Settings.scale, 0.0f, 0, 0, 64, 64, false, false
            )

            FontHelper.renderFontCentered(
                sb, FontHelper.blockInfoFont, blockAmount.toString(),
                x - (-32.0f), y - (BLOCK_ICON_Y - 32.0f) - 16.0f * Settings.scale, TextColor, blockScale
            )

            IconColor.a = tmpIcon
            TextColor.a = tmpText

            if (hitbox.hovered) FontHelper.renderFontCentered(
                sb, FontHelper.blockInfoFont, BlockStackForceShowPatch.GetBlockStack(
                    owner
                ).toString(),
                x - (-32.0f), y - (BLOCK_ICON_Y - 32.0f) - 16.0f * Settings.scale, TextColor, blockScale
            )
        }

        private fun move(x: Float, y: Float, d: Float)
        {
            this.setX(x - d * Settings.scale)
            this.setY(y - d * Settings.scale)
        }

        private val blockAmount: Int
            get() = blockInstance!!.blockAmount

        override fun onHover()
        {
            if (HoveredTimer >= HOVERED_TIMER_MIN)
            {
                HoveredTimer -= Gdx.graphics.deltaTime
            }
            val tips = ArrayList<PowerTip>()
            tips.add(TooltipInfo(blockInstance!!.makeName(), blockInstance.makeDescription()).toPowerTip())
            val isInScreen = hitbox.width / 2.0f < 1544.0f * Settings.scale
            TipHelper.queuePowerTips(
                if (isInScreen) owner.hb.cX + (owner.hb.width / 2.0f) + (20.0f * Settings.scale)
                else owner.hb.cX - owner.hb.width / 2.0f + -380.0f * Settings.scale,
                this.y + owner.hb.height + TipHelper.calculateAdditionalOffset(tips, this.y + owner.hb.height), tips
            )
        }

        override fun onUnhover()
        {
            if (HoveredTimer < HOVERED_TIMER_INIT) HoveredTimer = min(
                (HoveredTimer + Gdx.graphics.deltaTime).toDouble(),
                HOVERED_TIMER_INIT.toDouble()
            ).toFloat()
        }

        override fun onClick()
        {
        }

        companion object
        {
            const val HOVERED_TIMER_INIT: Float = 1.0f
            const val HOVERED_TIMER_MIN: Float = 0.2f
            private val baseHeight = 64.0f * Settings.scale
            private val baseWidth = 64.0f * Settings.scale
        }
    }

    @SpirePatch2(clz = AbstractCreature::class, method = "<class>")
    object BlockStackElementField
    {
        @JvmField
        var element: SpireField<List<BlockStackDividedElement>?> = SpireField<List<BlockStackDividedElement>?> { null }

        @JvmField
        var forceDrawBlock: SpireField<Boolean> = SpireField<Boolean> { false }
    }

    @SpirePatch(clz = RenderStackedIcons::class, method = "pls")
    object BlockStackPatchStslib
    {
        @SpirePrefixPatch
        @JvmStatic
        fun Prefix(
            __instance: AbstractCreature, sb: SpriteBatch, x: Float, y: Float, ___BLOCK_ICON_X: Float,
            ___BLOCK_ICON_Y: Float, ___blockOffset: Float, ___blockTextColor: Color?, ___blockOutlineColor: Color?,
            ___blockScale: Float
        ): SpireReturn<Void>
        {
            DrawBlocks(__instance, sb, x, y)
            return SpireReturn.Return()
        }
    }

    @SpirePatch2(clz = AbstractCreature::class, method = "renderBlockIconAndValue")
    object BlockStackPatchVanilla
    {
        @SpirePrefixPatch
        @JvmStatic
        fun Prefix(__instance: AbstractCreature, sb: SpriteBatch?, x: Float, y: Float): SpireReturn<Void>
        {
            if (getAllBlockInstances(__instance).stream()
                    .allMatch { obj: BlockInstance? -> obj!!.defaultBlock() }
            ) return SpireReturn.Continue()
            return SpireReturn.Return()
        }
    }

    @SpirePatch2(clz = AbstractCreature::class, method = "renderHealth")
    object BlockStackForceShowPatch
    {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor
        {
            return object : ExprEditor()
            {
                @Throws(CannotCompileException::class)
                override fun edit(m: FieldAccess)
                {
                    if (m.fieldName != "currentBlock" || !m.isReader) return
                    m.replace("{\$_ = " + BlockStackForceShowPatch::class.qualifiedName + ".GetBlockStack( $0 ) ;}")
                }
            }
        }

        @JvmStatic
        fun GetBlockStack(creature: AbstractCreature): Int
        {
            if (!BlockStackElementField.forceDrawBlock[creature]) return creature.currentBlock
            val totalBlock = getAllBlockInstances(creature).stream()
                .mapToInt { obj: BlockInstance? -> obj!!.blockAmount }.sum()
            if (totalBlock <= 0)
            {
                BlockStackElementField.forceDrawBlock[creature] = false
                return creature.currentBlock
            }
            return totalBlock
        }
    }
}
