package superstitio.cards.maso.SkillCard

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.damage.SexDamage
import superstitio.cards.SuperstitioCard
import superstitio.cards.maso.MasoCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.DamageActionMaker
import superstitioapi.cards.addEtherealMod
import superstitioapi.utils.CardUtility
import superstitioapi.utils.CreatureUtility
import superstitioapi.utils.setDescriptionArgs

class HumanCentipede : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        DamageModifierManager.addModifier(this, SexDamage())
        this.addEtherealMod()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        addToBot_applyPower(HumanCentipedePower(target, this.magicNumber))
    }

    override fun upgradeAuto()
    {
    }

    class HumanCentipedePower(owner: AbstractCreature, amount: Int) :
        EasyBuildAbstractPowerForPowerCard(owner = owner, amount = amount)
    {
        override fun getDescriptionStrings(): String
        {
            return powerCard.cardStrings.getEXTENDED_DESCRIPTION(0)
        }

        override fun onAttacked(info: DamageInfo, damageAmount: Int): Int
        {
            if (damageAmount > 0 && info.type == DamageType.NORMAL)
            {
                val map: MutableMap<AbstractCreature, Int> = HashMap()
                for (monster in CreatureUtility.getAllAliveMonsters())
                {
                    map[monster] = amount
                }
                map[AbstractDungeon.player] = amount
                DamageActionMaker.makeDamages(map)
                    .setSource(this.owner)
                    .setDamageType(DamageType.THORNS)
                    .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                    .setDamageModifier(this, SexDamage())
                    .addToBot()
            }

            return super.onAttacked(info, damageAmount)
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(this.amount)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return HumanCentipede()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(HumanCentipede::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 1
        private const val MAGIC = 8
        private const val UPGRADE_MAGIC = 3
    }
}
