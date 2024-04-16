package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.utils.PowerUtility;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;

public class SexToy extends AbstractLupaPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexToy.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    private static final int SEXUAL_HEAT_RATE = 5;
    public Map<String, Integer> sexToyNames;

    public SexToy(final AbstractCreature owner, int amount, String sexToyName) {
        super(POWER_ID, powerStrings.NAME, owner, amount, PowerType.BUFF, false);

        this.sexToyNames = new HashMap<>();
        this.sexToyNames.put(sexToyName, amount);

        this.updateDescription();
    }


    @Override
    public void updateDescription() {
        this.description = String.format(SexToy.powerStrings.DESCRIPTIONS[0], SEXUAL_HEAT_RATE);
        this.sexToyNames.forEach((name, num) ->
                this.description = this.description + String.format(SexToy.powerStrings.DESCRIPTIONS[1], name, num));
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
