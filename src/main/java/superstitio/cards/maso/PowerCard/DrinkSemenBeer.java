package superstitio.cards.maso.PowerCard;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.AbstractLupaPower;
import superstitio.powers.InsideSemen;
import superstitio.powers.OutsideSemen;
import superstitio.powers.patchAndInterface.barIndepend.HasBarRenderOnCreature_Power;
import superstitio.powers.patchAndInterface.interfaces.OnPostApplyThisPower;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitio.powers.patchAndInterface.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitio.utils.PowerUtility;

//排出，然后喝
public class DrinkSemenBeer extends MasoCard {
    public static final String ID = DataManager.MakeTextID(DrinkSemenBeer.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = 5;

    public DrinkSemenBeer() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new BeerCupSemen(AbstractDungeon.player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class BeerCupSemen extends AbstractLupaPower implements
            InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_Power,
            OnPostApplyThisPower, BetterOnApplyPowerPower {
        public static final String POWER_ID = DataManager.MakeTextID(BeerCupSemen.class);
        //绘制相关
        private int maxAmount;
        private int semenAmount;

        public BeerCupSemen(final AbstractCreature owner, final int maxAmount) {
            super(POWER_ID, owner, -1, PowerType.BUFF, false);
            this.maxAmount = maxAmount;
            semenAmount = 0;
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (power instanceof BeerCupSemen) {
                this.maxAmount += ((BeerCupSemen) power).maxAmount;
            }
        }

        @Override
        public void InitializePostApplyThisPower() {
            CheckFull();
            updateDescription();
        }

        @Override
        public int getAmountForDraw() {
            return this.semenAmount;
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(maxBarAmount());
        }


        public void CheckFull() {
            if (semenAmount >= maxBarAmount()) {
                this.Full();
            }
        }

        @Override
        public void onVictory() {
            final AbstractPlayer p = AbstractDungeon.player;
            if (p.currentHealth > 0) {
                p.heal(this.semenAmount);
            }
        }

        private void Full() {
            this.flash();
            AbstractPower power = this;
            AutoDoneInstantAction.addToBotAbstract(() ->
                    PowerUtility.BubbleMessageHigher(power, false, powerStrings.getDESCRIPTIONS()[2]));
            this.semenAmount = 0;
            addToBot(new HealAction(this.owner, this.owner, maxBarAmount()));
            AutoDoneInstantAction.addToBotAbstract(() ->
                    PowerUtility.BubbleMessageHigher(power, false, powerStrings.getDESCRIPTIONS()[3]));
            addToBot_removeSpecificPower(this);
        }


        @Override
        public AbstractPower getSelf() {
            return this;
        }

        @Override
        public String uuidOfSelf() {
            return this.ID;
        }

        @Override
        public float Height() {
            return 160 * Settings.scale;
        }

        @Override
        public Color setupBarOrginColor() {
            return Color.WHITE.cpy();
        }

        @Override
        public int maxBarAmount() {
            return maxAmount;
        }

        @Override
        public boolean betterOnApplyPower(AbstractPower power, AbstractCreature creature, AbstractCreature creature1) {
            if ((power instanceof InsideSemen || power instanceof OutsideSemen) && power.amount > 0) {
                AutoDoneInstantAction.addToBotAbstract(() ->
                        PowerUtility.BubbleMessageHigher(power, false, powerStrings.getDESCRIPTIONS()[1]));
                this.semenAmount += power.amount;
                CheckFull();
                return false;
            }
            return true;
        }
    }
}

