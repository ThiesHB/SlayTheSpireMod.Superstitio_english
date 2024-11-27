package superstitio.relics.blight

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.characters.AbstractPlayer
import superstitio.DataManager
import superstitio.relics.SuperstitioRelic
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.relicToBlight.InfoBlight.BecomeInfoBlight

@Seen
class JokeDescription : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), BecomeInfoBlight {
    override fun updateDescriptionArgs() {
    }

    override fun obtain() {
        InfoBlight.obtain(this)
    }

    override fun instantObtain(p: AbstractPlayer, slot: Int, callOnEquip: Boolean) {
        InfoBlight.instanceObtain(this, callOnEquip)
    }

    override fun instantObtain() {
        InfoBlight.instanceObtain(this, true)
    }

    companion object {
        val ID: String = DataManager.MakeTextID(JokeDescription::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.SPECIAL

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT
    }
}