package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DeathDoor extends AbstractLupaPower implements OnPlayerDeathPower{
    public static final String POWER_ID = DataManager.MakeTextID(DeathDoor.class.getSimpleName() + "Power");
    public DeathDoor(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    public
}
