package superstitio.relics.a_starter

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitio.powers.SexToy
import superstitio.relics.SuperstitioRelic
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.setDescriptionArgs

@Seen
class StartWithSexToy : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND)
{
    override fun atBattleStart()
    {
        this.flash()
        this.addToBot(RelicAboveCreatureAction(AbstractDungeon.player, this))
        for (i in 0 until SexToyNum)
        {
            ActionUtility.addToBot_applyPower(
                SexToy(
                    AbstractDungeon.player,
                    1,
                    superstitio.cards.general.TempCard.SexToy.Companion.getRandomSexToyNameWithoutRng()
                )
            )
        }
    }

    override fun updateDescriptionArgs()
    {
        setDescriptionArgs(SexToyNum)
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(StartWithSexToy::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.STARTER

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
        private const val SexToyNum = 2
    }
}