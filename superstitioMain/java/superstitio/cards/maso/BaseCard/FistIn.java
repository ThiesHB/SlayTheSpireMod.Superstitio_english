package superstitio.cards.maso.BaseCard;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import superstitio.DataManager;
import superstitio.cards.maso.MasoCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen;

public class FistIn extends MasoCard {
    public static final String ID = DataManager.MakeTextID(FistIn.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int MAGIC = 5;
    private static final int UPGRADE_MAGIC = -2;

    public FistIn() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        new ChoseCardFromHandCardSelectScreen(card -> AutoDoneInstantAction.addToBotAbstract(() -> {
            int costSave;
            if (card.costForTurn >= 0)
                costSave = card.costForTurn;
            else if (card.costForTurn == -1)
                costSave = EnergyPanel.getCurrentEnergy();
            else
                costSave = 0;

            addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, costSave * magicNumber));
            if (card.target != CardTarget.ENEMY || monster == null)
                addToBot(new NewQueueCardAction(card, true, false, true));
            else
                addToBot(new NewQueueCardAction(card, monster, false, true));
        }))
                .setRetainFilter()
                .setWindowText(cardStrings.getEXTENDED_DESCRIPTION()[0])
                .addToBot();
    }

    @Override
    public void upgradeAuto() {
    }
}

