package superstitio.delayHpLose;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaBlock;

public class RemoveDelayHpLoseBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(RemoveDelayHpLoseBlock.class);

    public RemoveDelayHpLoseBlock() {
        super(ID);
    }

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {

    }

//    @Override
//    public float onModifyBlockFinal(float block, AbstractCard card) {
//        ActionUtility.addToBot_reducePower(new DelayHealthDrop(this.owner, (int) block, this.owner), this.owner);
//        return super.onModifyBlockFinal(0, card);
//    }

//    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
//    public static class IsImmunityFields {
//        public static SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>> checkShouldImmunity;
//
//        static {
//            DelayHpLosePatch.IsImmunityFields.checkShouldImmunity =
//                    (SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>>) new SpireField(() -> null);
//        }
//    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new RemoveDelayHpLoseBlock();
    }

    @Override
    public Color blockImageColor() {
        return Color.PINK.cpy();
    }
}