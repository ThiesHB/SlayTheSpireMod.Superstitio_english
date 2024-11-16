package superstitio.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.utils.PowerUtility;

import java.util.HashMap;
import java.util.Map;

public class SexToy extends AbstractSuperstitioPower {
    public static final String POWER_ID = DataManager.MakeTextID(SexToy.class);
    private static final int SEXUAL_HEAT_RATE = 1;
    public Map<String, Integer> sexToyNames;

    public SexToy(final AbstractCreature owner, int amount, String sexToyName) {
        super(POWER_ID, owner, amount, PowerType.BUFF, false);

        this.sexToyNames = new HashMap<>();
        this.sexToyNames.put(sexToyName, amount);

        this.updateDescription();
    }

    public void Trigger() {
        this.flash();
        SexualHeat.addToBot_addSexualHeat(this.owner, this.amount / SEXUAL_HEAT_RATE);
    }

    @Override
    public void updateDescriptionArgs() {
        final String[] SexToysString = {""};
        this.sexToyNames.forEach((name, num) -> SexToysString[0] += String.format(powerStrings.getDESCRIPTION(1), name, num));
        setDescriptionArgs(SEXUAL_HEAT_RATE, SexToysString[0]);
    }

    @Override
    public void atStartOfTurn() {
        AutoDoneInstantAction.addToBotAbstract(this::Trigger);
//        Trigger();
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (!(power instanceof SexToy)) {
            return;
        }
        this.sexToyNames = PowerUtility.mergeAndSumMaps(this.sexToyNames, ((SexToy) power).sexToyNames);
        updateDescription();
    }
}
