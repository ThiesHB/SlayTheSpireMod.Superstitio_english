package superstitio.powers.lupaOnly;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;

public class ExposeSelf extends AbstractSuperstitioPower {
    public static final String POWER_ID = DataManager.MakeTextID(ExposeSelf.class);

    public ExposeSelf(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);

    }


    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            this.flash();
            this.addToBot(new ApplyPowerAction(
                    AbstractDungeon.player, AbstractDungeon.player, new OutsideSemen(AbstractDungeon.player, damageAmount)));
        }
        return super.onAttacked(info, damageAmount);
    }

    @Override
    public void atStartOfTurn() {
        addToBot_AutoRemoveOne(this);
    }

    @Override
    public void updateDescriptionArgs() {
    }
}
