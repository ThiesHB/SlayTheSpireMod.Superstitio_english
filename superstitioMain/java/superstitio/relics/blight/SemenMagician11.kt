package superstitio.relics.blight

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitio.DataManager
import superstitio.DataManager.CardTagsType
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.lupa.OnAddSemenPower
import superstitio.powers.lupaOnly.FloorSemen
import superstitio.powers.lupaOnly.InsideSemen
import superstitio.powers.lupaOnly.OutsideSemen
import superstitio.relics.SuperstitioRelic
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.relicToBlight.InfoBlight.BecomeInfoBlight
import superstitioapi.utils.ActionUtility

@Seen
class SemenMagician : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), BecomeInfoBlight {
    override fun updateDescriptionArgs() {
    }

    override fun onPlayCard(card: AbstractCard, m: AbstractMonster) {
        if (card.type == CardType.ATTACK) addToBot_AddSemen(card)
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
        val ID: String = DataManager.MakeTextID(SemenMagician::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.SPECIAL

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT

        private fun addToBot_AddSemen(card: AbstractCard) {
            var shouldApply = true
            for (power in AbstractDungeon.player.powers) {
                if (power is OnAddSemenPower) {
                    if (!power.onAddSemen_shouldApply(getSemenType(card))) shouldApply =
                        false
                }
            }
            if (shouldApply) {
                ActionUtility.addToTop_applyPower(getSemenType(card))
            }
        }

        private fun getSemenType(card: AbstractCard): AbstractPower? {
            if (card.type != CardType.ATTACK) return null
            if (!(card is FuckJob_Card && card is SuperstitioCard)) return FloorSemen(AbstractDungeon.player, 1)
            else if (card.hasTag(CardTagsType.InsideEjaculation)) return InsideSemen(AbstractDungeon.player, 1)
            else if (card.hasTag(CardTagsType.OutsideEjaculation)) return OutsideSemen(AbstractDungeon.player, 1)
            return FloorSemen(AbstractDungeon.player, 1)
        }
    }
}