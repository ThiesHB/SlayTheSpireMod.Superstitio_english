package superstitio.cards.general.TempCard

import basemod.AutoAdd
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.SPTT_Color
import superstitio.cards.SetCardColor
import superstitio.cards.general.AbstractTempCard
import superstitio.delayHpLose.RemoveDelayHpLoseBlock
import superstitioapi.utils.ActionUtility

@AutoAdd.Ignore
@SetCardColor(SPTT_Color.TempColor)
class SelfReference : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK, RemoveDelayHpLoseBlock())
        AutoplayField.autoplay[this] = true
        this.dontTriggerOnUseCard = true
        this.shuffleBackIntoDrawPile = true
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        this.dontTriggerOnUseCard = true
        this.addToBot_gainBlock()
    }

    override fun triggerOnExhaust()
    {
        ActionUtility.addToBot_makeTempCardInBattle(SelfReference(), ActionUtility.BattleCardPlace.Hand, upgraded)
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SelfReference::class.java)

        val CARD_TYPE: CardType = CardType.CURSE

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = -2
        private const val BLOCK = 5
        private const val UPGRADE_PLUS_BLOCK = 3
    }
}
