package superstitioapi.relic;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.relics.AbstractRelic;

/**
 * 可以用这个荒疫来携带一个遗物，这个遗物可以正常被查看和点击
 */
public abstract class BlightWithRelic extends AbstractBlight {

    public AbstractRelic relic;
    private boolean isInit = false;

    public BlightWithRelic(String id) {
        super(id, "BlightWithRelic", "Only a Relic carrier, do not spawn.", "maze.png", true);
        relic = makeRelic();
    }

    @Override
    public void setIncrement(int newInc) {
        super.setIncrement(newInc);
//        AbstractDungeon.getCurrRoom().spawnBlightAndObtain()
    }

    public abstract AbstractRelic makeRelic();

    @Override
    public void update() {
//        super.update();
//        if (!this.isDone) return;
        if (!isInit) {
            initRelic();
            isInit = true;
            if (Loader.isModLoadedOrSideloaded("VUPShionMod")) {
                isInit = false;
            }
        }

        this.hb.update();
        this.relic.update();
//        this.relic.hb.move(this.hb.cX,this.hb.cY);
//        this.relic.currentX = this.currentX;
//        this.relic.currentY = this.currentY;
//        this.hb.update();
//        if (this.hb.hovered && AbstractDungeon.topPanel.potionUi.isHidden) {
//            this.scale = Settings.scale * 1.25F;
//            CardCrawlGame.cursor.changeType(GameCursor.CursorType.INSPECT);
//        }
//        else {
//            this.scale = MathHelper.scaleLerpSnap(this.scale, Settings.scale);
//        }
////        this.relic.updateRelicPopupClick();
//        ReflectionHacks.RMethod relic_updateRelicPopupClick =
//                ReflectionHacks.privateMethod(AbstractRelic.class, "updateRelicPopupClick");
//        relic_updateRelicPopupClick.invoke(this.relic);
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        this.relic.renderTip(sb);
    }

    @Override
    public void obtain() {
        super.obtain();
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip) {
        super.instantObtain(p, slot, callOnEquip);
    }

    @Override
    public void bossObtainLogic() {
        super.bossObtainLogic();
    }

    @Override
    public void spawn(float x, float y) {
        super.spawn(x, y);
    }

    @Override
    public void renderInTopPanel(SpriteBatch sb) {
        this.relic.renderInTopPanel(sb);
    }

    private void initRelic() {
        this.relic.isDone = this.isDone;
        this.relic.isObtained = this.isObtained;
        this.relic.currentX = this.currentX;
        this.relic.currentY = this.currentY;
        this.relic.targetX = this.targetX;
        this.relic.targetY = this.targetY;
        this.relic.hb.move(this.currentX, this.currentY);
    }

    @Override
    public void render(SpriteBatch sb) {
//        super.render(sb);
        this.relic.render(sb);
    }
}
