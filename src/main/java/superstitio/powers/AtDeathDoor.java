package superstitio.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;

public class AtDeathDoor extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(AtDeathDoor.class.getSimpleName());

    public AtDeathDoor(final AbstractCreature owner) {
        super(POWER_ID, owner, -1);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.HP_LOSS)
            return 0;
        return super.onAttackedToChangeDamage(info, damageAmount);
    }
}
