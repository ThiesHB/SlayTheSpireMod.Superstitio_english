package superstitio.cards.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cards.modifiers.AbstractLupaBlock;

public class PregnantBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(PregnantBlock.class.getSimpleName());

    public PregnantBlock() {
        super(ID);
    }

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {

    }

    @Override
    public boolean shouldStack() {
        return false;
    }

    @Override
    public int amountLostAtStartOfTurn() {
        return 0;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock();
    }



    @Override
    public Priority priority() {
        return Priority.BOTTOM;
    }
}