package superstitio.cards.maso.PowerCard;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;

//造成攻击伤害或失去生命时，获得 !M! 临时力量
public class SadismForm extends MasoCard {
    public static final String ID = DataManager.MakeTextID(SadismForm.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;

    public SadismForm() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new SadismFormPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class SadismFormPower extends EasyBuildAbstractPowerForPowerCard {

        public SadismFormPower(int amount) {
            super(amount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount) {
            if (info.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) return;
            if (damageAmount > 0) {
                addToBot_applyPower(new GetTempStrengthNextTurn(this.owner, this.amount));
            }
        }

        @Override
        public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
            if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
                addToBot_applyPower(new GetTempStrengthNextTurn(this.owner, this.amount));
            }
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new SadismForm();
        }
    }

    public static class GetTempStrengthNextTurn extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(GetTempStrengthNextTurn.class);

        public GetTempStrengthNextTurn(final AbstractCreature owner, int amount) {
            super(POWER_ID, owner, amount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        @Override
        public void atStartOfTurn() {
            addToBot_applyPower(new StrengthPower(this.owner, this.amount));
//            addToBot_applyPower(new LoseStrengthPower(this.owner, this.amount));
            addToBot_removeSpecificPower(this);
        }
    }

}

