package superstitio.cards.general.TempCard

import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.DataManager.SPTT_DATA
import superstitio.cardModifier.modifiers.block.DrySemenBlock
import superstitio.cardModifier.modifiers.damage.SexDamage
import superstitio.cards.general.AbstractTempCard
import superstitioapi.SuperstitioApiSetup
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.hangUpCard.CardOrb_AtEndOfTurnEachTime
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.CardUtility
import superstitioapi.utils.CreatureUtility
import superstitioapi.utils.setDescriptionArgs

class GangBang @JvmOverloads constructor(
    attackAmount: Int = DAMAGE,
    blockAmount: Int = BLOCK,
    score: Int = 1,
    scoreRate: Double = 0.15
) : AbstractTempCard(
    ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET
), GoSomewhereElseAfterUse
{
    /**
     * @param score 1-5
     */
    init
    {
        this.originalName = cardStrings.getEXTENDED_DESCRIPTION(score) + cardStrings.getNAME()
        this.name = this.originalName
        this.setupDamage((attackAmount * (1 + (score - 1) * scoreRate)).toInt(), SexDamage())
        this.setupBlock((blockAmount * (1 + (score - 1) * scoreRate)).toInt(), DrySemenBlock())
        this.glowColor = Color.WHITE.cpy()
        if (!ActionUtility.isNotInBattle) this.beginGlowing()
        this.exhaust = true
        this.isMultiDamage = true
        //        this.purgeOnUse = true;
        AutoplayField.autoplay[this] = true
        this.dontTriggerOnUseCard = true
        this.setBackgroundTexture(
            SPTT_DATA.BG_ATTACK_512_SEMEN,
            SPTT_DATA.BG_ATTACK_SEMEN
        )
        this.initializeDescription()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        this.dontTriggerOnUseCard = true
    }

    override fun canUpgrade(): Boolean
    {
        return false
    }

    override fun upgradeAuto()
    {
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(TURN_TAKE)
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        val self = this
        CardOrb_AtEndOfTurnEachTime(
            this,
            cardGroup,
            CardUtility.CostSmart(TURN_TAKE)
        ) { cardOrbAtEndOfTurn: CardOrb_AtEndOfTurnEachTime ->
            cardOrbAtEndOfTurn.StartHitCreature(CreatureUtility.getRandomMonsterWithoutRngSafe())
            self.addToBot_gainCustomBlock(self.block, DrySemenBlock())
            self.addToBot_dealDamageToAllEnemies(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
            addToBot(WaitAction(1.0f))
        }
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(0))
            .addToBot_HangCard()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(GangBang::class.java)

        val CARD_TYPE: CardType = CardType.STATUS

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.ALL_ENEMY

        private const val COST = -2
        private const val DAMAGE = 3
        private const val BLOCK = 4
        private const val TURN_TAKE = 1
    }
}
