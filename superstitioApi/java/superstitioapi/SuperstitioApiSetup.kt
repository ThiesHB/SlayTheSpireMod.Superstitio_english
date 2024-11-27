package superstitioapi

import basemod.BaseMod
import basemod.interfaces.EditStringsSubscriber
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.localization.PowerStrings

@SpireInitializer
class SuperstitioApiSetup : EditStringsSubscriber
{
    init
    {
        BaseMod.subscribe(this)
        Logger.run("Done $this subscribing")
    }

    override fun receiveEditStrings()
    {
        BaseMod.loadCustomStringsFile(
            PowerStrings::class.java,
            DataUtility.makeLocalizationPath(Settings.language, "power")
        )
    }

    object DamageEffect
    {
        @SpireEnum
        lateinit var HeartMultiInOne: AttackEffect
    }

    companion object
    {
        const val MOD_NAME: String = "SuperstitioApi"

        @JvmStatic
        fun initialize()
        {
            val mod = SuperstitioApiSetup()
        }
    }
}
