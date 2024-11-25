package superstitio.powers.lupaOnly

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.helpers.Hitbox
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
import java.util.function.*

@NoNeedImg
class FloorSemen(owner: AbstractCreature, amount: Int) :
    AbstractSuperstitioPower(POWER_ID, owner, amount, if (owner.isPlayer) PowerType.BUFF else PowerType.DEBUFF, false),
    SemenPower, InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_SemenPower {
    init {
        updateDescription()
    }

    override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color) {
    }

    override fun onRemove() {
        InBattleDataManager.getBarRenderManager()?.let { barRenderManager: BarRenderManager ->
            barRenderManager.removeChunk(
                this
            )
        }
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(this.amount, getTotalValue())
    }

    override fun getSemenValue(): Int = semenValue

    override fun setupBarOrginColor(): Color {
        return ImgUtility.mixColor(HasBarRenderOnCreature_SemenPower.semenColor(), Color.BROWN, 0.5f, 0.9f)
    }

    override fun makeNewBarRenderOnCreature(): BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, out RenderOnThing> {
        return BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, RenderOnThing>(HasBarRenderOnCreature_SemenPower.Companion::makeNewBar_BodySemen)
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(FloorSemen::class.java)
        const val semenValue: Int = 1
    }
}
