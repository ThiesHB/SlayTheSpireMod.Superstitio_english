package superstitio.cards.general.SkillCard

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.PregnantBlock_newMonster
import superstitio.cards.general.GeneralCard
import superstitio.cards.general.TempCard.GiveBirth
import superstitio.monster.ChibiKindMonster
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility

//@AutoAdd.Ignore
class HaveBirthWith : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupBlock(BLOCK, UPGRADE_BLOCK, PregnantBlock_newMonster().removeAutoBind())
        this.cardsToPreview = GiveBirth()
        //this.exhaust = true;
    }

    private fun ForPlayer(player: AbstractPlayer)
    {
        addToBot_gainCustomBlock(
            PregnantBlock_newMonster(
                ChibiKindMonster(), ChibiKindMonster.MinionChibi(ChibiKindMonster())
            )
        )
    }

    private fun ForMonster(monster: AbstractMonster?)
    {
        addToBot_gainCustomBlock(PregnantBlock_newMonster(monster))
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val target = CardUtility.getSelfOrEnemyTarget(this, monster)
        if (target is AbstractPlayer)
            ForPlayer(AbstractDungeon.player)
        else
            ForMonster(target as AbstractMonster)
        ActionUtility.addToBot_makeTempCardInBattle(GiveBirth(), ActionUtility.BattleCardPlace.Discard, upgraded)
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(HaveBirthWith::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = SelfOrEnemyTargeting.SELF_OR_ENEMY

        private const val COST = 1
        private const val BLOCK = 10
        private const val UPGRADE_BLOCK = 3
    }
}
