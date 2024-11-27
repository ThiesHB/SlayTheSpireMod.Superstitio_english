package superstitio.cards.general.SkillCard.cardManipulation

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.actions.ChoseCardFromGridSelectWindowAction

class BecomeTrash : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.exhaust = true
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        ChoseCardFromGridSelectWindowAction(
            AbstractDungeon.player.drawPile
        ) { card: AbstractCard? -> addToBot(ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile)) }
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0), this.magicNumber))
            .setAnyNumber(true)
            .setChoseAmount(this.magicNumber)
            .addToBot()
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(BecomeTrash::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val MAGIC = 4
        private const val UPGRADE_MAGIC = 2
    }
}
