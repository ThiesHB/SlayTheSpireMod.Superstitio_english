package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import superstitio.Logger;

import static superstitio.utils.ActionUtility.VoidSupplier;

public class PregnantBlock_newMonster extends PregnantBlock {
    private final AbstractCreature sealCreature;
    private final VoidSupplier removeUnNatural;
    private final VoidSupplier removeNatural;
    private final AbstractCreature father;

    public PregnantBlock_newMonster(AbstractCreature father, VoidSupplier removeUnNatural, VoidSupplier removeNatural) {
        super();
        this.father = father;
        AbstractCreature sealCreature;
        this.removeUnNatural = removeUnNatural;
        this.removeNatural = removeNatural;
        try {
            sealCreature = father.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            sealCreature = new ApologySlime();
            Logger.error(e);
        }
        this.sealCreature = sealCreature;
        this.sealCreature.currentHealth = Math.max(this.sealCreature.currentHealth / 4, 20);
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock_newMonster(this.father, this.removeUnNatural, this.removeNatural);
    }

    @Override
    public void removeUnNaturally(DamageInfo info, int remainingDamage) {
        removeUnNatural.get();
    }

    @Override
    public void removeNaturally(int remainingDamage) {
        removeNatural.get();
    }
}