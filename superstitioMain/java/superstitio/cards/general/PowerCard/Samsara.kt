package superstitio.cards.general.PowerCard

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.status.Wound
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.EquilibriumPower
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.cards.general.PowerCard.Samsara.SamsaraPower
import superstitio.powers.AbstractSuperstitioPower
import superstitioapi.utils.ActionUtility

class Samsara : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.cardsToPreview = Wound()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
//        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), 8, true, true));
        ActionUtility.addToBot_makeTempCardInBattle(Wound(), ActionUtility.BattleCardPlace.DrawPile, this.magicNumber)
        addToBot_applyPower(EquilibriumPower(AbstractDungeon.player, 99))
        addToBot_applyPower(SamsaraPower(AbstractDungeon.player))
    }

    override fun upgradeAuto()
    {
    }

    class SamsaraPower(owner: AbstractCreature) : AbstractSuperstitioPower(POWER_ID, owner, -1)
    {
        override fun onPlayCard(card: AbstractCard, m: AbstractMonster?)
        {
            this.flash()
            this.addToBot(DrawCardAction(1))
        }

        override fun updateDescriptionArgs()
        {
        }

        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(SamsaraPower::class.java)
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Samsara::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 3

        private const val MAGIC = 8
        private const val UPGRADE_MAGIC = -2
    }
}

