package superstitio.delayHpLose

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.cards.DamageInfo
import superstitio.DataManager
import superstitio.cardModifier.modifiers.AbstractLupaBlock

class RemoveDelayHpLoseBlock : AbstractLupaBlock(ID) {
    override fun onThisBlockDamaged(info: DamageInfo, lostAmount: Int) {
    }

    //    @Override
    //    public float onModifyBlockFinal(float block, AbstractCard card) {
    //        ActionUtility.addToBot_reducePower(new DelayHealthDrop(this.owner, (int) block, this.owner), this.owner);
    //        return super.onModifyBlockFinal(0, card);
    //    }
    //    @SpirePatch2(clz = AbstractPlayer.class, method = "<class>")
    //    public static class IsImmunityFields {
    //        public static SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>> checkShouldImmunity;
    //
    //        static {
    //            DelayHpLosePatch.IsImmunityFields.checkShouldImmunity =
    //                    (SpireField<TriPredicate<AbstractPlayer, DamageInfo, Integer>>) new SpireField(() -> null);
    //        }
    //    }
    override fun makeCopy(): AbstractBlockModifier {
        return RemoveDelayHpLoseBlock()
    }

    override fun blockImageColor(): Color {
        return Color.PINK.cpy()
    }

    companion object {
        val ID: String = DataManager.MakeTextID(RemoveDelayHpLoseBlock::class.java)
    }
}