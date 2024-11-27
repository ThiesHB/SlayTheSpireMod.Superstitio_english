package superstitio.powers

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower
import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction
import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import superstitio.DataManager
import superstitio.InBattleDataManager
import superstitio.Logger
import superstitio.SuperstitioConfig
import superstitio.SuperstitioImg.NoNeedImg
import superstitio.powers.patchAndInterface.interfaces.orgasm.*
import superstitio.powers.sexualHeatNeedModifier.RefractoryPeriod
import superstitio.powers.sexualHeatNeedModifier.SexualHeatNeedModifier
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.powers.barIndepend.*
import superstitioapi.powers.interfaces.OnPostApplyThisPower
import superstitioapi.shader.ShaderUtility
import superstitioapi.shader.heart.HeartStreamShader.RenderHeartStream
import superstitioapi.utils.ActionUtility.VoidSupplier
import superstitioapi.utils.PowerUtility
import superstitioapi.utils.setDescriptionArgs
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.math.max

@NoNeedImg
class SexualHeat protected constructor(owner: AbstractCreature, private var heatAmount: Int) : AbstractSuperstitioPower(
    POWER_ID, owner, -1, if (owner.isPlayer) PowerType.BUFF else PowerType.DEBUFF, false
), OnPostApplyThisPower<SexualHeat>, HasBarRenderOnCreature_Power,
//    InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, InvisiblePower_InvisibleApplyPowerEffect,
//    InvisiblePower_InvisibleRemovePowerEffect,
    OnOrgasm_onSuccessfullyPreventOrgasm, OnOrgasm_onOrgasm,
    OnOrgasm_onEndOrgasm, OnOrgasm_onSquirt, OnOrgasm_onContinuallyOrgasm
{
    val barOrgasmShadowColor: Color = Color.YELLOW.cpy()
    private val barShadowColorOrigin: Color

    init
    {
        barOrgasmShadowColor.a = 0.6f
        this.barShadowColorOrigin = this.setupBarShadowColor()!!
    }

    val isInOrgasm: Boolean
        get() = Orgasm.isInOrgasm(owner)

    fun CheckOrgasm()
    {
        OnOrgasm.AllOnOrgasm(owner).forEach { power: OnOrgasm ->
            power.onCheckOrgasm(this)
        }
        if (getOrgasmTimesInTurn() >= (this.heatAmount / this.heatRequired)) return
        this.StartOrgasm()
    }

    protected fun addSexualHeat(heatAmount: Int)
    {
        this.heatAmount += heatAmount
        if (this.heatAmount < 0) this.heatAmount = 0
        CheckOrgasm()
        updateDescription()
        AbstractDungeon.onModifyPower()
    }

    protected fun reduceSexualHeat(heatAmount: Int)
    {
        this.heatAmount -= heatAmount
        if (this.heatAmount < 0) this.heatAmount = 0
        CheckEndOrgasm()
        updateDescription()
        AbstractDungeon.onModifyPower()
    }

    private fun StartOrgasm()
    {
        if (OnOrgasm.AllOnOrgasm(owner).any { power: OnOrgasm ->
                power.preventOrgasm(this)
            })
        {
            OnOrgasm.AllOnOrgasm(owner).forEach { power: OnOrgasm ->
                power.onSuccessfullyPreventOrgasm(this)
            }
            return
        }
        Orgasm()
        OnOrgasm.AllOnOrgasm(owner).forEach { power: OnOrgasm ->
            power.onOrgasm(this)
        }
        if (IsContinueOrgasm())
            OnOrgasm.AllOnOrgasm(owner)
                .forEach { power: OnOrgasm ->
                    power.onContinuallyOrgasm(this)
                }
        CheckOrgasm()
    }

    private fun IsContinueOrgasm(): Boolean
    {
        return getOrgasmTimesInTurn() > 1
    }

    private fun Orgasm()
    {
        AddOrgasmTime()
        if (!owner.isPlayer)
        {
            this.addToBot(
                ApplyPowerAction(
                    this.owner, this.owner, StunMonsterPower(
                        owner as AbstractMonster
                    )
                )
            )
            //            addToBot_reduceSexualHeat(this.owner, this.heatAmount);
            this.reduceSexualHeat(this.heatAmount)
            ForceEndOrgasm()
            addToBot_applyPower(RefractoryPeriod(this.owner, HEAT_REQUIREDOrigin))
            return
        }

        OnOrgasm.AllOnOrgasm(owner).forEach { power: OnOrgasm ->
            power.onOrgasmFirst(this)
        }

        Orgasm.startOrgasm(owner)
    }

    private fun CheckEndOrgasm()
    {
        if (this.heatAmount < this.heatRequired) ForceEndOrgasm()
    }

    private fun ForceEndOrgasm()
    {
        if (!isInOrgasm) return
        OnOrgasm.AllOnOrgasm(owner)
            .filter { power: OnOrgasm? -> power !is SexualHeat }
            .forEach { power: OnOrgasm? -> power?.onEndOrgasm(this) }
        this.onEndOrgasm(this)
        Orgasm.endOrgasm(owner)

    }

    private val heatRequired: Int
        //    @Override
        get() = max((HEAT_REQUIREDOrigin -
                owner.powers.stream().filter { power: AbstractPower? -> power is SexualHeatNeedModifier }
                    .mapToInt { power: AbstractPower -> (power as SexualHeatNeedModifier).reduceSexualHeatNeeded() }
                    .sum()).toDouble(), MIN_HEAT_REQUIRE.toDouble()
        )
            .toInt()

    private fun bubbleMessage(isDeBuffVer: Boolean, messageIndex: Int)
    {
        bubbleMessage(isDeBuffVer, powerStrings.getDESCRIPTION(messageIndex))
    }

    private fun bubbleMessage(isDeBuffVer: Boolean, message: String?)
    {
        PowerUtility.BubbleMessage(
            this.getBarRenderHitBox(), isDeBuffVer, message, -owner.hb.width / 2,
            PowerUtility.BubbleMessageHigher_HEIGHT + Height()
        )
    }

    override fun getBarRenderHitBox(): Hitbox
    {
        if (owner !is AbstractPlayer) return owner.hb
        val tipHitbox = ReflectionHacks.getPrivate<Hitbox>(
            AbstractDungeon.overlayMenu.energyPanel,
            EnergyPanel::class.java,
            "tipHitbox"
        )
        if (tipHitbox != null) return tipHitbox
        Logger.warning("no EnergyPanel found when " + this + "get hitbox.")
        return owner.hb
        //        return EnergyPanel.tipHitbox;
    }

    override fun getAmountForDraw() = this.heatAmount


    override fun InitializePostApplyThisPower(addedPower: SexualHeat)
    {
        CheckOrgasm()
        updateDescription()
    }

    override fun onRemove()
    {
        ForceEndOrgasm()
    }

    override fun makeMessage(): BarRenderUpdateMessage
    {
        return super.makeMessage().setDetail { bar: RenderOnThing? ->
            if (bar is BarRenderOnThing) bar.barShadowColor =
                if (this.isInOrgasm)
                    this.barOrgasmShadowColor
                else this.barShadowColorOrigin
        }
    }

    override fun makeNewBarRenderOnCreature(): BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, out RenderOnThing>
    {
        return BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, RenderOnThing> { hitbox: Supplier<Hitbox>, power: HasBarRenderOnCreature ->
            if (ShaderUtility.canUseShader && owner is AbstractPlayer) return@BiFunction BarRenderOnThing_Ring_Text(
                hitbox,
                power
            )
            else return@BiFunction BarRenderOnThing(hitbox, power)
        }
    }

    override fun onPlayCard(card: AbstractCard, m: AbstractMonster?)
    {
        if (!this.isInOrgasm || !owner.isPlayer) return
        OnOrgasm.AllOnOrgasm(owner).forEach { power: OnOrgasm ->
            power.onSquirt(this, card)
        }
        if (!card.freeToPlay() && card.costForTurn != 0)
        {
            AutoDoneInstantAction.addToBotAbstract(::ForceEndOrgasm, 2)
        }
    }

    override fun atEndOfTurn(isPlayer: Boolean)
    {
        if (!isPlayer) return
        this.reduceSexualHeat(this.heatAmount)
        ForceEndOrgasm()
    }


    override fun Height(): Float
    {
        return HEIGHT
    }

    override fun setupBarOriginColor(): Color
    {
        return PINK
    }

    override fun maxBarAmount(): Int
    {
        return heatRequired
    }

    override fun getDescriptionStrings(): String
    {
        return powerStrings.getDESCRIPTION(if (owner.isPlayer) 0 else 1)
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(
            heatRequired, if (this.isInOrgasm) String.format(
                powerStrings.getDESCRIPTION(2),
                getOrgasmTimesInTurn()
            )
            else ""
        )
    }

    override fun onOrgasm(SexualHeatPower: SexualHeat)
    {
        AutoDoneInstantAction.addToBotAbstract { bubbleMessage(false, if (IsContinueOrgasm()) 4 else 3) }
        if (!isInOrgasm || owner !is AbstractPlayer) return
        action_addOrgasmEffectAndSetLevel(this).addToBotAsAbstractAction()
    }

    override fun onEndOrgasm(SexualHeatPower: SexualHeat)
    {
        bubbleMessage(true, 5)
    }

    override fun onSquirt(SexualHeatPower: SexualHeat, card: AbstractCard)
    {
        bubbleMessage(false, 6)
    }

    override fun makeBarText(): String
    {
        return "%d/%d" + String.format("(%d)", getOrgasmTimesInTurn())
    }

    override fun onSuccessfullyPreventOrgasm(SexualHeatPower: SexualHeat)
    {
        bubbleMessage(false, 7)
    }

    override fun onContinuallyOrgasm(SexualHeatPower: SexualHeat)
    {
        this.addToBot(DrawCardAction(DRAW_CARD_INContinueOrgasm))
    }


    object Orgasm
    {
        fun endOrgasm(creature: AbstractCreature)
        {
            OrgasmField.isInOrgasm[creature] = false
        }

        fun isPlayerInOrgasm(): Boolean = OrgasmField.isInOrgasm[AbstractDungeon.player]

        fun startOrgasm(creature: AbstractCreature)
        {
            OrgasmField.isInOrgasm[creature] = true
        }

        fun isInOrgasm(creature: AbstractCreature): Boolean
        {
            return OrgasmField.isInOrgasm[creature]
        }

        @SpirePatch2(clz = AbstractCreature::class, method = "<class>")
        private object OrgasmField
        {
            @JvmField
            var isInOrgasm: SpireField<Boolean> = SpireField<Boolean> { false }
        }
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(SexualHeat::class.java)
        const val HEAT_REQUIREDOrigin: Int = 10
        val HEIGHT: Float = 55 * Settings.scale
        const val MIN_HEAT_REQUIRE: Int = 2
        private const val DRAW_CARD_INContinueOrgasm = 0

        //绘制相关
        private val PINK = Color(1f, 0.7529f, 0.7961f, 1.0f)
        val MAX_ORGASM_HEART_STREAM_LEVEL: Int
            get() = if (SuperstitioConfig.isEnablePerformanceMode) 3
            else 8

        fun addToBot_addSexualHeat(target: AbstractCreature, heatAmount: Int)
        {
            useConsumer_addSexualHeat(target, heatAmount, AutoDoneInstantAction.Companion::addToBotAbstract)
        }

        fun useConsumer_addSexualHeat(
            target: AbstractCreature, heatAmount: Int,
            action: Consumer<VoidSupplier>
        )
        {
            if (heatAmount < 0)
            {
                Logger.warning("add error number " + SexualHeat::class.java.simpleName + ". Amount: " + heatAmount)
                return
            }
            val activeSexualHeat = getActiveSexualHeat(target)
            if (activeSexualHeat != null)
            {
                action.accept(VoidSupplier { activeSexualHeat.addSexualHeat(heatAmount) })
            }
            else
            {
                val sexualHeat = SexualHeat(target, 0)
                action.accept(VoidSupplier {
                    target.addPower(sexualHeat)
                    sexualHeat.addSexualHeat(heatAmount)
                })
            }
        }

        fun addToBot_reduceSexualHeat(target: AbstractCreature, heatAmount: Int)
        {
            addAction_reduceSexualHeat(target, heatAmount, AutoDoneInstantAction.Companion::addToBotAbstract)
        }

        fun addAction_reduceSexualHeat(target: AbstractCreature, heatAmount: Int, action: Consumer<VoidSupplier>)
        {
            if (heatAmount < 0)
            {
                Logger.warning("reduce error number " + SexualHeat::class.java.simpleName + ". Amount: " + heatAmount)
                return
            }
            getActiveSexualHeat(target)?.let { power: SexualHeat ->
                action.accept(
                    VoidSupplier { power.reduceSexualHeat(heatAmount) })
            }
        }

        fun isInOrgasm(creature: AbstractCreature): Boolean
        {
            val power = getActiveSexualHeat(creature)
            return power != null && power.isInOrgasm
        }

        fun getActiveSexualHeat(creature: AbstractCreature): SexualHeat?
        {
            return creature.powers.filterIsInstance<SexualHeat>().firstOrNull()
        }

        fun getOrgasmTimesInTurn(): Int = InBattleDataManager.OrgasmTimesInTurn

        private fun AddOrgasmTime()
        {
            InBattleDataManager.OrgasmTimesInTurn++
            InBattleDataManager.OrgasmTimesTotal++
        }

        private fun action_addOrgasmEffectAndSetLevel(sexualHeat: SexualHeat): VoidSupplier
        {
            return RenderHeartStream.action_addHeartStreamEffectAndSetLevel(
                MAX_ORGASM_HEART_STREAM_LEVEL, { !Orgasm.isPlayerInOrgasm() },
                sexualHeat.owner::hb, InBattleDataManager.OrgasmTimesInTurn
            )
        }
    }
}