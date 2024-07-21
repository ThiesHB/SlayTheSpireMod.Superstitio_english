package superstitio.cards.general.SkillCard.gainEnergy;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.DelaySexualHeat;
import superstitio.powers.SexualHeat;

import java.util.Optional;

public class TimeStop extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(TimeStop.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int MAGIC = 1;
    private static final int COST_UPGRADED_NEW = 1;


    public TimeStop() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new TimeStopPower(AbstractDungeon.player, this.magicNumber));
        addToBot_applyPower(new NoDrawPower(player));
    }

    @Override
    public void upgradeAuto() {
        this.upgradeBaseCost(COST_UPGRADED_NEW);
    }

    @SuperstitioImg.NoNeedImg
    public static class TimeStopPower extends AbstractSuperstitioPower implements BetterOnApplyPowerPower {
        public static final String POWER_ID = DataManager.MakeTextID(TimeStopPower.class);
        public static final int sexualReturnRate = 2;

        public TimeStopPower(final AbstractCreature owner, int amount) {
            super(POWER_ID, owner, amount);
            this.loadRegion("time");
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount, sexualReturnRate);
        }


        @Override
        public void atEndOfRound() {
            super.atEndOfRound();
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

        @SpirePatch(clz = AbstractCreature.class, method = "applyEndOfTurnTriggers")
        public static class TimeStopEndTurnPatch {
            @SpirePrefixPatch
            public static SpireReturn<Void> Prefix(AbstractCreature __instance) {
                Optional<TimeStopPower> timeStopPower =
                        __instance.powers.stream().filter(power -> power instanceof TimeStopPower).map(power -> (TimeStopPower) power).findAny();
                if (timeStopPower.isPresent()) {
                    AbstractPower noDraw = __instance.getPower(NoDrawPower.POWER_ID);
                    if (noDraw != null)
                        timeStopPower.get().addToBot_AutoRemoveOne(noDraw);
                    return SpireReturn.Return();
                }
                return SpireReturn.Continue();
            }
        }

        @SpirePatch(clz = MonsterGroup.class, method = "applyEndOfTurnPowers")
        public static class TimeStopEndRoundPatch {
            @SpireInstrumentPatch
            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("atEndOfRound")) {
                            m.replace(String.format("if (!%s.shouldEscapeEndOfRound($0)){$_ = $proceed($$);}",
                                    TimeStopEndRoundPatch.class.getName()));
                        }

                    }
                };
            }

            public static boolean shouldEscapeEndOfRound(AbstractPower power) {
                if (power == null) return false;
                if (power instanceof TimeStopPower) return false;
                if (power.owner == null) return false;
                if (power.owner.powers == null) return false;
//                if (power instanceof NoDrawPower) return false;
                Optional<AbstractPower> timeStopPower =
                        power.owner.powers.stream().filter(power1 -> power1 instanceof TimeStopPower).findAny();
                return timeStopPower.isPresent();
            }
        }
    }
}
