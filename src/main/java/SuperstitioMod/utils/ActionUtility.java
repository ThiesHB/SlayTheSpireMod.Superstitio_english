package SuperstitioMod.utils;

import SuperstitioMod.actions.AutoDoneAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.powers.AllCardCostModifier;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Optional;

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

    public static void addToBot_makeTempCardInBattle(AbstractCard card, AbstractLupaCard.BattleCardPlace battleCardPlace, int amount, boolean upgrade) {
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

        AutoDoneAction.addToBotAbstract(() -> {
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
}
