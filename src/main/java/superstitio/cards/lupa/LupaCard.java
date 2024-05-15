package superstitio.cards.lupa;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;

import java.util.Arrays;

import static superstitio.cards.CardOwnerPlayerManager.IsLupaCard;

public abstract class LupaCard extends SuperstitioCard implements IsLupaCard {


    /**
     * 普通的方法
     *
     * @param id         卡牌ID
     * @param cardType   卡牌类型
     * @param cost       卡牌消耗
     * @param cardRarity 卡牌稀有度
     * @param cardTarget 卡牌目标
     */
    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget) {
        this(id, cardType, cost, cardRarity, cardTarget, CardTypeToString(cardType));
    }

    /**
     * 当需要自定义卡牌存储的二级目录名时
     */
    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, String imgSubFolder) {
        this(id, cardType, cost, cardRarity, cardTarget, DataManager.SPTT_DATA.LupaEnums.LUPA_CARD, imgSubFolder);
    }

    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor) {
        this(id, cardType, cost, cardRarity, cardTarget, cardColor, CardTypeToString(cardType));
    }

    public LupaCard(String id, CardType cardType, int cost, CardRarity cardRarity, CardTarget cardTarget, CardColor cardColor,
                    String imgSubFolder) {
        super(id, cardType, cost, cardRarity, cardTarget, cardColor, imgSubFolder);
    }

    public static void setupBlock(SuperstitioCard card, int amount, int amountOfAutoUpgrade, AbstractBlockModifier... blockModifiers) {
        SuperstitioCard.setupBlock(card, amount, amountOfAutoUpgrade, blockModifiers);
        if (blockModifiers == null || blockModifiers.length == 0) return;
        if (Arrays.stream(blockModifiers).anyMatch(blockModifier -> blockModifier instanceof RemoveDelayHpLoseBlock)) {
            DelayHpLosePatch.GainBlockTypeFields.ifReduceDelayHpLose.set(card, true);
        }
    }

    public static void addToBot_gainBlock(SuperstitioCard card, int amount) {
        if (DelayHpLosePatch.GainBlockTypeFields.ifReduceDelayHpLose.get(card)) {
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, AbstractDungeon.player, true);
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AbstractGameAction.AttackEffect.SHIELD));
        }
        else
            SuperstitioCard.addToBot_gainBlock(card, amount);
    }

    protected void useSemen(int amount) {
        int lastAmount = amount;
        final int insideSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof InsideSemen).map(power -> power.amount)
                .findAny().orElse(0);
        final int outsideSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof OutsideSemen).map(power -> power.amount)
                .findAny().orElse(0);

        if (lastAmount <= insideSemenValue) {
            addToBot_reducePower(InsideSemen.POWER_ID, lastAmount);
            return;
        }
        addToBot_reducePower(InsideSemen.POWER_ID, insideSemenValue);
        lastAmount -= insideSemenValue;
        if (lastAmount <= outsideSemenValue) {
            addToBot_reducePower(OutsideSemen.POWER_ID, lastAmount);
            return;
        }
        addToBot_reducePower(OutsideSemen.POWER_ID, outsideSemenValue);
    }

    protected boolean hasEnoughSemen(int amount) {
        final int semenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof OutsideSemen|| power instanceof InsideSemen)
                .map(power -> power.amount).findAny().orElse(0);
        return amount <= semenValue;
    }
}
