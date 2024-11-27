package superstitioapi.renderManager.inBattleManager

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch
import com.megacrit.cardcrawl.core.OverlayMenu
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.rooms.AbstractRoom
import com.megacrit.cardcrawl.stances.AbstractStance
import com.megacrit.cardcrawl.stances.NeutralStance
import superstitioapi.Logger
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.ActionUtility.VoidSupplier
import java.util.*
import java.util.function.Consumer

/**
 * 在Register之后就可以自动绘制和更新
 */
interface RenderInBattle
{
    fun shouldRemove(): Boolean
    {
        return false
    }

    fun render(sb: SpriteBatch)

    fun update()

    fun updateAnimation()
    {
    }

    sealed class RenderType
    {

        /**
         * 比姿态还靠前一点点
         */
        data object Stance : RenderType()

        /**
         * 在UI层，但是是优先级最低的UI
         */
        data object Panel : RenderType()

        /**
         * 在战斗中，会被UI等遮盖
         */
        data object Normal : RenderType()  //AbstractRoom.render

        /**
         * 最高层
         */
        data object AbovePanel : RenderType()  //Top
        companion object
        {
            fun values(): Array<RenderType>
            {
                return arrayOf(Stance, Panel, Normal, AbovePanel)
            }

            fun valueOf(value: String): RenderType
            {
                return when (value)
                {
                    "Stance"     -> Stance
                    "Panel"      -> Panel
                    "Normal"     -> Normal
                    "AbovePanel" -> AbovePanel
                    else         -> throw IllegalArgumentException("No object superstitioapi.renderManager.inBattleManager.RenderInBattle.RenderType.$value")
                }
            }

            fun nameOf(renderType: RenderType): String
            {
                return when (renderType)
                {
                    Stance     -> "Stance"
                    Panel      -> "Panel"
                    Normal     -> "Normal"
                    AbovePanel -> "AbovePanel"
                    else       -> throw IllegalArgumentException("No object superstitioapi.renderManager.inBattleManager.RenderInBattle.RenderType.$renderType")
                }
            }
        }
    }

