package superstitio.cards.general.PowerCard.monsterGirl;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.AbstractTempCard;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;

public class SlimeGirlMode extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(SlimeGirlMode.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = -2;
    private static final int MAGIC = 1;

    public SlimeGirlMode() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        addToBot_applyPower(new SlimeGirlModePower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class SlimeGirlModePower extends EasyBuildAbstractPowerForPowerCard {
        public SlimeGirlModePower(int amount) {
            super(amount);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new SlimeGirlMode();
        }

        @Override
        public void atStartOfTurn() {
            addToBot_applyPower(new PlayerFlightPower(AbstractDungeon.player, this.amount));
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }
    }

    public static class PlayerFlightPower extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(PlayerFlightPower.class);

        public PlayerFlightPower(AbstractPlayer owner, int amount) {
            super(POWER_ID, owner, amount);
        }

        @Override
        public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
            return this.calculateDamageTakenAmount(damage, type);
        }

        @Override
        public void atStartOfTurn() {
            //Nothing Happened
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount) {
            boolean willLive = this.calculateDamageTakenAmount((float) damageAmount, info.type) < (float) this.owner.currentHealth;
            if (info.owner != null && info.type == DamageInfo.DamageType.NORMAL && damageAmount > 0 && willLive) {
                this.flash();
                this.addToBot(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
            }

            return damageAmount;
        }

        private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type) {
            if (type == DamageInfo.DamageType.NORMAL)
                return damage / 2.0F;
            return damage;
        }

        @Override
        public void onRemove() {
            //Nothing Happened
        }

//        public void playApplyPowerSfx() {
//            CardCrawlGame.sound.play("POWER_FLIGHT", 0.05F);
//        }

        @Override
        public void updateDescriptionArgs() {
        }


    }
}
