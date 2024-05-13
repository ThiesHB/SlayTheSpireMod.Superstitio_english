package superstitio.cards.lupa.SkillCard;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.BetterOnApplyPowerPower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.powers.AbstractLupaPower;
import superstitio.powers.SexualDamage;
import superstitio.powers.SexualHeat;

@AutoAdd.Ignore
public class Sensitive3000 extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Sensitive3000.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 10;

    public Sensitive3000() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new Sensitive3000Power(AbstractDungeon.player));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class Sensitive3000Power extends AbstractLupaPower implements BetterOnApplyPowerPower {
        public static final String POWER_ID = DataManager.MakeTextID(Sensitive3000Power.class.getSimpleName());
        private static final int SexualHeatRate = 3000;

        public Sensitive3000Power(final AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public void updateDescriptionArgs() {
        }

        @Override
        public boolean betterOnApplyPower(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
            if (abstractPower instanceof SexualHeat)
            {
                abstractPower.amount *= SexualHeatRate;
                addToBot_applyPower(new SexualDamage(this.owner, abstractPower.amount, this.owner));
            }
            return true;
        }
    }
}
