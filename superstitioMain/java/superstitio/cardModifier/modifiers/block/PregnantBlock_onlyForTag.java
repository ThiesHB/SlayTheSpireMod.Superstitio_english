package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import superstitio.DataManager;

public class PregnantBlock_onlyForTag extends PregnantBlock {

    public static final String ID = DataManager.MakeTextID(PregnantBlock_onlyForTag.class);

    public PregnantBlock_onlyForTag() {
        super();
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock_onlyForTag();
    }

    @Override
    public void removeUnNaturally(DamageInfo info, int remainingDamage) {
    }

    @Override
    public void removeNaturally(int remainingDamage) {
    }
}