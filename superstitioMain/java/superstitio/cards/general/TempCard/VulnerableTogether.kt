package superstitio.cards.general.TempCard

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.VulnerablePower
import superstitio.DataManager
import superstitio.SPTT_Color
import superstitio.cards.SetCardColor
import superstitio.cards.general.AbstractTempCard
import superstitioapi.cards.addExhaustMod
import superstitioapi.cards.addRetainMod
import superstitioapi.utils.CreatureUtility
import java.util.*

@SetCardColor(SPTT_Color.TempColor)
class VulnerableTogether : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.addExhaustMod()
        this.addRetainMod()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(VulnerablePower(AbstractDungeon.player, this.magicNumber, false))
        Arrays.stream(CreatureUtility.getAllAliveMonsters()).forEach { creature: AbstractMonster? ->
            addToBot_applyPower(VulnerablePower(creature, this.magicNumber, false))
        }
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(VulnerableTogether::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.SPECIAL

        val CARD_TARGET: CardTarget = CardTarget.ALL
        private const val COST = 0
        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 1
    }
}
