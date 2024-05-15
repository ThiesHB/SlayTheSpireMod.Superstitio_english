package superstitio.cards.lupa.PowerCard.monsterGirl;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.AbstractTempCard;
import superstitio.cards.lupa.PowerCard.ChokeChoker;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;

public class FishGirlMode extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(FishGirlMode.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = -2;
    private static final int MAGIC = 1;

    public FishGirlMode() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        addToBot_applyPower(new FishGirlModePower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class FishGirlModePower extends EasyBuildAbstractPowerForPowerCard {
        public FishGirlModePower(int amount) {
            super(amount);
        }

        @Override
        protected AbstractCard getPowerCard() {
            return new FishGirlMode();
        }

        @Override
        public void atStartOfTurn() {
            addToBot_applyPower(new ChokeChoker.ChokeChokerPower(owner, amount));
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }
    }
}
