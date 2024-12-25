package superstitio.cards.general.TempCard

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction
import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.AbstractTempCard
import superstitioapi.cards.addExhaustMod
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CreatureUtility

class FeelPhantomBody @JvmOverloads constructor(private val sealCard: AbstractCard? = null) :
    AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.cardsToPreview = sealCard
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.addExhaustMod()
    }

    override fun makeCopy(): AbstractCard
    {
        if (sealCard != null) return FeelPhantomBody(sealCard)
        return super.makeCopy()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_drawCards()
        addToBot(LoseHPAction(CreatureUtility.getRandomMonsterSafe(), AbstractDungeon.player, this.magicNumber))
    }

    override fun triggerOnEndOfPlayerTurn()
    {
        super.triggerOnEndOfPlayerTurn()
        addToTop(ExhaustSpecificCardAction(this, AbstractDungeon.player.hand))
        if (sealCard == null) return
        ActionUtility.addToTop_makeTempCardInBattle(sealCard, ActionUtility.BattleCardPlace.Hand, 1, sealCard.upgraded)
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(FeelPhantomBody::class.java)

        val CARD_TYPE: CardType = CardType.STATUS

        val CARD_RARITY: CardRarity = CardRarity.SPECIAL

        val CARD_TARGET: CardTarget = CardTarget.ALL_ENEMY

        private const val COST = 0
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1
    }
}
