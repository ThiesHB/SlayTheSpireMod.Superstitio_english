package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class AtDeathDoor extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(AtDeathDoor.class.getSimpleName() + "Power");
    public AtDeathDoor(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.HP_LOSS)
            return 0;
        return super.onAttackedToChangeDamage(info,damageAmount);
    }
}
