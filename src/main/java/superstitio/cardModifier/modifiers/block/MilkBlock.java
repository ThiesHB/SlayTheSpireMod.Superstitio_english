package superstitio.cardModifier.modifiers.block;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaBlock;

public class MilkBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(MilkBlock.class);

    public MilkBlock() {
        super(ID);
    }

    @Override
    public void onThisBlockDamaged(DamageInfo info, int lostAmount) {

    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new MilkBlock();
    }

    @Override
    public Color blockImageColor() {
        return Color.WHITE.cpy();
    }
}