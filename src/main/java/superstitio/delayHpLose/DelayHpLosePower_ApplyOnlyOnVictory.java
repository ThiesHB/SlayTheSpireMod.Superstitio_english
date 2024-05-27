package superstitio.delayHpLose;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.utils.PowerUtility;

public class DelayHpLosePower_ApplyOnlyOnVictory extends DelayHpLosePower {
    public static final String POWER_ID = DataManager.MakeTextID(DelayHpLosePower_ApplyOnlyOnVictory.class);

    public DelayHpLosePower_ApplyOnlyOnVictory(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public boolean checkShouldInvisibleTips() {
        return false;
    }

    @Override
    public void onVictory() {
        this.isRemovedForApplyDamage = true;
        PowerUtility.BubbleMessage(this, false, pureName());
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
        AbstractDungeon.player.damage(UnBlockAbleDamage.damageInfo(this, amount));
    }

    @Override
    protected int addToBot_removeDelayHpLoss(int amount, boolean removeOther) {
        int lastAmount = amount - this.amount;
        addToBot_reducePowerToOwner(this.ID, amount);
        return lastAmount;
    }

    @Override
    public boolean showDecreaseAmount() {
        return true;
    }
}
