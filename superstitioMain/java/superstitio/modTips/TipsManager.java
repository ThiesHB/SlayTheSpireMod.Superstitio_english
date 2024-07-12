//package superstitio.modTips;
//
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.TipTracker;
//import com.megacrit.cardcrawl.ui.FtueTip;
//
//public class TipsManager {
//    public static final String DELAY_HP_LOSE_TIP = "DELAY_HP_LOSE_TIP";
//    public static void tryShowTip(String TipName) {
//        if (!(Boolean) TipTracker.tips.get(DELAY_HP_LOSE_TIP)) {
//            AbstractDungeon.ftue = new FtueTip(LABEL[0], MSG[0], 360.0F * Settings.scale, 760.0F * Settings.scale, FtueTip.TipType.RELIC);
//            TipTracker.neverShowAgain(DELAY_HP_LOSE_TIP);
//        }
//    }
//}
