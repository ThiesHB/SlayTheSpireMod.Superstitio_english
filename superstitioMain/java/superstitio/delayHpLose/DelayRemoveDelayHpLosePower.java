package superstitio.delayHpLose;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.powers.AbstractSuperstitioPower;
import superstitioapi.cardModifier.RenderAsBlockPower;
import superstitioapi.cardModifier.RenderStackedBlockInstancesPatch;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;

import java.util.ArrayList;

public class DelayRemoveDelayHpLosePower extends AbstractSuperstitioPower implements RenderAsBlockPower, InvisiblePower,
        OnPostApplyThisPower<DelayRemoveDelayHpLosePower> {
    public static final String POWER_ID = DataManager.MakeTextID(DelayRemoveDelayHpLosePower.class);
    public BlockInstance blockInstance;

    public DelayRemoveDelayHpLosePower(final AbstractCreature owner, final int amount) {
        super(POWER_ID, owner, amount);
    }

    private BlockInstance makeBlockInstance() {
        ArrayList<AbstractBlockModifier> block = new ArrayList<>();
        block.add(new DelayRemoveDelayHpLoseBlock());
        return new BlockInstance(owner, amount, block);
    }

    @Override
    public void atStartOfTurnPostDraw() {
        DelayHpLosePower.addToBot_removePower(DelayHpLosePower.class, amount, AbstractDungeon.player, true);
        addToBot_removeSpecificPower(this);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public BlockInstance getBlockInstance() {
        if (blockInstance == null) {
            this.blockInstance = makeBlockInstance();
        }
        return blockInstance;
    }

    @Override
    public void InitializePostApplyThisPower(DelayRemoveDelayHpLosePower addedPower) {
        this.blockInstance = makeBlockInstance();
        RenderStackedBlockInstancesPatch.BlockStackElementField.forceDrawBlock.set(owner, true);
    }
}
