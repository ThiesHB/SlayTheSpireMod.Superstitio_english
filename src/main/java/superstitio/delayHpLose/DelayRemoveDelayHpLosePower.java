package superstitio.delayHpLose;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.RenderAsBlockPower;
import superstitio.powers.AbstractLupaPower;

import java.util.ArrayList;

public class DelayRemoveDelayHpLosePower extends AbstractLupaPower implements RenderAsBlockPower {
    public static final String POWER_ID = DataManager.MakeTextID(DelayRemoveDelayHpLosePower.class);
    public final BlockInstance blockInstance;

    public DelayRemoveDelayHpLosePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount);
        ArrayList<AbstractBlockModifier> block = new ArrayList<>();
        block.add(new DelayRemoveDelayHpLoseBlock());
        this.blockInstance = new BlockInstance(owner, amount, block);
    }

    @Override
    public void atStartOfTurn() {
        DelayHpLosePower.addToBot_removePower(amount, this.owner, this.owner, false);
        addToBot_removeSpecificPower(this);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }


    @Override
    public BlockInstance getBlockInstance() {
        return blockInstance;
    }
}
