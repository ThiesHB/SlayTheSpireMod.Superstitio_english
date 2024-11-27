package superstitio.cards.maso.SkillCard.cruelTorture

import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.common.LoseHPAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.CruelTortureTag
import superstitio.cards.maso.MasoCard
import superstitioapi.utils.setDescriptionArgs

//尖桩贯穿
class CruelTorture_Impale : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        CardModifierManager.addModifier(this, CruelTortureTag())
        initializeDescription()
    }

    override fun applyPowers()
    {
//        updateRawDescription();
        super.applyPowers()
        initializeDescription()
    }

    override fun initializeDescription()
    {
        updateRawDescription()
        super.initializeDescription()
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(
            if (this.magicNumber != 0)
                sumAllDelayHpLosePower() / this.magicNumber
            else 0
        )
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_drawCards(if (this.magicNumber != 0) sumAllDelayHpLosePower() / this.magicNumber else 0)
        addToBot(LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber))
        addToBot_drawCards()
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(CruelTorture_Impale::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        //    public static final int BURN_CARD_NUM = 2;
        private const val COST = 1
        private const val MAGIC = 5
        private const val UPGRADE_MAGIC = -2
    }
}
