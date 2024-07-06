package superstitio.cardModifier.modifiers.block;

import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.cardModifier.modifiers.AbstractLupaBlock;

public abstract class PregnantBlock extends AbstractLupaBlock {

    public PregnantBlock(String id) {
        super(id);
    }

//    @Override
//    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {
//
//    }

    ///受到外力则为流产
    public int removeUnNaturally(DamageInfo info, int remainingDamage) {
        return remainingDamage;
    }

    ///正常顺产的效果
    public int removeNaturally(int remainingDamage) {
        return remainingDamage;
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
    public final int onRemove(boolean lostByStartOfTurn, DamageInfo info, int remainingDamage) {
        int remain = remainingDamage;
        if (info != null)
            remain = removeUnNaturally(info, remain);
        else
            remain = removeNaturally(remain);
        return remain;
    }

    @Override
    public Priority priority() {
        return Priority.BOTTOM;
    }
}