package superstitio.cards.maso.AttackCard

import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.WeakPower
import superstitio.DataManager
import superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.TempCard.Fuck_Ear
import superstitio.cards.maso.MasoCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_CardTrigger
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility

class Fuck_Eye(blank: Boolean) : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card,
    GoSomewhereElseAfterUse, IsNotLupaCard {
    constructor() : this(false) {
        this.cardsToPreview = Fuck_Ear(false)
    }

    init {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
        //        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC)
        CardModifierManager.addModifier(this, ExhaustMod())
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?) {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
        addToBot_dealDamage(AbstractDungeon.player, AttackEffect.BLUNT_LIGHT)
        addToBot_dealDamage(AbstractDungeon.player, AttackEffect.BLUNT_LIGHT)
        addToBot_applyPower(WeakPower(AbstractDungeon.player, 1, false))
    }

    override fun upgradeAuto() {
        upgradeCardsToPreview()
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup) {
        CardOrb_WaitCardTrigger(this, cardGroup, CardUtility.CostSmart(this.magicNumber)) { orb: CardOrb_CardTrigger, card: AbstractCard? ->
            orb.StartHitCreature(AbstractDungeon.player)
            //            addToBot_gainCustomBlock(new RemoveDelayHpLoseBlock());
            ActionUtility.addToBot_makeTempCardInBattle(Fuck_Ear(), ActionUtility.BattleCardPlace.Hand, this.upgraded)
        }
            .setTargetType(CardTarget.SELF) //                .setDesc(this.rawDescription)
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(0))
            .addToBot_HangCard()
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Fuck_Eye::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF_AND_ENEMY

        private const val COST = 1
        private const val DAMAGE = 4
        private const val UPGRADE_DAMAGE = 1

        //    private static final int BLOCK = 8;
        //    private static final int UPGRADE_BLOCK = 2;
        private const val MAGIC = 3
    }
}
