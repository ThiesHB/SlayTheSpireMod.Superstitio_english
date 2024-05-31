package superstitioapi.cards.patch;

import com.megacrit.cardcrawl.cards.CardGroup;

public interface GoSomewhereElseAfterUse {

//    CardGroup getCardGroupAfterUse();

    void afterInterruptMoveToCardGroup(CardGroup cardGroup);

}
