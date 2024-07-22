package superstitio.cards.maso.PowerCard;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onSquirt;


public class Sensitive3000 extends MasoCard {
    public static final String ID = DataManager.MakeTextID(Sensitive3000.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int COST_UPGRADED_NEW = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 0;

    public Sensitive3000() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new JuicyPussy_TempPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW);
    }

    public static class JuicyPussy_TempPower extends EasyBuildAbstractPowerForPowerCard implements OnOrgasm_onSquirt {
        public JuicyPussy_TempPower(int amount) {
            super(amount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }

        @Override
        public void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
            this.flash();
            this.addToBot(new GainEnergyAction(this.amount));
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new Sensitive3000();
        }
    }
}

