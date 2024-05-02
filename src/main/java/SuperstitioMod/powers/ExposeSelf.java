package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnLoseBlockPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class ExposeSelf extends AbstractLupaPower implements OnLoseBlockPower {
    public static final String POWER_ID = DataManager.MakeTextID(ExposeSelf.class.getSimpleName() + "Power");

    public ExposeSelf(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);

    }

    @Override
    public int onLoseBlock(DamageInfo damageInfo, int i) {
        this.addToBot(new ApplyPowerAction(
                AbstractDungeon.player, AbstractDungeon.player, new DrySemen(AbstractDungeon.player, i)));
        return i;
    }

    @Override
    public void atStartOfTurn() {
        if (this.amount == 1) {
            this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
        else {
            this.addToBot(new ReducePowerAction(this.owner, this.owner, POWER_ID, 1));
        }
    }

    @Override
    public void updateDescriptionArgs() {
    }
}
