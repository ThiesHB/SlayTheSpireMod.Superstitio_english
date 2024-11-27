package superstitio.cards.lupa.AttackCard

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.cards.SuperstitioCard
import superstitio.cards.lupa.LupaCard
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility

class SemenLubricate : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    private var inPlayingCard = false

    init
    {
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        this.setupMagicNumber(MAGIC)
    }

    fun continuePlayCard()
    {
        if (!inPlayingCard) return
        if (!hasEnoughSemen(this.magicNumber))
        {
            inPlayingCard = false
            return
        }
        if (ActionUtility.isNotInBattle)
        {
            inPlayingCard = false
            return
        }
        val attackCard = AbstractDungeon.player.drawPile.group.stream()
            .filter { card: AbstractCard -> card.type == CardType.ATTACK }
            .filter { card: AbstractCard? -> card !is SemenLubricate } //                .filter(card -> card != notCard)
            //                .filter(card -> CardUtility.canUseWithoutEnvironment())
            .findFirst()
        if (!attackCard.isPresent)
        {
            inPlayingCard = false
            return
        }
        addToBot_useSemenAndAutoRemove(this.magicNumber)
        AutoDoneInstantAction.addToBotAbstract { AbstractDungeon.player.drawPile.group.remove(attackCard.get()) }
        addToBot_applyPower(ContinuePlayCardPower(this))
        addToBot(NewQueueCardAction(attackCard.get(), true, false, true))
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_dealDamage(monster)
        inPlayingCard = true
        AutoDoneInstantAction.addToBotAbstract(this::continuePlayCard)
    }

    override fun canUse(p: AbstractPlayer?, m: AbstractMonster?): Boolean
    {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber)
    }

    override fun upgradeAuto()
    {
    }

    @NoNeedImg
    private class ContinuePlayCardPower(card: AbstractCard?) : EasyBuildAbstractPowerForPowerCard(-1), InvisiblePower
    {
        private var semenLubricate: SemenLubricate? = null

        init
        {
            semenLubricate = if (card is SemenLubricate) card
            else null
        }

        override fun onAfterCardPlayed(usedCard: AbstractCard)
        {
            if (owner !is AbstractPlayer) return
            if (semenLubricate != null && semenLubricate!!.inPlayingCard)
            {
                AutoDoneInstantAction.addToBotAbstract(semenLubricate!!::continuePlayCard)
                return
            }
            addToBot_removeSpecificPower(this)
        }

        override fun updateDescriptionArgs()
        {
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return SemenLubricate()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SemenLubricate::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 3
        private const val DAMAGE = 12
        private const val UPGRADE_DAMAGE = 4

        private const val MAGIC = 3
    }
}
