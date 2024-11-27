package superstitio.cards.general.SkillCard.gainEnergy

import com.megacrit.cardcrawl.actions.AbstractGameAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitio.powers.Overdraft
import superstitio.powers.SexualHeat
import superstitioapi.actions.XCostAction

class ForceOrgasm : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
        this.exhaust = true
        //        ExhaustiveVariable.setBaseValue(this, 2);
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val magicNumber = this.magicNumber
        addToBot(XCostAction(this, AbstractGameAction.ActionType.ENERGY) { effect: Int ->
            SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, effect * magicNumber)
            addToBot_applyPower(Overdraft(AbstractDungeon.player, effect * ExhaustNum))
        })
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(ForceOrgasm::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = -1
        private const val MAGIC = 15
        private const val UPGRADE_MAGIC = 5
        private const val ExhaustNum = 1
    }
}
