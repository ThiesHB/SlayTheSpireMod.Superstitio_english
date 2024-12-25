package superstitio.powers

import basemod.interfaces.ISubscriber
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.delayHpLose.UnBlockAbleIgnoresTempHPDamage
import superstitioapi.SuperstitioApiSubscriber.AtEndOfPlayerTurnPreCardSubscriber
import superstitioapi.actions.DamageEnemiesAction
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.addToBot_removeSelf
import superstitioapi.utils.setDescriptionArgs

class SexualDamage //        ReflectionHacks.setPrivate(this, "greenColor", Color.PINK.cpy());
    (owner: AbstractCreature, amount: Int, protected val giver: AbstractCreature) :
    AbstractSuperstitioPower(POWER_ID, owner, amount), HealthBarRenderPower, OnPostApplyThisPower<SexualDamage>,
    AtEndOfPlayerTurnPreCardSubscriber
{
    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(this.amount)
    }

    override fun getHealthBarAmount(): Int
    {
        return this.amount
    }

    override fun getColor(): Color
    {
        return BarColor
    }

    override fun renderAmount(sb: SpriteBatch, x: Float, y: Float, c: Color)
    {
        val temp = this.fontScale
        this.fontScale *= 1.5f
        super.renderAmount(sb, x, y, c)
        this.fontScale = temp
    }

    override fun receiveAtEndOfPlayerTurnPreCard()
    {
//        this.owner.damage(BindingHelper.makeInfo(new DamageModContainer(this, new UnBlockAbleDamage()), giver, amount, DamageType.HP_LOSS));
        this.flash()
        DamageEnemiesAction.builder(this.amount, this.owner)
            .setSource(this.giver)
            .setDamageModifier(this, UnBlockAbleIgnoresTempHPDamage())
            .setDamageType(DamageType.HP_LOSS)
            .setAttackEffectType(AttackEffect.POISON)
            .setAfterDamageConsumer { PowerUtility.RemovePower(this.owner, this) }
            .addToTop()
        addToBot_removeSelf()
    }

    override fun InitializePostApplyThisPower(addedPower: SexualDamage)
    {
        if (InBattleDataManager.subscribeManageGroups.stream()
                .anyMatch { iSubscriber: ISubscriber? -> iSubscriber is SexualDamage && iSubscriber.owner === this.owner }
        ) return
        InBattleDataManager.Subscribe(this)
    }

    override fun onRemove()
    {
        InBattleDataManager.UnSubscribe(this)
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(SexualDamage::class.java)
        private val BarColor: Color = Color.PURPLE.cpy()
    }
}
