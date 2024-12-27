package superstitio.cards.general.TempCard

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.random.Random
import superstitio.DataManager
import superstitio.cards.general.AbstractTempCard
import superstitioapi.utils.CardUtility
import superstitioapi.utils.ListUtility

class SexToy : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        val finalTarget = target
        repeat(this.magicNumber) {
            addToBot_applyPower(
                superstitio.powers.SexToy(
                    finalTarget,
                    1,
                    getRandomSexToyName()
                )
            )
        }
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SexToy::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.SPECIAL

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY
        fun getSexToyNames(): Array<String> = getCardStringsWithSFWAndFlavor(ID)
            .getEXTENDED_DESCRIPTION(0).split(",".toRegex()).dropLastWhile(String::isEmpty).toTypedArray()

        private const val COST = 0
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1

        fun getRandomSexToyName(): String =
            ListUtility.getRandomFromList(getSexToyNames(), AbstractDungeon.cardRandomRng)

        fun getRandomSexToyNameWithoutRng(): String
        {
            val random = Random(System.nanoTime())
            return ListUtility.getRandomFromList(getSexToyNames(), random)
        }
    }
}
