package superstitio.cards.general.SkillCard.hangUpCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;

public class RideDildoBike extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(RideDildoBike.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;
    private static final int WAIT_TIME = 5;

    public RideDildoBike() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        //抽卡数量
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    private void HangUpSpecificCard(AbstractCard card) {
        AbstractCard copyCard = card.makeStatEquivalentCopy();
//        copyCard.exhaust = true;
        AbstractCard showUpCard = card.makeStatEquivalentCopy();
        showUpCard.cardsToPreview = copyCard;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            AbstractDungeon.player.hand.removeCard(card);
            new CardOrb_WaitCardTrigger(card, AbstractDungeon.player.discardPile, WAIT_TIME, (orb, usedcard) -> {
                AutoDoneInstantAction.addToBotAbstract(() -> orb.cardHolder.moveToHand(card));
            })
                    .setDiscardOnEndOfTurn()
                    .setShowCard(showUpCard)
                    .setCardRawDescriptionWillShow(cardStrings.getEXTENDED_DESCRIPTION(1))
                    .addToBot_HangCard();
        });
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        new ChoseCardFromHandCardSelectScreen(card -> {
                    HangUpSpecificCard(card);
                    addToBot_drawCards();
                })
                .setWindowText(String.format(this.cardStrings.getEXTENDED_DESCRIPTION(0), this.magicNumber))
                .setChoiceAmount(this.magicNumber)
                .setAnyNumber(true)
                .setCanPickZero(true)
                .addToBot();
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(WAIT_TIME);
    }

    @Override
    public void upgradeAuto() {
    }
}
