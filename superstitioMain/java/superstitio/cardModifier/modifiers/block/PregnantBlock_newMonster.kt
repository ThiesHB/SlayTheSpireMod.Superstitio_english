package superstitio.cardModifier.modifiers.block

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.monster.ChibiKindMonster
import superstitioapi.pet.Minion
import superstitioapi.pet.PetManager

class PregnantBlock_newMonster : PregnantBlock
{
    private val sealCreature: Minion?
    private val father: AbstractMonster?

    constructor() : super(ID)
    {
        this.sealCreature = null
        this.father = null
    }

    constructor(father: AbstractMonster?) : super(ID)
    {
        val fatherCreature = father
        this.father = fatherCreature
        this.sealCreature = null
    }

    constructor(father: AbstractMonster?, sealCreature: Minion?) : super(ID)
    {
        val fatherCreature = father
        this.father = fatherCreature
        this.sealCreature = sealCreature
    }

    override fun makeCopy(): AbstractBlockModifier
    {
        if (this.father == null) return PregnantBlock_newMonster()
        if (this.sealCreature == null) return PregnantBlock_newMonster(this.father)
        return PregnantBlock_newMonster(this.father, this.sealCreature)
    }

    override fun getDescription(): String?
    {
        if (father == null)
            return super.getDescription()
        if (sealCreature == null) return super.getDescription() + blockStrings!!.getEXTENDED_DESCRIPTION(0) + father.name
        if (sealCreature is ChibiKindMonster.MinionChibi) return super.getDescription() + blockStrings!!.getEXTENDED_DESCRIPTION(
            0
        ) + blockStrings.getEXTENDED_DESCRIPTION(1)
        return super.getDescription()
    }

    //    @Override
    //    public int removeUnNaturally(DamageInfo info, int remainingDamage) {
    //        return super.removeUnNaturally(info, remainingDamage * 2);
    //    }
    override fun removeNaturally(remainingDamage: Int): Int
    {
        if (father == null) return super.removeNaturally(remainingDamage)
        if (sealCreature == null) PetManager.spawnMinion(father.javaClass)
        else PetManager.spawnMonster(sealCreature)
        return super.removeNaturally(remainingDamage)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(PregnantBlock_newMonster::class.java)
    }
}