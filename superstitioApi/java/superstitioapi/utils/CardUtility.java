package superstitioapi.utils;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import superstitioapi.DataUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CardUtility {


    protected static final Color AgressiveColor = Color.RED.cpy();
    protected static final Color HospitableColor = Color.GREEN.cpy();

    /**
     * @param ThisMod 当为true时， IsCardColorVanilla()被短路
     * @param Vanilla 当为true时， IsCardColorThisMod()被短路
     * @return 输入为true ture时，输出为满足两个条件之一的结果。输入为1true1false时，输出由true项决定。输入为false false时，输出两者都不满足的结果。
     */
    public static AbstractCard getRandomStatusCard(boolean ThisMod, boolean Vanilla) {
        List<AbstractCard> list = getCardsListForMod(ThisMod, Vanilla).stream()
                .filter(card -> card.type == AbstractCard.CardType.STATUS)
                .collect(Collectors.toList());
        return ListUtility.getRandomFromList(list, AbstractDungeon.cardRandomRng).makeCopy();
    }

    public static AbstractCreature getSelfOrEnemyTarget(AbstractCard card, AbstractMonster monster) {
        if (card.target != SelfOrEnemyTargeting.SELF_OR_ENEMY) {
            return CreatureUtility.getRandomMonsterSafe();
        }
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(card);
        if (target != null) return target;
        if (monster != null) return monster;
        return AbstractDungeon.player;
    }

    public static List<AbstractCard> getCardsListForMod(boolean ThisMod, boolean Vanilla) {
        List<AbstractCard> list;
        list = CardLibrary.cards.values().stream()
                .filter(card -> {
                    if (ThisMod && Vanilla)
                        return IsCardColorVanilla(card) || IsCardColorThisMod(card);
                    else if (ThisMod)
                        return !IsCardColorVanilla(card) && IsCardColorThisMod(card);
                    else if (Vanilla)
                        return IsCardColorVanilla(card) && !IsCardColorThisMod(card);
                    else
                        return !IsCardColorVanilla(card) && !IsCardColorThisMod(card);
                })
                .collect(Collectors.toList());
        return list;
    }

    public static boolean IsCardColorVanilla(AbstractCard card) {
        return card.getClass().getPackage().getName().contains("com.megacrit.cardcrawl");
//        return !card.cardID.contains(":");
    }

    public static boolean IsCardColorThisMod(AbstractCard card) {
        return !card.cardID.contains(DataUtility.MakeTextID(""));
    }

    public static ArrayList<AbstractCard> AllCardInBattle() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(AbstractDungeon.player.cardInUse);
        cards.addAll(AbstractDungeon.player.hand.group);
        cards.addAll(AbstractDungeon.player.discardPile.group);
        cards.addAll(AbstractDungeon.player.drawPile.group);
        return cards;
    }

    public static CardGroup[] AllCardGroupInBattle() {
        return new CardGroup[]{
                AbstractDungeon.player.hand,
                AbstractDungeon.player.drawPile,
                AbstractDungeon.player.discardPile,
                AbstractDungeon.player.exhaustPile};
    }

    public static ArrayList<AbstractCard> AllCardInBattle_ButWithoutCardInUse() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.addAll(AbstractDungeon.player.hand.group);
        cards.addAll(AbstractDungeon.player.discardPile.group);
        cards.addAll(AbstractDungeon.player.drawPile.group);
        return cards;
    }

//    public static AbstractCard makeStatEquivalentCopy(final AbstractCard c) {
//        final AbstractCard card = c.makeStatEquivalentCopy();
//        card.retain = c.retain;
//        card.selfRetain = c.selfRetain;
//        card.purgeOnUse = c.purgeOnUse;
//        card.isEthereal = c.isEthereal;
//        card.exhaust = c.exhaust;
//        card.glowColor = c.glowColor;
//        card.rawDescription = c.rawDescription;
//        card.cardsToPreview = c.cardsToPreview;
//        card.initializeDescription();
//        return card;
//    }

    public static void flashIfInHand(AbstractCard card) {
        if (AbstractDungeon.player.hand.contains(card))
            card.flash();
    }

    public static Color getColorFormCard(AbstractCard card) {
        switch (card.target) {
            case ENEMY:
            case ALL_ENEMY:
                return AgressiveColor.cpy();
            case SELF:
                return HospitableColor.cpy();
            case NONE:
            case ALL:
            case SELF_AND_ENEMY:
            default:
                return Color.PURPLE.cpy();
        }
    }

    public static Color getColorFormCard(AbstractCard.CardTarget target) {
        switch (target) {
            case ENEMY:
            case ALL_ENEMY:
                return AgressiveColor.cpy();
            case SELF:
                return HospitableColor.cpy();
            case NONE:
            case ALL:
            case SELF_AND_ENEMY:
            default:
                return Color.PURPLE.cpy();
        }
    }

    private static boolean hasEnoughEnergyOrTurnEnd(AbstractCard card) {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            return false;
        }
        return EnergyPanel.totalCount >= card.costForTurn || card.freeToPlay() || card.isInAutoplay;
    }

    public static boolean canNotUseWithoutEnvironment(AbstractCard card) {
        if (card.canUse(AbstractDungeon.player, null)) return true;
        //不是因为能量不够或者对象不对而无法打出
//            if (!(card.cardPlayable(null) && hasEnoughEnergyOrTurnEnd(card))) return;
        //似乎检测对象是否正确会导致攻击牌出问题，所以只加了这个检测，但是可能会引发其他错误
        return !(hasEnoughEnergyOrTurnEnd(card));
    }
}
