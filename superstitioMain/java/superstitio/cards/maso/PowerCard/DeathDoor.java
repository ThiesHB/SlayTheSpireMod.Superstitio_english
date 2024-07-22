package superstitio.cards.maso.PowerCard;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;


public class DeathDoor extends MasoCard {
    public static final String ID = DataManager.MakeTextID(DeathDoor.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public DeathDoor() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new DeathDoorPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class DeathDoorPower extends EasyBuildAbstractPowerForPowerCard implements OnPlayerDeathPower {

        public DeathDoorPower(int amount) {
            super(amount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        @Override
        public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
            if (this.amount == 0) return true;
            this.flash();
            addToBot(new HealAction(this.owner, this.owner, 1));
            addToBot_applyPower(new AtDeathDoor(this.owner));
            addToBot_AutoRemoveOne(this);
            return false;
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new DeathDoor();
        }
    }

    public static class AtDeathDoor extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(AtDeathDoor.class);

        public AtDeathDoor(final AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public void updateDescriptionArgs() {
        }

        @Override
        public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
            this.flash();
            if (info.type == DamageInfo.DamageType.HP_LOSS)
                return 0;
            if (info.type == DamageInfo.DamageType.THORNS)
                return 0;
            return super.onAttackedToChangeDamage(info, damageAmount);
        }
    }
}

