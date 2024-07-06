package superstitio.cards.colorless;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.actions.ChoseCardFromGridSelectWindowAction;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;

public class FindCardAndHang extends SuperstitioCard {
    public static final String ID = DataManager.MakeTextID(FindCardAndHang.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = -4;
    private static final int CHOSE_CARD = 10;
    private static final int WAIT_TIME = 3;

    public FindCardAndHang() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, CardColor.COLORLESS, "special");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new ExhaustMod());
    }

    private void HangUpSpecificCard(AbstractCard card) {
        AbstractCard copyCard = card.makeStatEquivalentCopy();
        copyCard.exhaust = true;
        AbstractCard showUpCard = this.makeStatEquivalentCopy();
        showUpCard.cardsToPreview = copyCard;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            AbstractDungeon.player.drawPile.removeCard(card);
            new CardOrb_WaitCardTrigger(card, AbstractDungeon.player.hand, magicNumber, (orb, usedcard) -> {
            })
                    .setShowCard(showUpCard)
                    .addToBot_HangCard();
        });
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ChoseCardFromGridSelectWindowAction(AbstractDungeon.player.drawPile,
                this::HangUpSpecificCard)
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0], CHOSE_CARD))
                .setChoseAmount(CHOSE_CARD)
                .setAnyNumber(true)
        );
    }

    @Override
    public void upgradeAuto() {
    }
}
