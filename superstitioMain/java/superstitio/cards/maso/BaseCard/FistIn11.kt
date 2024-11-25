package superstitio.cards.maso.BaseCard

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction
import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import superstitio.DataManager
import superstitio.cards.general.TempCard.SelfReference
import superstitio.cards.maso.MasoCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs
import java.util.function.Consumer

class FistIn : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base"), GoSomewhereElseAfterUse {
    init {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    private fun tryToUseCard(card: AbstractCard, target: AbstractCreature) {
        if (card.target == CardTarget.ENEMY && target is AbstractMonster) addToBot(
            NewQueueCardAction(
                card,
                target,
                false,
                true
            )
        )
        else if (card.target == SelfOrEnemyTargeting.SELF_OR_ENEMY) {
            addToBot(NewQueueCardAction(card, if (target is AbstractMonster) target else null, false, true))
        } else addToBot(NewQueueCardAction(card, true, false, true))
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(WAIT, DRAWCard)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        ChoseCardFromHandCardSelectScreen(Consumer { card: AbstractCard ->
            if (card is FistIn) {
                ActionUtility.addToBot_makeTempCardInBattle(
                    SelfReference(),
                    ActionUtility.BattleCardPlace.Hand,
                    card.upgraded
                )
                addToBot(ExhaustSpecificCardAction(card, AbstractDungeon.player.hand))
                return@Consumer
            }
            AutoDoneInstantAction.addToBotAbstract {
                val costSave = if (card.costForTurn >= 0) card.costForTurn
                else if (card.costForTurn == -1) EnergyPanel.getCurrentEnergy()
                else 0
                addToBot(LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, costSave * magicNumber))
                tryToUseCard(card, target)
            }
        })
            .setRetainFilter()
            .setWindowText(cardStrings.getEXTENDED_DESCRIPTION(0))
            .addToBot()
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup) {
        CardOrb_WaitCardTrigger(this, cardGroup, WAIT) { orb: CardOrb_CardTrigger, playedCard: AbstractCard ->
            orb.StartHitCreature(AbstractDungeon.player)
            addToBot_drawCards(DRAWCard)
            PowerUtility.BubbleMessage(orb.originCard.hb, false, cardStrings.getEXTENDED_DESCRIPTION(0))
        } //                .setDiscardOnEndOfTurn()
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1), DRAWCard)
            .addToBot_HangCard()
    }

    override fun upgradeAuto() {
    }

    companion object {
        val ID: String = DataManager.MakeTextID(FistIn::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.BASIC

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 0
        private const val MAGIC = 4
        private const val UPGRADE_MAGIC = -1
        private const val DRAWCard = 1

        private const val WAIT = 3
    }
}

