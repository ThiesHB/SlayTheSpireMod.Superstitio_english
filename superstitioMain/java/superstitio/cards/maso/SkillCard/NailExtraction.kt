package superstitio.cards.maso.SkillCard

import basemod.cardmods.EtherealMod
import basemod.cardmods.ExhaustMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.InBattleDataManager
import superstitio.cards.maso.MasoCard
import superstitioapi.cards.DamageActionMaker
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.setDescriptionArgs

//拔指甲/趾甲
class NailExtraction @JvmOverloads constructor(isPreview: Boolean = false) :
    MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        if (!isPreview) this.cardsToPreview = NailExtraction(true)
        this.setupMagicNumber(MAGIC)
        CardModifierManager.addModifier(this, ExhaustMod())
        CardModifierManager.addModifier(this, EtherealMod())
    }

    override fun applyPowers()
    {
        super.applyPowers()
        initializeDescription()
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(DAMAGE_TO_SELF, DRAW_CARD, COPY_SELF, MAX_IN_TURN)
    }

    override fun initializeDescription()
    {
        if (!ActionUtility.isNotInBattle)
        {
            this.baseMagicNumber = InBattleDataManager.NailExtractionPlayedInTurn
            this.magicNumber = this.baseMagicNumber
        }
        super.initializeDescription()
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        val damageActionMaker = DamageActionMaker.maker(DAMAGE_TO_SELF, AbstractDungeon.player)
            .setEffect(AttackEffect.NONE)
        if (!upgraded) damageActionMaker.setDamageType(DamageType.HP_LOSS).addToBot()
        else damageActionMaker.setEffect(AttackEffect.BLUNT_LIGHT).setDamageType(DamageType.NORMAL).addToBot()
        addToBot_drawCards(DRAW_CARD)
        ActionUtility.addToBot_makeTempCardInBattle(
            NailExtraction(),
            ActionUtility.BattleCardPlace.DrawPile,
            COPY_SELF,
            this.upgraded
        )
        InBattleDataManager.NailExtractionPlayedInTurn++
    }

    override fun canUse(p: AbstractPlayer?, m: AbstractMonster?): Boolean
    {
        if (InBattleDataManager.NailExtractionPlayedInTurn >= 20) return false
        return super.canUse(p, m)
    }

    override fun upgradeAuto()
    {
        upgradeCardsToPreview()
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(NailExtraction::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0
        private const val DAMAGE = 1
        private const val MAGIC = 0
        private const val DAMAGE_TO_SELF = 2

        //    private static final int UPGRADE_MAGIC = 1;
        private const val DRAW_CARD = 2
        private const val COPY_SELF = 2
        private const val MAX_IN_TURN = 20
    }
}
