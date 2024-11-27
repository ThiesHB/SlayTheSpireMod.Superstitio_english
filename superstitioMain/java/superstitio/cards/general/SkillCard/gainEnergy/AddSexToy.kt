package superstitio.cards.general.SkillCard.gainEnergy

import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.cards.general.TempCard.SexToy
import superstitioapi.utils.ActionUtility

class AddSexToy : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    //    private static final int UPGRADE_MAGIC = 1;
    init
    {
        this.setupMagicNumber(MAGIC)
        this.exhaust = true
        this.cardsToPreview = SexToy()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        ActionUtility.addToBot_makeTempCardInBattle(
            SexToy(),
            ActionUtility.BattleCardPlace.Hand,
            this.magicNumber,
            this.upgraded
        )
    }

    override fun upgradeAuto()
    {
//        upgradeMagicNumber(UPGRADE_MAGIC);
        upgradeCardsToPreview()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(AddSexToy::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val MAGIC = 2
    }
}
