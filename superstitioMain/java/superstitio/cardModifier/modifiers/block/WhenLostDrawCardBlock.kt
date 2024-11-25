package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaBlock;

public class WhenLostDrawCardBlock extends AbstractLupaBlock {
    public static final String ID = DataManager.MakeTextID(WhenLostDrawCardBlock.class);
    private final int drawAmount;

    public WhenLostDrawCardBlock() {
        this(1);
    }

    public WhenLostDrawCardBlock(int drawAmount) {
        super(ID);
        this.drawAmount = drawAmount;
    }

    @Override
    public int amountLostAtStartOfTurn() {
        return 0;
    }

    @Override
    public boolean shouldStack() {
        return false;
    }

    @Override
    public final int onRemove(boolean lostByStartOfTurn, DamageInfo info, int remainingDamage) {
        if (info != null) {
            addToBot(new DrawCardAction(drawAmount));
        }
        return remainingDamage;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.drawAmount);
    }

    @Override
    public Priority priority() {
        return Priority.TOP;
    }

    @Override
    public short subPriority() {
        return 255;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new WhenLostDrawCardBlock();
    }
}