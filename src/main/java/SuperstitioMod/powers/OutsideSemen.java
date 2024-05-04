package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.BlockMod.DrySemenBlock;
import com.evacipated.cardcrawl.mod.stslib.actions.common.GainCustomBlockAction;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModContainer;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class OutsideSemen extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(OutsideSemen.class.getSimpleName());

    public OutsideSemen(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new GainCustomBlockAction(new BlockModContainer(this, new DrySemenBlock()), this.owner, this.amount));
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}
