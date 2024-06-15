package superstitio.cardModifier.modifiers.block;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitioapi.pet.Minion;
import superstitioapi.pet.PetManager;

public class PregnantBlock_newMonster extends PregnantBlock {
    public static final String ID = DataManager.MakeTextID(PregnantBlock_newMonster.class);

    private final Minion sealCreature;
    private final AbstractMonster father;

    public PregnantBlock_newMonster() {
        super(ID);
        this.sealCreature = null;
        this.father = null;
    }

    public PregnantBlock_newMonster(AbstractMonster father) {
        super(ID);
        AbstractMonster fatherCreature;
        fatherCreature = father;
        this.father = fatherCreature;
        this.sealCreature = null;
    }

    public PregnantBlock_newMonster(AbstractMonster father, Minion sealCreature) {
        super(ID);
        AbstractMonster fatherCreature;
        fatherCreature = father;
        this.father = fatherCreature;
        this.sealCreature = sealCreature;
    }

    @Override
    public AbstractBlockModifier makeCopy() {
        if (this.father == null)
            return new PregnantBlock_newMonster();
        if (this.sealCreature == null)
            return new PregnantBlock_newMonster(this.father);
        return new PregnantBlock_newMonster(this.father, this.sealCreature);
    }

    @Override
    public void removeUnNaturally(DamageInfo info, int remainingDamage) {
//        removeUnNatural.get();
    }

    @Override
    public void removeNaturally(int remainingDamage) {
//        removeNatural.get();
        if (father == null) return;
        if (sealCreature == null)
            PetManager.spawnMinion(father.getClass());
        else
            PetManager.spawnMonster(sealCreature);
    }
}