    companion object
    {
        fun clearAll()
        {
            RENDER_IN_BATTLES.clear()
            RENDER_IN_BATTLES_STANCE.clear()
            RENDER_IN_BATTLES_PANEL.clear()
            RENDER_IN_BATTLES_ABOVE_PANEL.clear()
            ShouldRemove.clear()
        }

        fun Register(renderType: RenderType, vararg renderThings: RenderInBattle)
        {
            Collections.addAll(getRenderGroup(renderType), *renderThings)
            Logger.info("register " + renderThings.contentToString() + " to " + RenderType.nameOf(renderType))
        }

        fun getRenderGroup(renderType: RenderType?): MutableList<RenderInBattle>
        {
            return when (renderType)
            {
                RenderType.Panel      -> RENDER_IN_BATTLES_PANEL
                RenderType.AbovePanel -> RENDER_IN_BATTLES_ABOVE_PANEL
                RenderType.Stance     -> RENDER_IN_BATTLES_STANCE
                RenderType.Normal     -> RENDER_IN_BATTLES
                else                  -> RENDER_IN_BATTLES
            }
        }

        fun forEachRenderInBattle(consumer: Consumer<RenderInBattle>?)
        {
            RENDER_IN_BATTLES_STANCE.forEach(consumer)
            RENDER_IN_BATTLES.forEach(consumer)
            RENDER_IN_BATTLES_PANEL.forEach(consumer)
            RENDER_IN_BATTLES_ABOVE_PANEL.forEach(consumer)
        }

        fun forEachRenderInBattleGroup(consumer: Consumer<MutableList<RenderInBattle>>)
        {
            consumer.accept(RENDER_IN_BATTLES_STANCE)
            consumer.accept(RENDER_IN_BATTLES)
            consumer.accept(RENDER_IN_BATTLES_PANEL)
            consumer.accept(RENDER_IN_BATTLES_ABOVE_PANEL)
        }

        val RENDER_IN_BATTLES: MutableList<RenderInBattle> = ArrayList()
        val RENDER_IN_BATTLES_STANCE: MutableList<RenderInBattle> = ArrayList()
        val RENDER_IN_BATTLES_PANEL: MutableList<RenderInBattle> = ArrayList()
        val RENDER_IN_BATTLES_ABOVE_PANEL: MutableList<RenderInBattle> = ArrayList()
        val ShouldRemove: MutableList<RenderInBattle> = ArrayList()

        object RenderInBattlePatch
        {
            @SpirePatch2(clz = AbstractRoom::class, method = "update")
            object UpdatePatch
            {
                @SpirePostfixPatch
                @JvmStatic
                fun Postfix(__instance: AbstractRoom?)
                {
                    if (AbstractDungeon.isScreenUp) return
                    if (ActionUtility.isNotInBattle) return
                    forEachRenderInBattle(RenderInBattle::update)
                    forEachRenderInBattle(RenderInBattle::updateAnimation)
                    forEachRenderInBattle { renderInBattle: RenderInBattle ->
                        if (renderInBattle.shouldRemove() && !ShouldRemove.contains(renderInBattle))
                        {
                            ShouldRemove.add(renderInBattle)
                            AutoDoneInstantAction.addToBotAbstract(
                                VoidSupplier {
                                    forEachRenderInBattleGroup(Consumer<MutableList<RenderInBattle>>
                                    { renderInBattles: MutableList<RenderInBattle> ->
                                        Logger.info("stopRender$renderInBattle")
                                        renderInBattles.remove(renderInBattle)
                                        ShouldRemove.remove(renderInBattle)
                                    })
                                }
                            )
                        }
                    }
                }
            }

            @SpirePatch2(clz = AbstractStance::class, method = "render", paramtypez = [SpriteBatch::class])
            object StanceRenderPatch1
            {
                @SpirePrefixPatch
                @JvmStatic
                fun Prefix(__instance: AbstractStance?, sb: SpriteBatch)
                {
                    if (ActionUtility.isNotInBattle) return
                    sb.color = Color.WHITE
                    RENDER_IN_BATTLES_STANCE.forEach(Consumer { renderInBattle: RenderInBattle ->
                        renderInBattle.render(
                            sb
                        )
                    })
                }
            }

            @SpirePatch2(clz = NeutralStance::class, method = "render", paramtypez = [SpriteBatch::class])
            object StanceRenderPatch2
            {
                @SpirePrefixPatch
                @JvmStatic
                fun Prefix(__instance: NeutralStance?, sb: SpriteBatch)
                {
                    if (ActionUtility.isNotInBattle) return
                    sb.color = Color.WHITE
                    RENDER_IN_BATTLES_STANCE.forEach(Consumer { renderInBattle: RenderInBattle ->
                        renderInBattle.render(
                            sb
                        )
                    })
                }
            }


            @SpirePatch2(clz = AbstractRoom::class, method = "render", paramtypez = [SpriteBatch::class])
            object InGameRenderPatch
            {
                @SpireInsertPatch(rloc = 16)
                @JvmStatic
                fun Insert(__instance: AbstractRoom?, sb: SpriteBatch)
                {
                    if (ActionUtility.isNotInBattle) return
                    sb.color = Color.WHITE
                    RENDER_IN_BATTLES.forEach(Consumer { renderInBattle: RenderInBattle -> renderInBattle.render(sb) })
                }
            }

            @SpirePatch2(clz = OverlayMenu::class, method = "render", paramtypez = [SpriteBatch::class])
            object OverlayMenuRenderPatch
            {
                @SpirePrefixPatch
                @JvmStatic
                fun Prefix(__instance: OverlayMenu?, sb: SpriteBatch)
                {
                    if (Settings.hideLowerElements) return
                    if (ActionUtility.isNotInBattle) return
                    sb.color = Color.WHITE
                    RENDER_IN_BATTLES_PANEL.forEach(Consumer { renderInBattle: RenderInBattle ->
                        renderInBattle.render(
                            sb
                        )
                    })
                }
            }

            @SpirePatch2(clz = AbstractRoom::class, method = "renderAboveTopPanel", paramtypez = [SpriteBatch::class])
            object RenderAboveTopPanelPatch
            {
                @SpirePostfixPatch
                @JvmStatic
                fun Postfix(__instance: AbstractRoom?, sb: SpriteBatch)
                {
                    if (AbstractDungeon.isScreenUp) return
                    if (ActionUtility.isNotInBattle) return
                    sb.color = Color.WHITE
                    RENDER_IN_BATTLES_ABOVE_PANEL.forEach(Consumer { renderInBattle: RenderInBattle ->
                        renderInBattle.render(
                            sb
                        )
                    })
                }
            }
        }
    }
}
