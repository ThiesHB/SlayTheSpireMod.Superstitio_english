package SuperstitioMod.cards.BlockMod;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Abstract.AbstractLupaBlock;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;

public class MilkBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(MilkBlock.class.getSimpleName());

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