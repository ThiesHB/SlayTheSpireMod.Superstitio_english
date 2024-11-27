package superstitioapi.relicToBlight

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.evacipated.cardcrawl.modthespire.Loader
import com.megacrit.cardcrawl.blights.AbstractBlight
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.relics.AbstractRelic

/*
   以下是原版荒疫可以调用的接口名单，其他的可以看看我的BlightHook
   public void onPlayCard(AbstractCard card, AbstractMonster m) {
   }

   public boolean canPlay(AbstractCard card) {
       return true;
   }

   public void onVictory() {
   }

   public void atBattleStart() {
   }

   public void atTurnStart() {
   }

   public void onPlayerEndTurn() {
   }

   public void onBossDefeat() {
   }

   public void onCreateEnemy(AbstractMonster m) {
   }

   public void effect() {
   //不知道这个effect是干嘛的，总之也放在这里
   }

   public void onEquip() {
   }
*/
/**
 * 可以用这个荒疫来携带一个遗物，这个遗物可以正常被查看和点击
 * 荒疫需要手动添加哦
 */
abstract class BlightWithRelic(var relic: AbstractRelic) :
    AbstractBlight(relic.relicId, "BlightWithRelic", "Only a Relic carrier, do not spawn.", "maze.png", true) {
    private var isInit = false

    //    protected static String getIdWithRelic(AbstractRelic relic) {
    //        return "blight_" + relic.relicId;
    //    }
    private fun initRelic() {
        relic.isDone = this.isDone
        relic.isObtained = this.isObtained
        relic.currentX = this.currentX
        relic.currentY = this.currentY
        relic.targetX = this.targetX
        relic.targetY = this.targetY
        relic.hb.move(this.currentX, this.currentY)
    }

    override fun update() {
        if (!isInit) {
            initRelic()
            //给紫音写的patch
            isInit = !Loader.isModLoadedOrSideloaded("VUPShionMod")
        }

        if (AbstractDungeon.isScreenUp) return

        //翻页时不显示，不更新碰撞箱，Relic本来就有这个功能所以不写
        if (AbstractDungeon.player != null && AbstractDungeon.player.blights.indexOf(this) / AbstractRelic.MAX_RELICS_PER_PAGE == AbstractRelic.relicPage) {
            hb.update()
        } else {
            hb.hovered = false
        }

        super.update()
        relic.update()
    }

    override fun renderTip(sb: SpriteBatch) {
        relic.renderTip(sb)
    }

    override fun renderInTopPanel(sb: SpriteBatch) {
        relic.renderInTopPanel(sb)
    }

    override fun render(sb: SpriteBatch) {
        relic.render(sb)
    }
}
