package superstitioapi.utils;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.AllCardCostModifier;

import java.util.Optional;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.currMapNode;

public class ActionUtility {

    public static void addToBot_applyPower(final AbstractPower power) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(power.owner, AbstractDungeon.player, power));
    }

    public static void addToBot_applyPower(final AbstractPower power, final AbstractCreature source) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(power.owner, source, power));
    }

    public static void addToTop_applyPower(final AbstractPower power) {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(power.owner, AbstractDungeon.player, power));
    }

//    public static void addToBot_reducePower(final AbstractPower power, final AbstractCreature source) {
//        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(power.owner, source, power, power.amount));
//    }
    //不知道为什么这段代码不起作用，开摆
    //但是：this.addToBot(new ReducePowerAction(this.owner, this.owner, power, 1));这段代码就有用

    public static void addToBot_reducePower(
            final String powerId, final int amount, final AbstractCreature target, final AbstractCreature source) {
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(target, source, powerId, amount));
    }

    public static void addToBot_removeSpecificPower(final AbstractPower power, final AbstractCreature source) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(power.owner, source, power));
    }

    public static void addToBot_removeSpecificPower(
            final String powerId, final AbstractCreature target, final AbstractCreature source) {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(target, source, powerId));
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, BattleCardPlace battleCardPlace, int amount) {
        addToBot_makeTempCardInBattle(card, battleCardPlace, amount, false);
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, BattleCardPlace battleCardPlace, int amount,
                                                     boolean upgrade) {
        if (upgrade)
            card.upgrade();
        final AbstractGameAction gameAction;
        gameAction = getMakeTempCardAction(card, battleCardPlace, amount);
        addToBot(gameAction);

        AutoDoneInstantAction.addToBotAbstract(() -> {
            Optional<AllCardCostModifier> power = AllCardCostModifier.getActivateOne();
            power.ifPresent(AllCardCostModifier::tryUseEffect);
        });
    }

    public static void addToTop_makeTempCardInBattle(AbstractCard card, BattleCardPlace battleCardPlace, int amount,
                                                     boolean upgrade) {
        if (upgrade)
            card.upgrade();
        final AbstractGameAction gameAction;
        gameAction = getMakeTempCardAction(card, battleCardPlace, amount);
        AutoDoneInstantAction.addToTopAbstract(() -> {
            Optional<AllCardCostModifier> power = AllCardCostModifier.getActivateOne();
            power.ifPresent(AllCardCostModifier::tryUseEffect);
        });
        addToTop(gameAction);
    }

    private static AbstractGameAction getMakeTempCardAction(AbstractCard card, BattleCardPlace battleCardPlace, int amount) {
        final AbstractGameAction gameAction;
        switch (battleCardPlace) {
            case Hand:
                gameAction = new MakeTempCardInHandAction(card, amount);
                break;
            case DrawPile:
                gameAction = new MakeTempCardInDrawPileAction(card, amount, true, true);
                break;
            case Discard:
            default:
                gameAction = new MakeTempCardInDiscardAction(card, amount);
                break;
        }
        return gameAction;
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, BattleCardPlace battleCardPlace) {
        addToBot_makeTempCardInBattle(card, battleCardPlace, 1);
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, BattleCardPlace battleCardPlace, boolean upgrade) {
        addToBot_makeTempCardInBattle(card, battleCardPlace, 1, upgrade);
    }


    public static void addEffect(final AbstractGameEffect effect) {
        AbstractDungeon.effectList.add(effect);
    }

    public static void addToBot(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToBottom(action);
    }

    public static void addToTop(AbstractGameAction action) {
        AbstractDungeon.actionManager.addToTop(action);
    }

    public static boolean isNotInBattle() {
        if (currMapNode == null) return true;
        if (AbstractDungeon.getCurrRoom() == null) return true;
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) return true;
        if (AbstractDungeon.getCurrRoom().monsters == null) return true;
        if (AbstractDungeon.getCurrRoom().monsters.monsters == null) return true;
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream().allMatch(AbstractCreature::isDeadOrEscaped);
    }

    public enum BattleCardPlace {
        Hand, DrawPile, Discard
    }

    public interface VoidSupplier {
        void get();
    }

    public interface FunctionReturnSelfType {
        FunctionReturnSelfType get();
    }
}
