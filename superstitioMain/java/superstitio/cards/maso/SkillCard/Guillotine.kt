package superstitio.cards.maso.SkillCard

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.cards.IsMasoCard
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.maso.MasoCard
import superstitio.orbs.Card_Orb_OnOrgasm
import superstitio.orbs.Card_Orb_OnOrgasm_WaitTime
import superstitio.powers.EasyBuildAbstractPowerForPowerCard
import superstitio.powers.sexualHeatNeedModifier.SexualHeatNeedModifier
import superstitioapi.cards.DamageActionMaker
import superstitioapi.cards.patch.GoSomewhereElseAfterUse
import superstitioapi.utils.CostSmart
import superstitioapi.utils.addToBot_removeSelf
import superstitioapi.utils.setDescriptionArgs

@IsMasoCard
class Guillotine : MasoCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card, GoSomewhereElseAfterUse
{
    init
    {
        FuckJob_Card.initFuckJobCard(this)
        //        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
//        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(GuillotinePower())
    }

    override fun upgradeAuto()
    {
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(HeatNeedAdd, DAMAGE_PERCENT)
    }

    override fun afterInterruptMoveToCardGroup(cardGroup: CardGroup)
    {
        Card_Orb_OnOrgasm_WaitTime(this, cardGroup, CostSmart(this.magicNumber)) { orb: Card_Orb_OnOrgasm ->
            orb.StartHitCreature(AbstractDungeon.player)
            DamageActionMaker.maker(99999, AbstractDungeon.player)
                .setDamageType(CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose)
                .setEffect(AttackEffect.BLUNT_HEAVY)
                .addToBot()
        }
            .setDiscardOnEndOfTurn()
            .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
            .setTargetType(CardTarget.SELF)
            .addToBot_HangCard()
    }

    class GuillotinePower : EasyBuildAbstractPowerForPowerCard(-1), SexualHeatNeedModifier, NonStackablePower
    {
        override fun getDescriptionStrings(): String
        {
            return powerCard.cardStrings.getEXTENDED_DESCRIPTION(0)
        }

        override fun reduceSexualHeatNeeded(): Int
        {
            return -HeatNeedAdd
        }

        override fun atDamageGive(damage: Float, type: DamageType): Float
        {
            return super.atDamageGive(damage, type) * (1 + DAMAGE_PERCENT.toFloat() / 100)
        }

        override fun atEndOfTurn(isPlayer: Boolean)
        {
            addToBot_removeSelf()
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(HeatNeedAdd, DAMAGE_PERCENT)
        }

        override fun makePowerCard(): SuperstitioCard
        {
            return Guillotine()
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Guillotine::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 0

        //    private static final int DAMAGE = 4;
        //    private static final int UPGRADE_DAMAGE = 1;
        //    private static final int BLOCK = 8;
        //    private static final int UPGRADE_BLOCK = 2;
        private const val DAMAGE_PERCENT = 50
        private const val HeatNeedAdd = 10
        private const val MAGIC = 2
        private const val UPGRADE_MAGIC = 1
    }
}
