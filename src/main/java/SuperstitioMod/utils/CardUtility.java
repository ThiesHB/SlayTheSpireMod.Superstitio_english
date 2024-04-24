package SuperstitioMod.utils;

import SuperstitioMod.SuperstitioModSetup;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CardUtility {


    /**
     * @param ThisMod 当为true时， IsCardColorVanilla()被短路
     * @param Vanilla 当为true时， IsCardColorThisMod()被短路
     * @return 输入为true ture时，输出为满足两个条件之一的结果。输入为1true1false时，输出由true项决定。输入为false false时，输出两者都不满足的结果。
     */
    public static AbstractCard getRandomCurseCard(boolean ThisMod, boolean Vanilla) {
        List<AbstractCard> list = getCardsListForMod(ThisMod, Vanilla).stream()
                .filter(card -> card.type == AbstractCard.CardType.STATUS)
                .collect(Collectors.toList());
        return list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1)).makeCopy();
    }

    public static List<AbstractCard> getCardsListForMod(boolean ThisMod, boolean Vanilla) {
        List<AbstractCard> list;
        list = CardLibrary.cards.values().stream()
                .filter(card -> {
                    if (ThisMod && Vanilla)
                        return !(!IsCardColorVanilla(card) && !IsCardColorThisMod(card));
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
        return !card.cardID.contains(":");
    }

    public static boolean IsCardColorThisMod(AbstractCard card) {
        return !card.cardID.contains(SuperstitioModSetup.MakeTextID(""));
    }

    public static ArrayList<AbstractCard> AllCardInBattle() {
        ArrayList<AbstractCard> cards = new ArrayList<>();
        cards.add(AbstractDungeon.player.cardInUse);
        cards.addAll(AbstractDungeon.player.hand.group);
        cards.addAll(AbstractDungeon.player.discardPile.group);
        cards.addAll(AbstractDungeon.player.drawPile.group);
        return cards;
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

    public static void flashIfInHand(AbstractCard card){
        if (AbstractDungeon.player.hand.contains(card))
            card.flash();
    }

}
