package superstitio.powers.lupaOnly

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.SuperstitioConfig
import superstitioapi.powers.barIndepend.BarRenderOnThing_Vertical
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature_Power
import java.util.function.Supplier

interface HasBarRenderOnCreature_SemenPower : HasBarRenderOnCreature_Power, SemenPower {
    override val self: AbstractPower
        get() = this as AbstractPower

    override fun Height(): Float {
        return 170 * Settings.scale
    }

    override fun makeBarText(): String {
        return "%d"
    }

    override fun maxBarAmount(): Int {
        return Integer.max((self.amount * 1.5f).toInt(), self.owner.maxHealth / 2)
    }


    override fun getAmountForDraw(): Int = self.amount * this.getSemenValue()

    override fun uuidPointTo(): String {
        return "AllSemen"
    }

    companion object {
        fun semenColor(): Color {
            return if (SuperstitioConfig.isEnableSFW()) Color.RED.cpy()
            else Color.WHITE.cpy()
        }

        fun makeNewBar_BodySemen(
            hitbox: Supplier<Hitbox>,
            power: HasBarRenderOnCreature
        ): BarRenderOnThing_Vertical {
            val bar = BarRenderOnThing_Vertical(hitbox, power)
            bar.barTextColor = semenColor()
            return bar
        }
    }
}
