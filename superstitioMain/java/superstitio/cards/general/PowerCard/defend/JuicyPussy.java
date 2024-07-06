package superstitio.cards.general.PowerCard.defend;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.GeneralCard;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onSquirt;


public class JuicyPussy extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(JuicyPussy.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;

    public JuicyPussy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new JuicyPussyPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class JuicyPussyPower extends EasyBuildAbstractPowerForPowerCard implements OnOrgasm_onSquirt {
        public JuicyPussyPower(int amount) {
            super(amount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }

        @Override
        public void onSquirt(SexualHeat SexualHeatPower, AbstractCard card) {
            this.flash();
            DelayHpLosePower.addToBot_removePower(amount, AbstractDungeon.player, true);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new JuicyPussy();
        }
    }
}

