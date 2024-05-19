package superstitio.delayHpLose;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.DataManager;
import superstitio.utils.PowerUtility;

public class DelayHpLosePower_ApplyOnlyOnVictory extends DelayHpLosePower {
    private static final String POWER_ID = DataManager.MakeTextID(DelayHpLosePower_ApplyOnlyOnVictory.class);

    public DelayHpLosePower_ApplyOnlyOnVictory(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void onVictory() {
        this.isRemovedForApplyDamage = true;
        PowerUtility.BubbleMessage(this, false, pureName());
        CardCrawlGame.sound.play("POWER_TIME_WARP", 0.05f);
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, this.amount, DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType));
    }
}
