package superstitio.cards.maso.SkillCard.cruelTorture

import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.status.Burn
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.DexterityPower
import com.megacrit.cardcrawl.powers.LoseDexterityPower
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.CruelTortureTag
import superstitio.cards.IsMasoCard
import superstitio.cards.maso.MasoCard
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_AtStartOfTurnEachTime
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CostSmart
import superstitioapi.utils.setDescriptionArgs

//炮烙
@IsMasoCard
class CruelTorture_HotPillar : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), GoSomewhereElseAfterUse
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        CardModifierManager.addModifier(this, CruelTortureTag())
        this.cardsToPreview = Burn()
    }

    private fun addTempDexterity(self: CruelTorture_HotPillar)
    {
        self.addToBot_applyPower(DexterityPower(AbstractDungeon.player, this.magicNumber))
        self.addToBot_applyPower(LoseDexterityPower(AbstractDungeon.player, this.magicNumber))
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(BURN_CARD_NUM)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        ActionUtility.addToBot_makeTempCardInBattle(Burn(), ActionUtility.BattleCardPlace.Discard, BURN_CARD_NUM)
        addTempDexterity(this)
    }

    override fun upgradeAuto()
    {
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        val self = this
        CardOrb_AtStartOfTurnEachTime(
            this,
            cardGroup,
            CostSmart(CostSmart.CostType.NaN)
        ) { cardOrbAtStartOfTurn: CardOrb_AtStartOfTurnEachTime ->
            cardOrbAtStartOfTurn.StartHitCreature(AbstractDungeon.player)
            addTempDexterity(self)
        }
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(0))
            .addToBot_HangCard()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(CruelTorture_HotPillar::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF
        const val BURN_CARD_NUM: Int = 1
        private const val COST = 1
        private const val MAGIC = 3
        private const val UPGRADE_MAGIC = 1
//        private const val BIG_NUMBER_FOR_TURN = 99
    }
}
