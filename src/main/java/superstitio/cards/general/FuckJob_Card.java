package superstitio.cards.general;

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.SuperstitioModSetup;
import superstitio.cardModifier.modifiers.damage.SexDamage;
import superstitio.cardModifier.modifiers.damage.SexDamage_Fuck;
import superstitio.cardModifier.modifiers.damage.SexDamage_Job;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.AttackCard.Fuck_Nipple;
import superstitio.cards.general.AttackCard.breast.Job_Breast;
import superstitio.cards.general.AttackCard.genitalSpecialEffect.Fuck_Anal;
import superstitio.cards.general.AttackCard.genitalSpecialEffect.Fuck_Vaginal;
import superstitio.cards.general.AttackCard.hand.Job_Armpit;
import superstitio.cards.general.AttackCard.hand.Job_Hand;
import superstitio.cards.general.AttackCard.headTempCard.Job_Hair;
import superstitio.cards.general.AttackCard.legDrawCard.Job_Foot;
import superstitio.cards.general.AttackCard.legDrawCard.Job_LegPit;
import superstitio.cards.general.AttackCard.mouthCost.Fuck_Throat;
import superstitio.cards.general.AttackCard.mouthCost.Job_Blow;
import superstitio.cards.general.AttackCard.torsoJustDamage.Job_Groin;
import superstitio.cards.general.TempCard.Fuck_Ear;
import superstitio.cards.maso.AttackCard.Fuck_Eye;
import superstitio.cards.maso.AttackCard.Fuck_Navel;
import superstitio.powers.lupaOnly.FloorSemen;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;

import static superstitio.orbs.orbgroup.SexMarkOrbGroup.SexMarkType;
import static superstitio.orbs.orbgroup.SexMarkOrbGroup.addToBot_GiveMarkToOrbGroup;
import static superstitio.utils.ActionUtility.addToBot_applyPower;

public interface FuckJob_Card {
    static final int InsideSemenRate = 3;
    static final int OutsideSemenRate = 2;
    static final int FloorSemenRate = 1;

    static void addToTop_Semen_Inside() {
        addToBot_applyPower(new InsideSemen(AbstractDungeon.player, InsideSemenRate));
    }

    static void addToTop_Semen_Outside() {
        addToBot_applyPower(new OutsideSemen(AbstractDungeon.player, OutsideSemenRate));
    }

    static void addToTop_Semen_Normal() {
        addToBot_applyPower(new FloorSemen(AbstractDungeon.player, FloorSemenRate));
    }


    static void addToTop_gainSexMark_Inside(String sexName) {
        addToBot_GiveMarkToOrbGroup(sexName, SexMarkType.Inside);
    }

    static void addToTop_gainSexMark_Outside(String sexName) {
        addToBot_GiveMarkToOrbGroup(sexName, SexMarkType.OutSide);
    }

    static BodyPart getBodyCard(FuckJob_Card fuckJob) {
        if (fuckJob instanceof Fuck_Nipple || fuckJob instanceof Job_Breast)
            return BodyPart.breast;
        if (fuckJob instanceof Job_Foot || fuckJob instanceof Job_LegPit)
            return BodyPart.leg;
        if (fuckJob instanceof Fuck_Anal || fuckJob instanceof Fuck_Vaginal)
            return BodyPart.genital;
        if (fuckJob instanceof Job_Armpit || fuckJob instanceof Job_Hand)
            return BodyPart.hand;
        if (fuckJob instanceof Fuck_Eye || fuckJob instanceof Fuck_Ear || fuckJob instanceof Job_Hair)
            return BodyPart.head;
        if (fuckJob instanceof Fuck_Throat || fuckJob instanceof Job_Blow)
            return BodyPart.mouth;
        if (fuckJob instanceof Fuck_Navel || fuckJob instanceof Job_Groin)
            return BodyPart.torso;
        return BodyPart.unknown;
    }

    static void initFuckJobCard(SuperstitioCard card) {
        if (!SuperstitioModSetup.getEnableSFW())
            card.setBackgroundTexture(
                    DataManager.SPTT_DATA.BG_ATTACK_512_SEMEN,
                    DataManager.SPTT_DATA.BG_ATTACK_SEMEN);

        if (card.cardID.contains("Fuck"))
            DamageModifierManager.addModifier(card, new SexDamage_Fuck());
        else if (card.cardID.contains("Job"))
            DamageModifierManager.addModifier(card, new SexDamage_Job());
        else
            DamageModifierManager.addModifier(card, new SexDamage());
    }

    enum BodyPart {
        leg,
        hand,
        breast,
        head,
        mouth,
        torso,
        genital,
        unknown
    }
}
