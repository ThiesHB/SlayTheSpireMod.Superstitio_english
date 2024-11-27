package superstitio.cards.general

import basemod.helpers.CardModifierManager
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager
import superstitio.DataManager.SPTT_DATA
import superstitio.SuperstitioConfig
import superstitio.cardModifier.modifiers.card.InsideEjaculationTag
import superstitio.cardModifier.modifiers.card.OutsideEjaculationTag
import superstitio.cardModifier.modifiers.damage.SexDamage
import superstitio.cardModifier.modifiers.damage.SexDamage_Fuck
import superstitio.cardModifier.modifiers.damage.SexDamage_Job
import superstitio.cards.SuperstitioCard
import superstitio.cards.general.AttackCard.breast.Job_Breast
import superstitio.cards.general.AttackCard.genitalSpecialEffect.Fuck_Anal
import superstitio.cards.general.AttackCard.genitalSpecialEffect.Fuck_Vaginal
import superstitio.cards.general.AttackCard.hand.Job_Armpit
import superstitio.cards.general.AttackCard.hand.Job_Hand
import superstitio.cards.general.AttackCard.headTempCard.Job_Hair
import superstitio.cards.general.AttackCard.legDrawCard.Job_Foot
import superstitio.cards.general.AttackCard.mouthCost.Fuck_Throat
import superstitio.cards.general.TempCard.Fuck_Ear
import superstitio.cards.lupa.AttackCard.Job_Blow
import superstitio.cards.lupa.AttackCard.Job_Groin
import superstitio.cards.lupa.AttackCard.Job_LegPit
import superstitio.cards.maso.AttackCard.Fuck_Eye
import superstitio.cards.maso.AttackCard.Fuck_Navel
import superstitio.cards.maso.AttackCard.Fuck_Nipple
import superstitio.powers.lupaOnly.FloorSemen
import superstitio.powers.lupaOnly.InsideSemen
import superstitio.powers.lupaOnly.OutsideSemen

interface FuckJob_Card {
    sealed class SexType {
        data object Fuck : SexType()
        data object Job : SexType()
        data object Unknown : SexType()
        companion object {
            fun values(): Array<SexType> {
                return arrayOf(Fuck, Job, Unknown)
            }

            fun valueOf(value: String): SexType {
                return when (value) {
                    "Fuck" -> Fuck
                    "Job" -> Job
                    "Unknown" -> Unknown
                    else -> throw IllegalArgumentException("No object superstitio.cards.general.FuckJob_Card.SexType.$value")
                }
            }
        }
    }

    sealed class BodyPart {
        data object leg : BodyPart()
        data object hand : BodyPart()
        data object breast : BodyPart()
        data object head : BodyPart()
        data object mouth : BodyPart()
        data object torso : BodyPart()
        data object genital : BodyPart()
        data object unknown : BodyPart()
        companion object {
            fun values(): Array<BodyPart> {
                return arrayOf(leg, hand, breast, head, mouth, torso, genital, unknown)
            }

            fun valueOf(value: String): BodyPart {
                return when (value) {
                    "leg" -> leg
                    "hand" -> hand
                    "breast" -> breast
                    "head" -> head
                    "mouth" -> mouth
                    "torso" -> torso
                    "genital" -> genital
                    "unknown" -> unknown
                    else -> throw IllegalArgumentException("No object superstitio.cards.general.FuckJob_Card.BodyPart.$value")
                }
            }
        }
    }

    companion object {
        fun getBodyPartType(fuckJob: FuckJob_Card?): BodyPart {
            if (fuckJob is Fuck_Nipple || fuckJob is Job_Breast) return BodyPart.breast
            if (fuckJob is Job_Foot || fuckJob is Job_LegPit) return BodyPart.leg
            if (fuckJob is Fuck_Anal || fuckJob is Fuck_Vaginal) return BodyPart.genital
            if (fuckJob is Job_Armpit || fuckJob is Job_Hand) return BodyPart.hand
            if (fuckJob is Fuck_Eye || fuckJob is Fuck_Ear || fuckJob is Job_Hair) return BodyPart.head
            if (fuckJob is Fuck_Throat || fuckJob is Job_Blow) return BodyPart.mouth
            if (fuckJob is Fuck_Navel || fuckJob is Job_Groin) return BodyPart.torso
            return BodyPart.unknown
        }

        /**
         * 手动进行配置，手动指定SexType来配置卡牌
         */
        fun initFuckJobCard(card: SuperstitioCard, sexType: SexType) {
            if (!SuperstitioConfig.isEnableSFW()) card.setBackgroundTexture(
                SPTT_DATA.BG_ATTACK_512_SEMEN,
                SPTT_DATA.BG_ATTACK_SEMEN
            )
            if (sexType == SexType.Fuck) {
                CardModifierManager.addModifier(card, InsideEjaculationTag())
                DamageModifierManager.addModifier(card, SexDamage_Fuck())
            } else if (sexType == SexType.Job) {
                CardModifierManager.addModifier(card, OutsideEjaculationTag())
                DamageModifierManager.addModifier(card, SexDamage_Job())
            } else DamageModifierManager.addModifier(card, SexDamage())
        }

        /**
         * 自动化进行配置，根据卡牌ID自动判断是Fuck还是Job，并自动配置卡牌
         */
        fun initFuckJobCard(card: SuperstitioCard) {
            if (card.cardID.contains("Fuck")) {
                initFuckJobCard(card, SexType.Fuck)
                return
            }
            if (card.cardID.contains("Job")) {
                initFuckJobCard(card, SexType.Job)
                return
            }
            initFuckJobCard(card, SexType.Unknown)
        }

        fun initFuckJobCardWithoutBond(card: SuperstitioCard, sexType: SexType) {
            if (!SuperstitioConfig.isEnableSFW()) card.setBackgroundTexture(
                SPTT_DATA.BG_ATTACK_512_SEMEN,
                SPTT_DATA.BG_ATTACK_SEMEN
            )
            if (sexType == SexType.Fuck) DamageModifierManager.addModifier(card, SexDamage_Fuck().removeAutoBind())
            else if (sexType == SexType.Job) DamageModifierManager.addModifier(card, SexDamage_Job().removeAutoBind())
            else DamageModifierManager.addModifier(card, SexDamage().removeAutoBind())
        }

        const val InsideSemenRate: Int = InsideSemen.Companion.semenValue
        const val OutsideSemenRate: Int = OutsideSemen.Companion.semenValue
        const val FloorSemenRate: Int = FloorSemen.Companion.semenValue
    }
}
