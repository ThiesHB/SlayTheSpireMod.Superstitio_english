package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;

import static superstitioapi.utils.ActionUtility.VoidSupplier;

public class PregnantBlock_chooseEffect extends PregnantBlock {
    public static final String ID = DataManager.MakeTextID(PregnantBlock_chooseEffect.class);

    private final AbstractCreature sealCreature;
    private final VoidSupplier removeUnNatural;
    private final VoidSupplier removeNatural;

    public PregnantBlock_chooseEffect(AbstractCreature father, VoidSupplier removeUnNatural, VoidSupplier removeNatural) {
        super(ID);
        this.removeUnNatural = removeUnNatural;
        this.removeNatural = removeNatural;
        this.sealCreature = father;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock_chooseEffect(this.sealCreature, this.removeUnNatural, this.removeNatural);
    }

    @Override
    public int removeUnNaturally(DamageInfo info, int remainingDamage) {
        removeUnNatural.get();
        return super.removeUnNaturally(info, remainingDamage);
    }

    @Override
    public int removeNaturally(int remainingDamage) {
        removeNatural.get();
        return super.removeNaturally(remainingDamage);
    }
}