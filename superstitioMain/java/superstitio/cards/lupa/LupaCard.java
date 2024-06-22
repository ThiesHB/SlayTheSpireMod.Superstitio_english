package superstitio.cards.lupa;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.powers.lupaOnly.FloorSemen;
import superstitio.powers.lupaOnly.InsideSemen;
import superstitio.powers.lupaOnly.OutsideSemen;
import superstitio.powers.lupaOnly.SemenPower;
import superstitioapi.actions.AutoDoneInstantAction;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    protected List<SemenPower> sortedSemenList() {
        List<SemenPower> collect = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof SemenPower)
                .map(power -> (SemenPower) power).sorted(SemenPower::compareTo).collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }

    protected List<SemenPower> sortedSemenList(int maxValue) {
        List<SemenPower> collect = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof SemenPower)
                .map(power -> (SemenPower) power)
                .filter(semenPower -> semenPower.getSemenValue() <= maxValue)
                .sorted(SemenPower::compareTo)
                .collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }

    protected void addToBot_useSemenAndAutoRemove(int valueNeed) {
        if (!hasEnoughSemen(valueNeed)) {
            for (AbstractPower power : AbstractDungeon.player.powers) {
                if (power instanceof InsideSemen || power instanceof OutsideSemen || power instanceof FloorSemen) {
                    addToBot_removeSpecificPower(power);
                }
            }
            return;
        }
        if (valueNeed <= 0) return;
        int valueNeedRemain = valueNeed;
        List<SemenPower> semenPowers = sortedSemenList();
        List<SemenPower> smartCheapUse = sortedSemenList(valueNeed);
        if (semenPowers == null || semenPowers.isEmpty()) return;
        SemenPower semenPower;
        if (smartCheapUse == null || smartCheapUse.isEmpty()) {
            semenPower = semenPowers.get(0);
        } else
            semenPower = smartCheapUse.get(0);

//        if (semenPower.getTotalValue() >= valueNeedRemain) {
//            semenPower.addToBot_UseValue(valueNeedRemain);
////            valueNeedRemain = 0;
//            return;
//        }
        semenPower.addToBot_UseValue(semenPower.getSemenValue());
        valueNeedRemain -= semenPower.getSemenValue();
        int finalValueNeedRemain = valueNeedRemain;
        AutoDoneInstantAction.addToBotAbstract(() -> addToBot_useSemenAndAutoRemove(finalValueNeedRemain));
    }

    protected boolean hasEnoughSemen(int amount) {
        return amount <= getTotalSemenValue();
    }

    protected int getTotalSemenValue() {
        final int insideSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof InsideSemen).map(power -> power.amount)
                .findAny().orElse(0) * InsideSemenRate;
        final int outsideSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof OutsideSemen).map(power -> power.amount)
                .findAny().orElse(0) * OutsideSemenRate;
        final int floorSemenValue = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof FloorSemen).map(power -> power.amount)
                .findAny().orElse(0) * FloorSemenRate;
        return insideSemenValue + outsideSemenValue + floorSemenValue;
    }
}
