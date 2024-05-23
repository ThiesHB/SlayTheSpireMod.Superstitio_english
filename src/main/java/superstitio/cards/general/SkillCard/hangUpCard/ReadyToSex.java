package superstitio.cards.general.SkillCard.hangUpCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.actions.ChoseCardFromHandCardSelectScreen;
import superstitio.cards.general.GeneralCard;
import superstitio.hangUpCard.CardOrb_WaitCardTrigger;
import superstitio.hangUpCard.HangUpCardGroup;

public class ReadyToSex extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(ReadyToSex.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 6;
    private static final int UPGRADE_MAGIC = -2;
    private static final int CHOSE_CARD = 1;
    private static final int WAIT_TIME = 3;

    public ReadyToSex() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ChoseCardFromHandCardSelectScreen(
                this::HangUpSpecificCard)
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0], CHOSE_CARD))
                .setChoiceAmount(CHOSE_CARD)
        );
    }

    private AbstractGameAction HangUpSpecificCard(AbstractCard card) {
        return new AutoDoneInstantAction() {
            @Override
            public void autoDoneUpdate() {
                AbstractDungeon.player.hand.removeCard(card);
                AbstractCard copyCard = card.makeStatEquivalentCopy();
                copyCard.exhaust = true;
                HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                        new CardOrb_WaitCardTrigger(card, AbstractDungeon.player.discardPile, (orb, usedcard) -> {
                            addToBot(new NewQueueCardAction(copyCard, true, false, true));
                        }, magicNumber)
                                .setNotEvokeOnEndOfTurn()
                );
            }
        };
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
