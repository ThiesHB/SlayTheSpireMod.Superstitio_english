package superstitio.cards.colorless;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.actions.ChoseCardFromGridSelectWindowAction;
import superstitio.cards.SuperstitioCard;
import superstitio.hangUpCard.CardOrb_WaitCardTrigger;
import superstitio.hangUpCard.HangUpCardGroup;

public class FindCardAndHang extends SuperstitioCard {
    public static final String ID = DataManager.MakeTextID(FindCardAndHang.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = -4;
    private static final int CHOSE_CARD = 10;
    private static final int WAIT_TIME = 3;

    public FindCardAndHang() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, CardColor.COLORLESS, "special");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new ExhaustMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ChoseCardFromGridSelectWindowAction(AbstractDungeon.player.discardPile,
                this::HangUpSpecificCard)
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0], CHOSE_CARD))
                .setChoseAmount(CHOSE_CARD)
                .setAnyNumber(true)
        );
    }

    private void HangUpSpecificCard(AbstractCard card) {
        AbstractCard copyCard = card.makeStatEquivalentCopy();
        copyCard.exhaust = true;
        AbstractCard showUpCard = this.makeStatEquivalentCopy();
        showUpCard.cardsToPreview = copyCard;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            AbstractDungeon.player.hand.removeCard(card);
            HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                    new CardOrb_WaitCardTrigger(card, AbstractDungeon.player.hand, (orb, usedcard) -> {
                    }, magicNumber)
                            .setNotEvokeOnEndOfTurn()
                            .setShowCard(showUpCard)
            );
        });
    }

    @Override
    public void upgradeAuto() {
    }
}
