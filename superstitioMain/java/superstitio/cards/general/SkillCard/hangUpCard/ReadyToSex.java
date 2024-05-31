package superstitio.cards.general.SkillCard.hangUpCard;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;

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

    private void HangUpSpecificCard(AbstractCard card) {
        AbstractCard copyCard = card.makeStatEquivalentCopy();
        copyCard.exhaust = true;
        AbstractCard showUpCard = this.makeStatEquivalentCopy();
        showUpCard.cardsToPreview = copyCard;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            AbstractDungeon.player.hand.removeCard(card);
            new CardOrb_WaitCardTrigger(card, AbstractDungeon.player.discardPile, magicNumber, (orb, usedcard) -> {
                addToBot(new NewQueueCardAction(copyCard, true, false, true));
            })
                    .setNotEvokeOnEndOfTurn()
                    .setShowCard(showUpCard)
                    .addToBot_HangCard();
        });
    }

    @Override
    public void upgradeAuto() {
    }
}
