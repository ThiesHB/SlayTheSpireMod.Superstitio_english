package superstitio.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.orbs.CardOrb_CardTrigger;
import superstitio.powers.interfaces.orgasm.OnOrgasm_onOrgasm;

import static superstitio.InBattleDataManager.getHangUpCardOrbGroup;
import static superstitio.actions.AutoDoneInstantAction.addToBotAbstract;

public class FastWindUp extends AbstractLupaPower implements OnOrgasm_onOrgasm {
    public static final String POWER_ID = DataManager.MakeTextID(FastWindUp.class.getSimpleName());

    public FastWindUp(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        for (int i = 0; i < this.amount; i++) {
            addToBotAbstract(() -> getHangUpCardOrbGroup()
                    .ifPresent(cardGroup -> cardGroup.orbs.stream()
                            .filter(orb -> orb instanceof CardOrb_CardTrigger)
                            .forEach(orb -> ((CardOrb_CardTrigger) orb)
                                    .forceAcceptAction(new superstitio.cards.lupa.PowerCard.FastWindUp()))));
        }
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }
}
