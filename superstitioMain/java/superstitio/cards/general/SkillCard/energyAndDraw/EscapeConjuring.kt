package superstitio.cards.general.SkillCard.energyAndDraw

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction
import com.megacrit.cardcrawl.actions.common.GainEnergyAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.powers.SexualHeat

class EscapeConjuring : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    init {
        CardModifierManager.addModifier(this, ExhaustMod())
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        val discardCard = AbstractDungeon.player.hand.group.stream()
            .filter { card: AbstractCard -> card.costForTurn != 0 && !card.freeToPlay() }.count().toInt()
        val statusCardNum = AbstractDungeon.player.hand.group.stream()
            .filter { card: AbstractCard -> card.type == CardType.STATUS || card.type == CardType.CURSE }.count()
            .toInt()
        val deBuffNum = AbstractDungeon.player.powers.stream()
            .filter { power: AbstractPower -> power.type == PowerType.DEBUFF }.count().toInt()

        SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, deBuffNum * this.magicNumber)

        //        addToBot(new GainEnergyAction(statusCardNum));
        AbstractDungeon.player.hand.group.stream()
            .filter { card: AbstractCard -> card.type == CardType.STATUS || card.type == CardType.CURSE }
            .map(::DiscardSpecificCardAction)
            .forEach(this::addToBot)
        addToBot(GainEnergyAction(statusCardNum))
        addToBot_drawCards(statusCardNum)
    }

    override fun upgradeAuto() {
        CardModifierManager.removeModifiersById(this, ExhaustMod.ID, false)
    }

    companion object {
        val ID: String = DataManager.MakeTextID(EscapeConjuring::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
    }
}
