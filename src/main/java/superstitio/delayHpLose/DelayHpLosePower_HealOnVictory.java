package superstitio.delayHpLose;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.utils.PowerUtility;

public class DelayHpLosePower_HealOnVictory extends DelayHpLosePower {
    private static final String POWER_ID = DataManager.MakeTextID(DelayHpLosePower_HealOnVictory.class);

    public DelayHpLosePower_HealOnVictory(AbstractCreature owner, int amount) {
        super(owner, amount);
        this.powerStrings = getPowerStringsWithSFW(POWER_ID);
//        this.name = powerStrings.getRightVersion().NAME;
//        this.ID = POWER_ID;
        this.updateDescription();
    }

    @Override
    public void onVictory() {
        this.isRemoveByTimePass = true;
        AutoDoneInstantAction.addToTopAbstract(() -> {
            AbstractDungeon.effectsQueue.add(new HealEffect(owner.hb.cX - owner.animX, owner.hb.cY, amount));
            playRemoveEffect();
        });
        addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this));
    }
}
