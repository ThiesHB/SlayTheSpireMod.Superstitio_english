package superstitio.cardModifier.modifiers.block;

import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaBlock;

public abstract class PregnantBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(PregnantBlock.class);

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
    public final int onRemove(boolean lostByStartOfTurn, DamageInfo info, int remainingDamage) {
        if (info != null)
            removeUnNaturally(info, remainingDamage);
        else
            removeNaturally(info, remainingDamage);
        return remainingDamage;
    }

    ///受到外力则为流产
    public abstract void removeUnNaturally(DamageInfo info, int remainingDamage);

    ///正常顺产的效果
    public abstract void removeNaturally(DamageInfo info, int remainingDamage);

    @Override
    public Priority priority() {
        return Priority.BOTTOM;
    }
}