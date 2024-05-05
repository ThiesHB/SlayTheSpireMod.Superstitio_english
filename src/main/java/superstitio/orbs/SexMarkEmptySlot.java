package superstitio.orbs;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import superstitio.DataManager;

public class SexMarkEmptySlot extends EmptyOrbSlot {
    public static final String ORB_ID = DataManager.MakeTextID(SexMarkEmptySlot.class.getSimpleName());

    public SexMarkEmptySlot() {
        this(AbstractDungeon.player.drawX + AbstractDungeon.player.hb_x,
                AbstractDungeon.player.drawY + AbstractDungeon.player.hb_y + AbstractDungeon.player.hb_h / 2.0F);
    }

    public SexMarkEmptySlot(float x, float y) {
        super(x, y);
        this.ID = ORB_ID;
        this.name = "StringWIP: 空余球位";
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.description = "StringWIP: 空余球位";
    }

    @Override
    public AbstractOrb makeCopy() {
        return new SexMarkEmptySlot();
    }
}
