package superstitio.cards.lupa;

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.SuperstitioModSetup;
import superstitio.cards.lupa.AttackCard.breast.Fuck_Nipple;
import superstitio.cards.lupa.AttackCard.breast.Job_Breast;
import superstitio.cards.lupa.AttackCard.genital.Fuck_Anal;
import superstitio.cards.lupa.AttackCard.genital.Fuck_Vaginal;
import superstitio.cards.lupa.AttackCard.hand.Job_Armpit;
import superstitio.cards.lupa.AttackCard.hand.Job_Hand_BaseCard;
import superstitio.cards.lupa.AttackCard.headTempCard.Fuck_Eye;
import superstitio.cards.lupa.AttackCard.headTempCard.Job_Hair;
import superstitio.cards.lupa.AttackCard.legDrawCard.Job_Foot;
import superstitio.cards.lupa.AttackCard.legDrawCard.Job_LegPit;
import superstitio.cards.lupa.AttackCard.mouthCost.Fuck_Throat;
import superstitio.cards.lupa.AttackCard.mouthCost.Job_Blow;
import superstitio.cards.lupa.AttackCard.torsoJustDamage.Fuck_Navel;
import superstitio.cards.lupa.AttackCard.torsoJustDamage.Job_Groin;
import superstitio.cards.lupa.TempCard.Fuck_Ear;
import superstitio.cards.modifiers.damage.SexDamage;
import superstitio.cards.modifiers.damage.SexDamage_Fuck;
import superstitio.cards.modifiers.damage.SexDamage_Job;
import superstitio.powers.InsideSemen;
import superstitio.powers.OutsideSemen;
import superstitio.utils.ActionUtility;

import static superstitio.orbs.orbgroup.SexMarkOrbGroup.SexMarkType;
import static superstitio.orbs.orbgroup.SexMarkOrbGroup.addToBot_GiveMarkToOrbGroup;

public abstract class AbstractLupaCard_FuckJob extends AbstractLupaCard {
    /**
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String customCardType) {
        this(id, cardType, cost, cardRarity, cardTarget, SuperstitioModSetup.LupaEnums.LUPA_CARD, customCardType);
    }

    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public AbstractLupaCard_FuckJob(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                                    String customCardType) {
        super(id, cardType, cost, cardRarity, cardTarget, cardColor, customCardType);
        if (!SuperstitioModSetup.enableSFW)
            this.setBackgroundTexture(
                    DataManager.makeImgFilesPath_UI("bg_attack_512_semen"),
                    DataManager.makeImgFilesPath_UI("bg_attack_semen"));

        if (id.contains("Fuck"))
            DamageModifierManager.addModifier(this, new SexDamage_Fuck());
        else if (id.contains("Job"))
            DamageModifierManager.addModifier(this, new SexDamage_Job());
        else
            DamageModifierManager.addModifier(this, new SexDamage());
    }

    public static void addToTop_gainSexMark_Inside(String sexName) {
        ActionUtility.addToBot_applyPower(new InsideSemen(AbstractDungeon.player, 1));
        addToBot_GiveMarkToOrbGroup(sexName, SexMarkType.Inside);
    }

    public static void addToTop_gainSexMark_Outside(String sexName) {
        ActionUtility.addToBot_applyPower(new OutsideSemen(AbstractDungeon.player, 1));
        addToBot_GiveMarkToOrbGroup(sexName, SexMarkType.OutSide);
    }

    public static BodyPart getBodyCard(AbstractLupaCard_FuckJob fuckJob) {
        if (fuckJob instanceof Fuck_Nipple || fuckJob instanceof Job_Breast)
            return BodyPart.breast;
        if (fuckJob instanceof Job_Foot || fuckJob instanceof Job_LegPit)
            return BodyPart.leg;
        if (fuckJob instanceof Fuck_Anal || fuckJob instanceof Fuck_Vaginal)
            return BodyPart.genital;
        if (fuckJob instanceof Job_Armpit || fuckJob instanceof Job_Hand_BaseCard)
            return BodyPart.hand;
        if (fuckJob instanceof Fuck_Eye || fuckJob instanceof Fuck_Ear || fuckJob instanceof Job_Hair)
            return BodyPart.head;
        if (fuckJob instanceof Fuck_Throat || fuckJob instanceof Job_Blow)
            return BodyPart.mouth;
        if (fuckJob instanceof Fuck_Navel || fuckJob instanceof Job_Groin)
            return BodyPart.torso;
        return BodyPart.unknown;
    }

    public enum BodyPart {
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
