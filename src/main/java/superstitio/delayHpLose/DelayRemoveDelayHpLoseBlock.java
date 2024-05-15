package superstitio.delayHpLose;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.AbstractLupaBlock;

public class DelayRemoveDelayHpLoseBlock extends AbstractLupaBlock {

    public static final String ID = DataManager.MakeTextID(DelayRemoveDelayHpLoseBlock.class);

    public DelayRemoveDelayHpLoseBlock() {
        super(ID);
    }

    @Override
    public float onModifyBlock(float block, AbstractCard card) {
        return super.onModifyBlock(block, card);
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new DelayRemoveDelayHpLoseBlock();
    }

    @Override
    public Color blockImageColor() {
        return Color.PINK.cpy();
    }
}