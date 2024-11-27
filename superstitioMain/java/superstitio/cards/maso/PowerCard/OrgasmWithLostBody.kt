package superstitio.cards.maso.PowerCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect
import superstitio.DataManager
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.TempCard.FeelPhantomBody
import superstitio.cards.maso.MasoCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility

class OrgasmWithLostBody : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.cardsToPreview = FeelPhantomBody()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(OrgasmWithLostBodyPower().upgradeCardInThis(upgraded))
    }

    override fun upgradeAuto()
    {
        this.upgradeCardsToPreview()
    }

    class OrgasmWithLostBodyPower : EasyBuildAbstractPowerForPowerCard(-1)
    {
        private fun tryBecomeFeelPhantomBodyCard(card: AbstractCard)
        {
            if (CardUtility.canUseWithoutEnvironment(card)) return
            AutoDoneInstantAction.addToBotAbstract {
                AbstractDungeon.player.hand.removeCard(card)
                AbstractDungeon.effectList.add(PurgeCardEffect(card))
            }
            ActionUtility.addToBot_makeTempCardInBattle(
                FeelPhantomBody(card), ActionUtility.BattleCardPlace.Hand,
                powerCard.upgraded
            )
        }

        override fun onCardDraw(card: AbstractCard)
        {
            super.onCardDraw(card)
            if (ActionUtility.isNotInBattle) return
            tryBecomeFeelPhantomBodyCard(card)
        }

        override fun updateDescriptionArgs()
        {
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return OrgasmWithLostBody()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(OrgasmWithLostBody::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
    }
}

