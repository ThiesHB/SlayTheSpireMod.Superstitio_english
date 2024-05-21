package superstitio.cards.general.SkillCard.gainEnergy;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.DelaySexualHeat;
import superstitio.powers.SexualHeat;

public class TimeStop extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(TimeStop.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;

    private static final int COST_UPGRADED_NEW = 2;


    public TimeStop() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new TimeStopPower(player, 1)));
        addToBot_applyPower(new NoDrawPower(player));
    }

    @Override
    public void upgradeAuto() {
        this.upgradeBaseCost(COST_UPGRADED_NEW);
    }

    public static class TimeStopPower extends AbstractSuperstitioPower implements BetterOnApplyPowerPower {
        public static final String POWER_ID = DataManager.MakeTextID(TimeStopPower.class);
        public static final int sexualReturnRate = 2;

        public TimeStopPower(final AbstractCreature owner, int amount) {
            super(POWER_ID, owner, amount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount, sexualReturnRate);
        }


        @Override
        public void atEndOfRound() {
            //if (!isPlayer) return;
            addToBot_AutoRemoveOne(this);
        }

        @Override
        public boolean betterOnApplyPower(AbstractPower power, AbstractCreature creature, AbstractCreature creature1) {
            if (power instanceof SexualHeat && power.amount > 0) {
                this.flash();
                this.addToBot(new ApplyPowerAction(this.owner, this.owner, new DelaySexualHeat(this.owner, power.amount * sexualReturnRate)));
                return false;
            }
            return true;
        }
    }
}
