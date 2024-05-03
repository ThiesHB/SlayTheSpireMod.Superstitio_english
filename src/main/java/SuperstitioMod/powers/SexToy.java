package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.utils.PowerUtility;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;

public class SexToy extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(SexToy.class.getSimpleName() );
    private static final int SEXUAL_HEAT_RATE = 1;
    public Map<String, Integer> sexToyNames;

    public SexToy(final AbstractCreature owner, int amount, String sexToyName) {
        super(POWER_ID, owner, amount, PowerType.BUFF, false);

        this.sexToyNames = new HashMap<>();
        this.sexToyNames.put(sexToyName, amount);

        this.updateDescription();
    }

    @Override
    public void updateDescriptionArgs() {
        final String[] SexToysString = {""};
        this.sexToyNames.forEach((name, num) -> SexToysString[0] += String.format(powerStringsSet.getRightVersion().DESCRIPTIONS[1], name, num));
        setDescriptionArgs(SEXUAL_HEAT_RATE, SexToysString[0]);
    }

    @Override
    public void atStartOfTurn() {
        this.addToBot(new ApplyPowerAction(this.owner, this.owner, new SexualHeat(this.owner, this.amount / SEXUAL_HEAT_RATE)));
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
