package superstitio.powers;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import superstitio.DataManager;

public class SexPlateArmorPower extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(SexPlateArmorPower.class.getSimpleName());

    public SexPlateArmorPower(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_PLATED", 0.05F);
    }

    public void wasHPLost(DamageInfo info, int damageAmount) {
        if (info.owner != null && info.owner != this.owner && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount > 0) {
            this.flash();
            addToBot_reducePowerToOwner(SexPlateArmorPower.POWER_ID, 1);
        }
    }

    public void atEndOfTurnPreEndTurnCards(boolean isPlayer) {
        this.flash();
        this.addToBot(new GainBlockAction(this.owner, this.owner, this.amount));
    }
}
