package superstitioapi.utils;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import superstitioapi.DataUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        return getRandomFromList(list, AbstractDungeon.cardRandomRng).makeCopy();
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

    public static <T> T getRandomFromList(List<T> list, Random random) {
        return list.get(random.random(list.size() - 1));
    }

    public static <T> T getRandomFromList(Stream<T> stream, Random random) {
        List<T> list = stream.collect(Collectors.toList());
        return getRandomFromList(list, random);
    }

    public static <T> T getRandomFromList(T[] list, Random random) {
        return list[random.random(list.length - 1)];
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

    public static boolean isNotInBattle() {
        if (AbstractDungeon.currMapNode == null) return true;
        if (AbstractDungeon.getCurrRoom() == null) return true;
        if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) return true;
        if (AbstractDungeon.getCurrRoom().monsters == null) return true;
        if (AbstractDungeon.getCurrRoom().monsters.monsters == null) return true;
        return AbstractDungeon.getCurrRoom().monsters.monsters.stream().allMatch(AbstractCreature::isDeadOrEscaped);
    }
}
