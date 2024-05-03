package SuperstitioMod.cards.Lupa;

import SuperstitioMod.DataManager;
import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.DamageMod.SexDamage;
import SuperstitioMod.powers.FreshSemen;
import SuperstitioMod.powers.SexMark_Inside;
import SuperstitioMod.powers.SexMark_Outside;
import SuperstitioMod.utils.ActionUtility;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

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
        DamageModifierManager.addModifier(this,new SexDamage());
    }

    public static void addToTop_gainSexMark_Inside(String sexName) {
        ActionUtility.addToBot_applyPowerToPlayer(new FreshSemen(AbstractDungeon.player, 1));
        AbstractDungeon.actionManager.addToTop(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new SexMark_Inside(AbstractDungeon.player, sexName)));
    }

    public static void addToTop_gainSexMark_Outside(String sexName) {
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
                        new SexMark_Outside(AbstractDungeon.player, sexName)));
    }
}
