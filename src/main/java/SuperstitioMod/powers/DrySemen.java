package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DrySemen extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(DrySemen.class.getSimpleName() );

    public DrySemen(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, this.amount));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}
