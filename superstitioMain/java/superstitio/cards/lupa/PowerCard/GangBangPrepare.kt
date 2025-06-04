package superstitio.cards.lupa.PowerCard

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.card.InsideEjaculationTag
import superstitio.cardModifier.modifiers.card.OutsideEjaculationTag
import superstitio.cards.IsLupaCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.TempCard.GangBang
import superstitio.cards.lupa.LupaCard
import superstitio.orbs.orbgroup.SexMarkOrbGroup
import superstitio.orbs.orbgroup.SexMarkOrbGroup.SexMarkType
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.utils.setDescriptionArgs
@IsLupaCard
class GangBangPrepare : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.cardsToPreview = GangBang()
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(GangBangPreparePower(this.magicNumber))
        val sexMarkOrbGroup = superstitio.InBattleDataManager.getSexMarkOrbGroup()
        if (sexMarkOrbGroup != null)
        {
            sexMarkOrbGroup.changeScoreRate(sexMarkOrbGroup.scoreRate + magicNumber.toDouble() / 100)
            return
        }

        InBattleDataManager.Subscribe(
            SexMarkOrbGroup(
                AbstractDungeon.player.hb,
                magicNumber.toDouble() / 100
            )
        )
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    class GangBangPreparePower(scoreRateForShow: Int) : EasyBuildAbstractPowerForPowerCard(scoreRateForShow)
    {
        override fun onPlayCard(card: AbstractCard, m: AbstractMonster?)
        {
            if (card is FuckJob_Card && card is SuperstitioCard)
            {
                if (card.hasTag(InsideEjaculationTag.Companion.getInsideEjaculationTag()))
                {
                    addToBot_gainSexMark_Inside(card.name)
                    return
                }
                if (card.hasTag(OutsideEjaculationTag.Companion.getOutsideEjaculationTag()))
                {
                    addToBot_gainSexMark_Outside((card as SuperstitioCard).cardStrings.getEXTENDED_DESCRIPTION(0))
                }
            }
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(amount)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return GangBangPrepare()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(GangBangPrepare::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2

        //    private static final int COST_UPGRADED_NEW = 2;
        private const val MAGIC = 15
        private const val UPGRADE_MAGIC = 5

        fun addToBot_gainSexMark_Inside(sexName: String)
        {
            SexMarkOrbGroup.addToBot_GiveMarkToOrbGroup(sexName, SexMarkType.Inside)
        }

        fun addToBot_gainSexMark_Outside(sexName: String)
        {
            SexMarkOrbGroup.addToBot_GiveMarkToOrbGroup(sexName, SexMarkType.OutSide)
        }
    }
}

