package superstitio.cards.lupa;

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.SuperstitioModSetup;
import superstitio.cards.modifiers.damage.SexDamage;
import superstitio.orbs.SexMarkOrb_Inside;
import superstitio.orbs.SexMarkOrb_Outside;
import superstitio.orbs.actions.GiveSexMarkToOrbGroupAction;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitio.powers.InsideSemen;
import superstitio.powers.OutsideSemen;
import superstitio.utils.ActionUtility;

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
        DamageModifierManager.addModifier(this, new SexDamage());
    }

    public static void addToTop_gainSexMark_Inside(String sexName) {
        ActionUtility.addToBot_applyPower(new InsideSemen(AbstractDungeon.player, 1));
        AbstractDungeon.actionManager.addToBottom(
                new GiveSexMarkToOrbGroupAction((SexMarkOrbGroup) InBattleDataManager.orbGroups.stream()
                        .filter(orbGroup -> orbGroup instanceof SexMarkOrbGroup).findAny().orElse(null), new SexMarkOrb_Inside(sexName)));
    }

    public static void addToTop_gainSexMark_Outside(String sexName) {
        ActionUtility.addToBot_applyPower(new OutsideSemen(AbstractDungeon.player, 1));
        AbstractDungeon.actionManager.addToBottom(
                new GiveSexMarkToOrbGroupAction((SexMarkOrbGroup) InBattleDataManager.orbGroups.stream()
                        .filter(orbGroup -> orbGroup instanceof SexMarkOrbGroup).findAny().orElse(null), new SexMarkOrb_Outside(sexName)));
    }
}
