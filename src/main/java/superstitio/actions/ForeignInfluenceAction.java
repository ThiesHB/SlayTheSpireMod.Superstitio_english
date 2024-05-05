//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitio.actions;

import superstitio.utils.CardUtility;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToDiscardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ForeignInfluenceAction extends AbstractContinuallyAction {
    private final boolean upgraded;
    private boolean retrieveCard = false;

    public ForeignInfluenceAction(boolean upgraded) {
        super(ActionType.CARD_MANIPULATION,Settings.ACTION_DUR_FAST);
        this.upgraded = upgraded;
    }

    public static CardRarity makeRandomCardRarity(Random random) {
        int roll = random.random(99);
        AbstractCard.CardRarity cardRarity;
        if (roll < 55) {
            cardRarity = CardRarity.COMMON;
        }
        else if (roll < 85) {
            cardRarity = CardRarity.UNCOMMON;
        }
        else {
            cardRarity = CardRarity.RARE;
        }
        return cardRarity;
    }

    @Override
    protected void RunAction() {
        if (this.retrieveCard) return;
        if (AbstractDungeon.cardRewardScreen.discoveryCard != null) {
            AbstractCard showCard = AbstractDungeon.cardRewardScreen.discoveryCard.makeStatEquivalentCopy();
            if (this.upgraded) {
                showCard.setCostForTurn(0);
            }

            showCard.current_x = -1000.0F * Settings.xScale;
            addToHandOrDiscard(showCard);

            AbstractDungeon.cardRewardScreen.discoveryCard = null;
        }
        this.retrieveCard = true;
    }

    private static void addToHandOrDiscard(AbstractCard disCard) {
        if (AbstractDungeon.player.hand.size() < 10) {
            AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(disCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }
        else {
            AbstractDungeon.effectList.add(new ShowCardAndAddToDiscardEffect(disCard, (float) Settings.WIDTH / 2.0F, (float) Settings.HEIGHT / 2.0F));
        }
    }

    @Override
    protected void ActionSetUp() {
        AbstractDungeon.cardRewardScreen.customCombatOpen(generateCardChoices(
                (List<AbstractCard>) CardLibrary.cards.values(), 3, AbstractCard.CardType.ATTACK
        ), CardRewardScreen.TEXT[1], true);
    }

    public static ArrayList<AbstractCard> generateCardChoices(List<AbstractCard> cardList, int amount,
                                                        AbstractCard.CardType... cardTypes) {
        Random random = AbstractDungeon.cardRandomRng;
        return generateCardChoices(cardList, amount,
                card -> card.rarity == makeRandomCardRarity(random),
                card -> Arrays.stream(cardTypes).collect(Collectors.toList()).contains(card.type));
    }

    @SafeVarargs
    public static ArrayList<AbstractCard> generateCardChoices(List<AbstractCard> cardList, int amount,
                                                              Function<AbstractCard, Boolean>... cardFilters) {
        ArrayList<AbstractCard> cardChoose = new ArrayList<>();

        while (cardChoose.size() != amount) {
            Stream<AbstractCard> cardCanChoose = cardList.stream();
            for (Function<AbstractCard, Boolean> cardFilter : cardFilters)
                cardCanChoose = cardCanChoose.filter(cardFilter::apply);
            AbstractCard tmp = CardUtility.getRandomFromList(cardCanChoose, AbstractDungeon.cardRandomRng);

            if (cardChoose.stream().noneMatch(c -> c.cardID.equals(tmp.cardID)))
                cardChoose.add(tmp.makeCopy());
        }

        return cardChoose;
    }
}
