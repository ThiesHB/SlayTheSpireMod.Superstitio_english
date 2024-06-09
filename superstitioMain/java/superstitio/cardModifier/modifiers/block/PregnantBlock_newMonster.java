package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import superstitio.Logger;
import superstitioapi.pet.CopyAndSpawnMonsterUtility;
import superstitioapi.pet.PetManager;

public class PregnantBlock_newMonster extends PregnantBlock {
    private final AbstractMonster sealCreature;
    private final AbstractMonster father;
    public PregnantBlock_newMonster(){
        sealCreature = null;
        father = null;
    }

    public PregnantBlock_newMonster(AbstractMonster father) {
        super();
        AbstractMonster fatherCreature;
        AbstractMonster sealCreature;
        fatherCreature = father;
        sealCreature = CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster(father.getClass());
        this.father = fatherCreature;
        this.sealCreature = sealCreature;
        this.sealCreature.currentHealth = Math.max(this.sealCreature.currentHealth / 4, 20);
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        if (this.father == null)
            return new PregnantBlock_newMonster();
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