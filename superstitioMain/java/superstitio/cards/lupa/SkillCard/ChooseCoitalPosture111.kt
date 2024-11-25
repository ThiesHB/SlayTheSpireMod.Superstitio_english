package superstitio.cards.lupa.SkillCard

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.CardLibrary
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.lupa.LupaCard
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.ListUtility
import java.util.stream.Collectors

//随机生成一张Fuck/Job卡
class ChooseCoitalPosture : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET) {
    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        val card = randomFuckJobCard.makeCopy()
        //        if (!CardModifierManager.hasModifier(card, ExhaustMod.ID))
        card.setCostForTurn(0)
        CardModifierManager.addModifier(card, ExhaustMod())
        ActionUtility.addToBot_makeTempCardInBattle(card, ActionUtility.BattleCardPlace.Hand, upgraded)
    }

    override fun upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW)
    }

    companion object {
        val ID: String = DataManager.MakeTextID(ChooseCoitalPosture::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 1
        private const val COST_UPGRADED_NEW = 0


        val randomFuckJobCard: AbstractCard
            get() = ListUtility.getRandomFromList(allFuckJobCard, AbstractDungeon.cardRandomRng)

        val allFuckJobCard: List<AbstractCard>
            get() = CardLibrary.cards.values.stream()
                .filter { card: AbstractCard? -> card is FuckJob_Card && CardOwnerPlayerManager.isLupaCard(card) }
                .collect(Collectors.toList())
    }
}

