package superstitio.utils;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.powers.AllCardCostModifier;

import java.util.ArrayList;
import java.util.Optional;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.currMapNode;

public class ActionUtility {

    public static void addToBot_applyPower(final AbstractPower power) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(power.owner, AbstractDungeon.player, power));
    }

    public static void addToBot_applyPower(final AbstractPower power, AbstractCreature source) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(power.owner, source, power));
    }

    public static void addToTop_applyPower(final AbstractPower power) {
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(power.owner, AbstractDungeon.player, power));
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, AbstractLupaCard.BattleCardPlace battleCardPlace, int amount) {
        addToBot_makeTempCardInBattle(card, battleCardPlace, amount, false);
    }

    public static AbstractMonster getRandomMonsterSafe() {
        final AbstractMonster m = AbstractDungeon.getRandomMonster();
        if (m != null && !m.isDeadOrEscaped() && !m.isDead) {
            return m;
        }
        return null;
    }

    public static AbstractMonster getRandomMonsterWithoutRng() {
        final AbstractMonster m = currMapNode.room.monsters.getRandomMonster((AbstractMonster) null, true, new Random());
        if (m != null && !m.isDeadOrEscaped() && !m.isDead) {
            return m;
        }
        return null;
    }

    public static ArrayList<AbstractMonster> getMonsters() {
        return AbstractDungeon.getMonsters().monsters;
    }

    public static AbstractMonster[] getAllAliveMonsters() {
        return getMonsters().stream().filter(ActionUtility::isAlive).toArray(AbstractMonster[]::new);
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, AbstractLupaCard.BattleCardPlace battleCardPlace, int amount,
                                                     boolean upgrade) {
        if (upgrade)
            card.upgrade();
        switch (battleCardPlace) {
            case Hand:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card, amount));
                break;
            case DrawPile:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(card, amount, true, true));
                break;
            case Discard:
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card, amount));
                break;
        }

        AutoDoneInstantAction.addToBotAbstract(() -> {
            Optional<AllCardCostModifier> power = AllCardCostModifier.getActivateOne();
            power.ifPresent(AllCardCostModifier::tryUseEffect);
        });
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, AbstractLupaCard.BattleCardPlace battleCardPlace) {
        addToBot_makeTempCardInBattle(card, battleCardPlace, 1);
    }

    public static void addToBot_makeTempCardInBattle(AbstractCard card, AbstractLupaCard.BattleCardPlace battleCardPlace, boolean upgrade) {
        addToBot_makeTempCardInBattle(card, battleCardPlace, 1, upgrade);
    }

    public static boolean isAlive(final AbstractCreature c) {
        return c != null && !c.isDeadOrEscaped() && !c.isDead;
    }

    public static void addEffect(final AbstractGameEffect effect) {
        AbstractDungeon.effectList.add(effect);
    }

    public interface VoidSupplier {
        void get();
    }
}
