package superstitio.cards.lupa;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.delayHpLose.DelayHpLosePatch;
import superstitio.delayHpLose.DelayHpLosePower_ApplyEachTurn;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.powers.lupaOnly.FloorSemen;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;

import java.util.Arrays;

import static superstitio.cards.CardOwnerPlayerManager.IsLupaCard;
import static superstitio.cards.general.FuckJob_Card.*;

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
            DelayHpLosePower_ApplyEachTurn.addToBot_removePower(amount, AbstractDungeon.player, AbstractDungeon.player, true);
            AbstractDungeon.effectList.add(
                    new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY,
                            AbstractGameAction.AttackEffect.SHIELD));
        }
        else
            SuperstitioCard.addToBot_gainBlock(card, amount);
    }

    protected void useSemen(int amount) {
        if (!hasEnoughSemen(amount)) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof InsideSemen || power instanceof OutsideSemen || power instanceof FloorSemen) {
                    addToBot_removeSpecificPower(power);
                }
            }
            return;
        }
        int lastAmount = amount;
        final int insideSemen = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof InsideSemen).map(power -> power.amount)
                .findAny().orElse(0);
        final int outsideSemen = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof OutsideSemen).map(power -> power.amount)
                .findAny().orElse(0);
        final int floorSemen = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof FloorSemen).map(power -> power.amount)
                .findAny().orElse(0);

        int insideSemenNeed = (lastAmount - (lastAmount % InsideSemenRate)) / InsideSemenRate;
        if ((lastAmount % InsideSemenRate) > ((outsideSemen * OutsideSemenRate) + floorSemen))
            insideSemenNeed += 1;
        final int insideSemenRemove;
        if (insideSemenNeed > insideSemen) {
            lastAmount -= insideSemen * InsideSemenRate;
            insideSemenRemove = insideSemen;
        }
        else {
            lastAmount = lastAmount % InsideSemenRate;
            insideSemenRemove = insideSemenNeed;
        }
        addToBot_reducePower(InsideSemen.POWER_ID, insideSemenRemove);
        if (lastAmount <= 0) return;

        int outsideSemenNeed = (lastAmount - (lastAmount % OutsideSemenRate)) / OutsideSemenRate;
        if ((lastAmount % OutsideSemenRate) > floorSemen)
            outsideSemenNeed += 1;
        final int outsideSemenRemove;
        if (outsideSemenNeed > insideSemen) {
            lastAmount -= insideSemen * OutsideSemenRate;
            outsideSemenRemove = outsideSemen;
        }
        else {
            lastAmount = lastAmount % OutsideSemenRate;
            outsideSemenRemove = outsideSemenNeed;
        }
        addToBot_reducePower(OutsideSemen.POWER_ID, outsideSemenRemove);
        if (lastAmount <= 0) return;

        final int floorSemenNeed = lastAmount;
        final int floorSemenRemove = Math.min(floorSemenNeed, floorSemen);
        addToBot_reducePower(FloorSemen.POWER_ID, floorSemenRemove);
    }

    protected boolean hasEnoughSemen(int amount) {
        final int insideSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof InsideSemen).map(power -> power.amount)
                .findAny().orElse(0) * InsideSemenRate;
        final int outsideSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof OutsideSemen).map(power -> power.amount)
                .findAny().orElse(0) * OutsideSemenRate;
        final int floorSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof FloorSemen).map(power -> power.amount)
                .findAny().orElse(0) * FloorSemenRate;
        return amount <= insideSemenValue + outsideSemenValue + floorSemenValue;
    }
}
