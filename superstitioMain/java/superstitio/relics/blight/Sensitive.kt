package superstitio.relics.blight

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.Logger
import superstitio.powers.SexualHeat
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onEndOrgasm
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasmFirst
import superstitio.relics.SuperstitioRelic
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.powers.AllCardCostModifier
import superstitioapi.powers.AllCardCostModifier_PerEnergy
import superstitioapi.powers.interfaces.HasAllCardCostModifyEffect
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.relicToBlight.InfoBlight.BecomeInfoBlight
import superstitioapi.utils.setDescriptionArgs
import java.lang.reflect.InvocationTargetException
import kotlin.math.min

@Seen
class Sensitive : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND),
    HasAllCardCostModifyEffect, BecomeInfoBlight,
    OnOrgasm_onOrgasmFirst, OnOrgasm_onEndOrgasm
{
    private val hpLose = 0
    override fun onEndOrgasm(SexualHeatPower: SexualHeat)
    {
        AutoDoneInstantAction.addToBotAbstract { AllCardCostModifier.RemoveAllByHolder(this) }
    }

    override fun IDAsHolder(): String
    {
        return ID
    }

    override val activeEffectHold: AllCardCostModifier?
        get() = AllCardCostModifier.getAllByHolder(this).firstOrNull { it.isActive }


    override fun onOrgasmFirst(SexualHeatPower: SexualHeat)
    {
        val decreaseCost = min(SexualHeat.getOrgasmTimesInTurn(), AbstractDungeon.player.energy.energyMaster)
        AutoDoneInstantAction.addToBotAbstract {
            try
            {
                AllCardCostModifier.addTo_Bot_EditAmount_Top_FirstByHolder(
                    this@Sensitive, decreaseCost, { power: AllCardCostModifier? ->
                        if (power != null)
                            return@addTo_Bot_EditAmount_Top_FirstByHolder 1
                        else
                            return@addTo_Bot_EditAmount_Top_FirstByHolder 1
                    },
                    AllCardCostModifier_PerEnergy::class.java.getConstructor(
                        AbstractCreature::class.java,
                        Int::class.java,
                        Int::class.java,
                        HasAllCardCostModifyEffect::class.java
                    )
                )
            }
            catch (e: NoSuchMethodException)
            {
                Logger.error(e)
            }
            catch (e: InvocationTargetException)
            {
                Logger.error(e)
            }
            catch (e: InstantiationException)
            {
                Logger.error(e)
            }
            catch (e: IllegalAccessException)
            {
                Logger.error(e)
            }
        }
    }


    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(SexualHeatRate)
    }

    override fun onPlayCard(card: AbstractCard, m: AbstractMonster?)
    {
        if (card.isInAutoplay) return
        var amount = 0
        if (card.costForTurn >= 1) amount += card.costForTurn
        if (card.costForTurn == -1) amount += card.energyOnUse
        if (amount == 0) return
        SexualHeat.useConsumer_addSexualHeat(
            AbstractDungeon.player,
            amount * SexualHeatRate,
            AutoDoneInstantAction.Companion::addToTopAbstract
        )
    }

    override fun obtain()
    {
        InfoBlight.obtain(this)
    }

    override fun instantObtain(p: AbstractPlayer, slot: Int, callOnEquip: Boolean)
    {
        InfoBlight.instanceObtain(this, callOnEquip)
    }

    override fun instantObtain()
    {
        InfoBlight.instanceObtain(this, true)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Sensitive::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.SPECIAL

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        private const val SexualHeatRate = 2
    }
}