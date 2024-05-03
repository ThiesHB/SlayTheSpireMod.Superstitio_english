package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SexMark_Inside extends SexMark {
    public static final String POWER_ID = DataManager.MakeTextID(SexMark_Inside.class.getSimpleName());
    public static final int MARKNeeded = 5;

    public static final int AOEDamageRate = 2;

    public SexMark_Inside(final AbstractCreature owner, final String sexName) {
        super(POWER_ID, owner, sexName);
    }

    @Override
    protected void Trigger() {
        super.Trigger();
        this.addToBot(new DamageAllEnemiesAction(
                AbstractDungeon.player,
                AOEDamageRate * FindJobAndFuckCard(),
                DamageInfo.DamageType.HP_LOSS,
                AbstractGameAction.AttackEffect.LIGHTNING));
    }

    @Override
    protected float Height() {
        return this.owner.hb.cY + SexMark.BAR_RADIUS + SexMark.BAR_RADIUS + SexMark.BAR_Blank;
    }

    @Override
    public void updateDescriptionArgs() {
        StringBuilder names = new StringBuilder();
        for (String sexName : this.sexNames) {
            names.append(sexName);
        }
        setDescriptionArgs(names, MARKNeeded, AOEDamageRate * FindJobAndFuckCard());
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
    }
}
