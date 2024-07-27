package superstitio.cards.lupa.PowerCard.defend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.lupaOnly.SemenPower;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.powers.barIndepend.HasBarRenderOnCreature_Power;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleIconAndAmount;
import superstitioapi.powers.interfaces.invisible.InvisiblePower_InvisibleTips;
import superstitioapi.utils.PowerUtility;

//排出，然后喝
public class DrinkSemenBeer extends LupaCard {
    public static final String ID = DataManager.MakeTextID(DrinkSemenBeer.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

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
        addToBot_applyPower(new DrinkSemenBeerPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    @SuperstitioImg.NoNeedImg
    public static class DrinkSemenBeerPower extends EasyBuildAbstractPowerForPowerCard implements
            InvisiblePower_InvisibleTips, InvisiblePower_InvisibleIconAndAmount, HasBarRenderOnCreature_Power,
            OnPostApplyThisPower<DrinkSemenBeerPower>, BetterOnApplyPowerPower {
        public static final String POWER_ID = DataManager.MakeTextID(DrinkSemenBeerPower.class);
        //绘制相关
        private int maxAmount;
        private int semenAmount;

        public DrinkSemenBeerPower(final int maxAmount) {
            super(-1, false);
            this.name = powerCard.cardStrings.getEXTENDED_DESCRIPTION()[0];
            this.maxAmount = maxAmount;
            this.semenAmount = 0;
            updateDescription();
        }

        public void CheckFull() {
            if (semenAmount >= maxBarAmount()) {
                this.Full();
            }
        }

        private void Full() {
            this.flash();
            AbstractPower power = this;
            AutoDoneInstantAction.addToBotAbstract(() ->
                    PowerUtility.BubbleMessageHigher(power, false, powerCard.cardStrings.getEXTENDED_DESCRIPTION()[2]));
            this.semenAmount = 0;
            addToBot(new HealAction(this.owner, this.owner, maxBarAmount()));
            AutoDoneInstantAction.addToBotAbstract(() ->
                    PowerUtility.BubbleMessageHigher(power, false, powerCard.cardStrings.getEXTENDED_DESCRIPTION()[3]));
            addToBot_removeSpecificPower(this);
        }

        @Override
        public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (power instanceof DrinkSemenBeerPower) {
                this.maxAmount += ((DrinkSemenBeerPower) power).maxAmount;
            }
        }

        @Override
        public void InitializePostApplyThisPower(DrinkSemenBeerPower addedPower) {
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

        @Override
        public void onVictory() {
            final AbstractPlayer p = AbstractDungeon.player;
            if (p.currentHealth > 0) {
                p.heal(this.semenAmount);
            }
        }

        @Override
        public AbstractPower getSelf() {
            return this;
        }

        @Override
        public float Height() {
            return 20 * Settings.scale;
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
            if ((power instanceof SemenPower) && power.amount > 0) {
                AutoDoneInstantAction.addToBotAbstract(() ->
                        PowerUtility.BubbleMessageHigher(power, false, powerCard.cardStrings.getEXTENDED_DESCRIPTION()[1]));
                SemenPower semenPower = (SemenPower) power;
                this.semenAmount += power.amount * semenPower.getSemenValue();
                CheckFull();
                return true;
            }
            return true;
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new DrinkSemenBeer();
        }
    }
}

