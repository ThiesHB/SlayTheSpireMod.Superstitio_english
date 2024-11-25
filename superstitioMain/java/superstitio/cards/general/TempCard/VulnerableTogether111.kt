package superstitio.cards.general.TempCard

import basemod.cardmods.ExhaustMod
import basemod.cardmods.RetainMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.VulnerablePower
import superstitio.DataManager
import superstitio.cards.general.AbstractTempCard
import superstitioapi.utils.CreatureUtility
import java.util.*

class VulnerableTogether : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        CardModifierManager.addModifier(this, ExhaustMod())
        CardModifierManager.addModifier(this, RetainMod())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_applyPower(VulnerablePower(AbstractDungeon.player, this.magicNumber, false))
        Arrays.stream(CreatureUtility.getAllAliveMonsters()).forEach { creature: AbstractMonster? ->
            addToBot_applyPower(VulnerablePower(creature, this.magicNumber, false))
        }
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(VulnerableTogether::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.SPECIAL

        val CARD_TARGET: CardTarget = CardTarget.ALL
        private const val COST = 0
        private const val MAGIC = 1
        private const val UPGRADE_MAGIC = 1
    }
}
