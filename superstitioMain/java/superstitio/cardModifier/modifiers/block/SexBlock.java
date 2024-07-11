package superstitio.cardModifier.modifiers.block;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaBlock;

public class SexBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(SexBlock.class);

    public SexBlock() {
        super(ID);
    }

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {
    }

    public Priority priority() {
        return Priority.NORMAL;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new SexBlock();
    }

//    @Override
//    public Color blockImageColor() {
//        return Color.PINK.cpy();
//    }
}