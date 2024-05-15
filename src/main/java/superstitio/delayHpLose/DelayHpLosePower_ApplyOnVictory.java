package superstitio.delayHpLose;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.DamageActionMaker;
import superstitio.utils.PowerUtility;

public class DelayHpLosePower_ApplyOnVictory extends DelayHpLosePower {
    private static final String POWER_ID = DataManager.MakeTextID(DelayHpLosePower_ApplyOnVictory.class);

    public DelayHpLosePower_ApplyOnVictory(AbstractCreature owner, int amount) {
        super(owner, amount);
        this.powerStrings = getPowerStringsWithSFW(POWER_ID);
//        this.name = powerStrings.getRightVersion().NAME;
//        this.ID = POWER_ID;
        this.updateDescription();
    }

    @Override
    public void onVictory() {
        this.isRemoveByTimePass = true;
        DamageActionMaker.maker(this.amount, this.owner)
                .setDamageModifier(this, new UnBlockAbleDamage())
                .setEffect(AbstractGameAction.AttackEffect.LIGHTNING)
                .setDamageType(DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType)
                .addToTop();
        AutoDoneInstantAction.addToTopAbstract(() -> {
            PowerUtility.BubbleMessage(this, false, pureName());
            CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
        });
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
