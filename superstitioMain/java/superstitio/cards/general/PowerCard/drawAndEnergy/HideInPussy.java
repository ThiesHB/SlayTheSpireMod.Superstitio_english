package superstitio.cards.general.PowerCard.drawAndEnergy;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen;


public class HideInPussy extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(HideInPussy.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int COST_UPGRADED_NEW = 1;
    private static final int MAGIC = 1;

    public HideInPussy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new HideInPussyPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW);
    }

    public static class HideInPussyPower extends EasyBuildAbstractPowerForPowerCard {
        public HideInPussyPower(int amount) {
            super(amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            if (!isPlayer || AbstractDungeon.player.hand.isEmpty()) return;
            new ChoseCardFromHandCardSelectScreen(card -> {
                card.freeToPlayOnce = true;
                card.retain = true;
            })
                    .setWindowText(String.format(powerCard.cardStrings.getEXTENDED_DESCRIPTION()[0], this.amount))
                    .setChoiceAmount(this.amount)
                    .setAnyNumber(true)
                    .setCanPickZero(true)
                    .addToBot();
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new HideInPussy();
        }
    }
}

