package superstitio.powers

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitioapi.utils.setDescriptionArgs

class DelayDamage(owner: AbstractCreature, amount: Int, private val damageAmount: Int) : AbstractSuperstitioPower(
    POWER_ID, owner, amount
), HealthBarRenderPower
{
    private var giver: AbstractCreature = AbstractDungeon.player
    private var damageModifier: AbstractDamageModifier? = null

    fun setupGiver(giver: AbstractCreature): DelayDamage
    {
        this.giver = giver
        return this
    }

    fun setupDamageModifier(damageModifier: AbstractDamageModifier?): DelayDamage
    {
        this.damageModifier = damageModifier
        return this
    }

    override fun atEndOfRound()
    {
        addToBot_AutoRemoveOne(this)
    }

    override fun onRemove()
    {
        if (damageModifier != null) addToBot(
            DamageAction(
                this.owner, BindingHelper.makeInfo(
                    DamageModContainer(this, damageModifier), giver, this.damageAmount,
                    DamageType.NORMAL
                )
            )
        )
        addToBot(DamageAction(this.owner, DamageInfo(giver, this.damageAmount, DamageType.NORMAL)))
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(this.amount)
    }

    override fun getHealthBarAmount(): Int
    {
        return this.damageAmount
    }

    override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color)
    {
        super.renderAmount(sb, x, y, c)
        renderAmount2(sb, x, y, c, this.damageAmount)
        //        AbstractDungeon.actionManager.currentAction.target
    }

    override fun getColor(): Color
    {
        return BarColor
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(DelayDamage::class.java)
        private val BarColor: Color = Color.ORANGE.cpy()
    }
}
