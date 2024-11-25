package superstitio.relics.blight

import basemod.AutoAdd.Seen
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.DataManager
import superstitio.DataManager.CanOnlyDamageDamageType
import superstitio.delayHpLose.DelayHpLosePower_HealOnVictory
import superstitio.delayHpLose.IsImmunityFields
import superstitio.relics.SuperstitioRelic
import superstitioapi.powers.interfaces.TriPredicate
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.relicToBlight.InfoBlight.BecomeInfoBlight
import superstitioapi.utils.ActionUtility

@Seen
class DevaBody_Masochism : SuperstitioRelic(ID, RELIC_TIER, LANDING_SOUND), BecomeInfoBlight {
    override fun updateDescriptionArgs() {
    }

    override fun atBattleStart() {
        this.flash()
        ActionUtility.addToBot(RelicAboveCreatureAction(AbstractDungeon.player, this))
        SetPlayerImmunity()
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
        val ID: String = DataManager.MakeTextID(DevaBody_Masochism::class.java)

        // 遗物类型
        private val RELIC_TIER = RelicTier.SPECIAL

        // 点击音效
        private val LANDING_SOUND = LandingSound.FLAT

        fun SetPlayerImmunity() {
            IsImmunityFields.checkShouldImmunity[AbstractDungeon.player] =
                TriPredicate { player: AbstractPlayer?, damageInfo: DamageInfo?, damageAmount: Int ->
                    if (damageInfo!!.type == CanOnlyDamageDamageType.UnBlockAbleDamageType) {
                        return@TriPredicate false
                    }
                    ActionUtility.addToTop_applyPower(
                        DelayHpLosePower_HealOnVictory(
                            AbstractDungeon.player,
                            damageAmount
                        )
                    )
                    true
                }
        }
    }
}



