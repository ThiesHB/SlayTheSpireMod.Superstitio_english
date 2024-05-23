package superstitio.cards.maso.BaseCard;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.actions.ChoseCardFromHandCardSelectScreen;
import superstitio.cards.lupa.LupaCard;
import superstitio.cards.maso.MasoCard;

public class FistIn extends MasoCard {
    public static final String ID = DataManager.MakeTextID(FistIn.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 5;
    private static final int UPGRADE_MAGIC = -2;

    public FistIn() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        new ChoseCardFromHandCardSelectScreen(card -> AutoDoneInstantAction.newAutoDone(() -> {
            addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, card.costForTurn * magicNumber));
            addToBot(new NewQueueCardAction(card, true, false, true));
        }))
                .setRetainFilter()
                .setWindowText(cardStrings.getEXTENDED_DESCRIPTION()[0])
                .addToBot();
    }

    @Override
    public void upgradeAuto() {
    }
}

