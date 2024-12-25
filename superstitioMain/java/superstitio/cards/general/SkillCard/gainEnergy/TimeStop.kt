package superstitio.cards.general.SkillCard.gainEnergy

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower
import com.evacipated.cardcrawl.modthespire.lib.*
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.powers.NoDrawPower
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.MethodCall
import superstitio.DataManager
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.cards.general.GeneralCard
import superstitio.cards.general.SkillCard.gainEnergy.TimeStop.TimeStopPower
import superstitio.powers.AbstractSuperstitioPower
import superstitio.powers.DelaySexualHeat
import superstitio.powers.SexualHeat
import superstitioapi.utils.addToBot_AutoRemoveOne
import superstitioapi.utils.setDescriptionArgs

class TimeStop : GeneralCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        setupMagicNumber(MAGIC)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        addToBot_applyPower(TimeStopPower(AbstractDungeon.player, this.magicNumber))
        addToBot_applyPower(NoDrawPower(player))
    }

    override fun upgradeAuto()
    {
        this.upgradeBaseCost(COST_UPGRADED_NEW)
    }

    @NoNeedImg
    class TimeStopPower(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount),
        BetterOnApplyPowerPower
    {
        init
        {
            this.loadRegion("time")
        }

        override fun updateDescriptionArgs()
        {
            setDescriptionArgs(amount, sexualReturnRate)
        }


        override fun atEndOfRound()
        {
            super.atEndOfRound()
            //if (!isPlayer) return;
            addToBot_AutoRemoveOne(this)
        }

        override fun betterOnApplyPower(
            power: AbstractPower,
            creature: AbstractCreature,
            creature1: AbstractCreature
        ): Boolean
        {
            if (power is SexualHeat && power.amount > 0)
            {
                this.flash()
                this.addToBot(
                    ApplyPowerAction(
                        this.owner,
                        this.owner,
                        DelaySexualHeat(this.owner, power.amount * sexualReturnRate)
                    )
                )
                return false
            }
            return true
        }


        companion object
        {
            val POWER_ID: String = DataManager.MakeTextID(TimeStopPower::class.java)
            const val sexualReturnRate: Int = 2

            @SpirePatch2(clz = AbstractCreature::class, method = "applyEndOfTurnTriggers")
            object TimeStopEndTurnPatch
            {
                @SpirePrefixPatch
                @JvmStatic
                fun Prefix(__instance: AbstractCreature): SpireReturn<Void>
                {
                    val timeStopPower =
                        __instance.powers.stream().filter { power: AbstractPower? -> power is TimeStopPower }
                            .map { power: AbstractPower -> power as TimeStopPower }.findAny()
                    if (timeStopPower.isPresent)
                    {
                        val noDraw = __instance.getPower(NoDrawPower.POWER_ID)
                        if (noDraw != null) timeStopPower.get().addToBot_AutoRemoveOne(noDraw)
                        return SpireReturn.Return()
                    }
                    return SpireReturn.Continue()
                }
            }

            @SpirePatch2(clz = MonsterGroup::class, method = "applyEndOfTurnPowers")
            object TimeStopEndRoundPatch
            {
                @SpireInstrumentPatch
                @JvmStatic
                fun Instrument(): ExprEditor
                {
                    return object : ExprEditor()
                    {
                        @Throws(CannotCompileException::class)
                        override fun edit(m: MethodCall)
                        {
                            if (m.className == AbstractPower::class.qualifiedName && m.methodName == "atEndOfRound")
                            {
                                m.replace(
                                    String.format(
                                        "if (!%s.shouldEscapeEndOfRound($0)){\$_ = \$proceed($$);}",
                                        Companion::class.qualifiedName
                                    )
                                )
                            }
                        }
                    }
                }
            }

            @JvmStatic
            fun shouldEscapeEndOfRound(power: AbstractPower?): Boolean
            {
                if (power == null) return false
                if (power is TimeStopPower) return false
                if (power.owner == null) return false
                if (power.owner.powers == null) return false
                //                if (power instanceof NoDrawPower) return false;
                val timeStopPower =
                    power.owner.powers.stream().filter { power1: AbstractPower? -> power1 is TimeStopPower }
                        .findAny()
                return timeStopPower.isPresent
            }
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(TimeStop::class.java)

        val CARD_TYPE: CardType = CardType.SKILL

        val CARD_RARITY: CardRarity = CardRarity.RARE

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = 2
        private const val MAGIC = 1
        private const val COST_UPGRADED_NEW = 1
    }
}
