package superstitio.relics.defect

import basemod.AutoAdd
import basemod.abstracts.CustomSavable
import com.evacipated.cardcrawl.mod.stslib.relics.BetterOnLoseHpRelic
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.PowerTip
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.powers.SexualHeat
import superstitio.relics.SuperstitioRelic
import superstitioapi.utils.ActionUtility

/**
 * 右键点击切换S和M形态。
 * S：每造成超过SadismModeRate的伤害获得一点快感。
 * M：每受到超过MasochismModeRate的伤害获得一点快感。
 */
@AutoAdd.Ignore
class SorM //Default();
//updateDesAndImg();
    : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), ClickableRelic, CustomSavable<Int>, BetterOnLoseHpRelic
{
    private var ClickTime = 0
    private var MasochismMode = false
    private var SadismMode = false


    private fun Default()
    {
        MasochismMode = true
        SadismMode = false
    }

    //    @Override
    //    public void updateDescription(AbstractPlayer.PlayerClass c) {
    //        updateDesAndImg();
    //    }
    private fun updateDesAndImg()
    {
        this.description = this.updatedDescription
        tips.clear()
        tips.add(PowerTip(this.name, this.description))
        this.initializeTips()
        if (!MasochismMode && !SadismMode) Default()
    }

    private fun AddSexualHeat(amount: Int)
    {
        SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, amount)
    }

    override fun atBattleStart()
    {
        updateDesAndImg()
    }

    override fun onLoseHp(damageAmount: Int)
    {
    }

    override fun betterOnLoseHp(damageInfo: DamageInfo, i: Int): Int
    {
        if (ActionUtility.isNotInBattle) return i
        if (!MasochismMode) return i
        if (i < MasochismModeDamageNeed) return i
        if (damageInfo.type == CanOnlyDamageDamageType.UnBlockAbleDamageType) return i
        AddSexualHeat(i / MasochismModeDamageNeed * MasochismModeSexualHeatRate)
        this.flash()
        return i
    }

    override fun onAttack(info: DamageInfo, damageAmount: Int, target: AbstractCreature)
    {
        if (!SadismMode) return
        if (damageAmount < SadismModeDamageNeed) return
        AddSexualHeat(SadismModeSexualHeatRate)
        //        AddSexualHeat(damageAmount / SadismModeRate * SexualHeatRate);
        this.flash()
    }

    override fun updateDescriptionArgs()
    {
    }

    override fun getDescriptionStrings(): String
    {
        if (MasochismMode && SadismMode) return String.format(
            DESCRIPTIONS[3], MasochismModeDamageNeed, MasochismModeSexualHeatRate, SadismModeDamageNeed,
            MasochismModeSexualHeatRate
        )
        if (SadismMode) return String.format(DESCRIPTIONS[2], SadismModeDamageNeed, MasochismModeSexualHeatRate)
        if (MasochismMode) return String.format(DESCRIPTIONS[1], MasochismModeDamageNeed, MasochismModeSexualHeatRate)
        return String.format(
            DESCRIPTIONS[0], MasochismModeDamageNeed, MasochismModeSexualHeatRate, SadismModeDamageNeed,
            MasochismModeSexualHeatRate
        )
    }

    override fun onRightClick()
    {
        if (!ActionUtility.isNotInBattle) return
        this.flash()
        if (ClickTime >= 99)
        {
            MasochismMode = true
            SadismMode = true
            return
        }
        ClickTime++
        MasochismMode = !MasochismMode
        SadismMode = !SadismMode
        updateDesAndImg()
    }

    override fun onSave(): Int
    {
        if (MasochismMode && SadismMode) return 30000 + ClickTime
        if (MasochismMode) return 10000 + ClickTime
        if (SadismMode) return 20000 + ClickTime
        return 0
    }

    override fun onLoad(integer: Int)
    {
        this.ClickTime = integer % 10000
        when (integer - this.ClickTime)
        {
            30000 ->
            {
                MasochismMode = true
                SadismMode = true
            }

            10000 ->
            {
                MasochismMode = true
                SadismMode = false
            }

            20000 ->
            {
                MasochismMode = false
                SadismMode = true
            }

            else  ->
            {
                MasochismMode = false
                SadismMode = false
            }
        }
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(SorM::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.SPECIAL

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        private const val SadismModeDamageNeed = 15
        private const val MasochismModeDamageNeed = 3

        private const val MasochismModeSexualHeatRate = 1
        private const val SadismModeSexualHeatRate = 2
    }
}