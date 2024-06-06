package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import superstitio.Logger;

public class PregnantBlock_newMonster extends PregnantBlock {
    private final AbstractCreature sealCreature;
    private final AbstractCreature father;

    public PregnantBlock_newMonster(AbstractCreature father) {
        super();
        AbstractCreature fatherCreature;
        AbstractCreature sealCreature;
        try {
            fatherCreature = father;
            sealCreature = father.getClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            fatherCreature = new ApologySlime();
            sealCreature = new ApologySlime();
            Logger.error(e);
        }
        this.father = fatherCreature;
        this.sealCreature = sealCreature;
        this.sealCreature.currentHealth = Math.max(this.sealCreature.currentHealth / 4, 20);
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        return new PregnantBlock_newMonster(this.father);
    }

    @Override
    public void removeUnNaturally(DamageInfo info, int remainingDamage) {
//        removeUnNatural.get();
    }

    @Override
    public void removeNaturally(int remainingDamage) {
//        removeNatural.get();
    }
}