package superstitio.cards.general.SkillCard.cardManipulation

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.NormalCard
import superstitio.cards.general.TempCard.SelfReference
import superstitioapi.actions.ChoseCardFromGridSelectWindowAction
import superstitioapi.cards.addRetainMod

//荣耀洞
class GloryHole : NormalCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    //
    //    private static final int COST_UPGRADED_NEW = 2;
    init
    {
        this.exhaust = true
    }

    //    private static AbstractCard getCard(AbstractCard card) {
    //        if (card instanceof GloryHole)
    //            return new SelfReference();
    //        return card.makeStatEquivalentCopy();
    //    }
    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        ChoseCardFromGridSelectWindowAction(AbstractDungeon.player.masterDeck) { card: AbstractCard ->
            addToBot(
                makeChoseCardCopy(card)
            )
        }
            .setWindowText(String.format(cardStrings.getEXTENDED_DESCRIPTION(0)))
            .setAnyNumber(true)
            .addToBot()

        //                .setRetainFilter(card -> !Objects.equals(card.cardID, GloryHole.ID))
    }

    override fun upgradeAuto()
    {
//        upgradeBaseCost(COST_UPGRADED_NEW);
        this.addRetainMod()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(GloryHole::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2


        private fun makeChoseCardCopy(card: AbstractCard): AbstractGameAction
        {
            if (card.cardID == ID)
            {
                val selfReference = SelfReference()
                if (card.upgraded) selfReference.upgrade()
                return MakeTempCardInHandAction(selfReference)
            }
            return MakeTempCardInHandAction(card.makeStatEquivalentCopy())
        }
    }
}
