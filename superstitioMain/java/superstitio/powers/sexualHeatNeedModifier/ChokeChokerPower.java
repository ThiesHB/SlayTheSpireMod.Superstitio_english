package superstitio.powers.sexualHeatNeedModifier;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.cardModifier.modifiers.damage.UnBlockAbleHpLoseLikeDamage;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm;
import superstitioapi.cards.DamageActionMaker;

@SuperstitioImg.NoNeedImg
public class ChokeChokerPower extends AbstractSuperstitioPower implements
        OnOrgasm_onOrgasm, SexualHeatNeedModifier {
    public static final String POWER_ID = DataManager.MakeTextID(ChokeChokerPower.class);
    public static final int ChokeRate = 2;

    public ChokeChokerPower(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.loadRegion("choke");
    }

    @Override
    public void atStartOfTurnPostDraw() {
//            AddPowers();
    }

//        @Override
//        public void InitializePostApplyThisPower(ChokeChokerPower addedPower) {
//            AutoDoneInstantAction.addToBotAbstract(addedPower::AddPowers);
//        }

//        public void AddPowers() {
//            this.addToBot(new ApplyPowerAction(this.owner, this.owner, new InTurnSexualHeatNeededModifier(this.owner, amount)));
//        }


    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        this.flash();
        DamageActionMaker.maker(this.amount / ChokeRate, this.owner)
                .setSource(this.owner)
                .setEffect(AbstractGameAction.AttackEffect.NONE)
                .setSkipWait(true)
                .setDamageModifier(this, new UnBlockAbleHpLoseLikeDamage())
                .setDamageType(DataManager.CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose)
                .addToTop();
//            for (int i = 0; i < this.amount; i++) {
//                this.addToBot(new LoseHPAction(this.owner, null, 1));
//            }
//            this.addToBot(new LoseHPAction(this.owner, null, this.amount));
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount, amount / ChokeRate);
    }

    @Override
    public int reduceSexualHeatNeeded() {
        return this.amount;
    }
}
