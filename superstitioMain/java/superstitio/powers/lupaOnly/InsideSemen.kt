package superstitio.powers.lupaOnly

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.powers.barIndepend.BarRenderManager
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature
import superstitioapi.powers.barIndepend.RenderOnThing
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.ImgUtility
import superstitioapi.utils.setDescriptionArgs
import java.util.function.BiFunction
import java.util.function.Supplier

@NoNeedImg
class InsideSemen(owner: AbstractCreature, amount: Int) :
    AbstractSuperstitioPower(POWER_ID, owner, amount, if (owner.isPlayer) PowerType.BUFF else PowerType.DEBUFF, false),
    SemenPower, InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_SemenPower
{
    //    public int maxSemen;
    //TODO 改装成不同怪物获得不同精液名称
    //    public String semenSource;
    init
    {
        updateDescription()
    }

    override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color)
    {
    }

    override fun onRemove()
    {
        InBattleDataManager.getBarRenderManager()?.let { barRenderManager: BarRenderManager ->
            barRenderManager.removeChunk(
                this
            )
        }
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(this.amount, getTotalValue())
    }

    override val self: AbstractPower
        get() = this

    override fun setupBarOriginColor(): Color
    {
        return ImgUtility.mixColor(HasBarRenderOnCreature_SemenPower.semenColor(), Color.PINK, 0.3f, 0.9f)
    }

    override fun getSemenValue(): Int = semenValue

    override fun makeNewBarRenderOnCreature(): BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, out RenderOnThing>
    {
        return BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, RenderOnThing>(HasBarRenderOnCreature_SemenPower.Companion::makeNewBar_BodySemen)
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(InsideSemen::class.java)

        //    public static final int MAX_Semen_Origin = 10;
        const val semenValue: Int = 3

        private const val ToOutSideSemenRate = 1
    }
}