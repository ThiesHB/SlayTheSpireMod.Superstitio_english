package superstitio.cards.modifiers.block;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cards.modifiers.AbstractLupaBlock;

public class DrySemenBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(DrySemenBlock.class.getSimpleName());

    public DrySemenBlock() {
        super(ID);
    }

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new DrySemenBlock();
    }

    @Override
    public Priority priority() {
        return Priority.TOP;
    }

    @Override
    public Color blockImageColor() {
        return Color.LIGHT_GRAY.cpy();
    }
}