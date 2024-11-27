package superstitioapi.powers.barIndepend

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.powers.AbstractPower
import java.util.function.BiFunction
import java.util.function.Supplier

interface HasBarRenderOnCreature_Power : HasBarRenderOnCreature {
    val self: AbstractPower
        get() = this as AbstractPower

    override fun makeNewBarRenderOnCreature():
            BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, out RenderOnThing> {
        return BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, RenderOnThing>(::BarRenderOnThing)
    }

    override fun uuidOfSelf(): String {
        return self.ID
    }

    override fun uuidPointTo(): String {
        if (self.owner is AbstractPlayer)
            return super.uuidPointTo() + ":player:" + self.owner.name
        return super.uuidPointTo() + ":" + self.owner.name
    }

    override fun getAmountForDraw(): Int = self.amount

    override fun maxBarAmount(): Int

    override val name: String
        get() = self.name

    override val description: String
        get() = self.description

    override fun getBarRenderHitBox(): Hitbox = self.owner.hb
}
