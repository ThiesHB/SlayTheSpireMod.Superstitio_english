package superstitio.powers.sexualHeatNeedModifier

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.AbstractCreature
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.cardModifier.modifiers.damage.UnBlockAbleHpLoseLikeDamage
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm
import superstitioapi.cards.DamageActionMaker
import superstitioapi.utils.setDescriptionArgs

@NoNeedImg
class ChokeChokerPower(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount),
    OnOrgasm_onOrgasm, SexualHeatNeedModifier {
    init {
        this.loadRegion("choke")
    }

    override fun atStartOfTurnPostDraw() {
//            AddPowers();
    }


    //        @Override
    //        public void InitializePostApplyThisPower(ChokeChokerPower addedPower) {
    //            AutoDoneInstantAction.addToBotAbstract(addedPower::AddPowers);
    //        }
    //        public void AddPowers() {
    //            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new InTurnSexualHeatNeededModifier(this.owner, amount)));
    //        }
    override fun onOrgasm(SexualHeatPower: SexualHeat) {
        this.flash()
        DamageActionMaker.maker(this.amount / ChokeRate, this.owner)
            .setSource(this.owner)
            .setEffect(AttackEffect.NONE)
            .setSkipWait(true)
            .setDamageModifier(this, UnBlockAbleHpLoseLikeDamage())
            .setDamageType(CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose)
            .addToTop()
        //            for (int i = 0; i < this.amount; i++) {
//                this.addToBot(new LoseHPAction(this.owner, null, 1));
//            }
//            this.addToBot(new LoseHPAction(this.owner, null, this.amount));
    }

    override fun updateDescriptionArgs() {
        this.setDescriptionArgs(amount, amount / ChokeRate)
    }

    override fun reduceSexualHeatNeeded(): Int {
        return this.amount
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(ChokeChokerPower::class.java)
        const val ChokeRate: Int = 2
    }
}
