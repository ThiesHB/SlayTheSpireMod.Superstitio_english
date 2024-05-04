package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExposeSelf extends AbstractLupaPower implements OnLoseBlockPower {
    public static final String POWER_ID = DataManager.MakeTextID(ExposeSelf.class.getSimpleName() );

    public ExposeSelf(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);

    }

    @Override
    public int onLoseBlock(DamageInfo damageInfo, int i) {
        this.addToBot(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player, new OutsideSemen(AbstractDungeon.player, i)));
        return i;
    }

    @Override
    public void atStartOfTurn() {
        addToBot_AutoRemoveOne(this);
    }

    @Override
    public void updateDescriptionArgs() {
    }
}
