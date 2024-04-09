package SuperstitioMod.cards.Lupa.SexType;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class RenderPatch {
    @SpirePatch(clz = EnergyPanel.class, method = "render", paramtypes = { "com.badlogic.gdx.graphics.g2d.SpriteBatch" })
    public static class EnergyPanelRenderPatch
    {
        public static void Prefix(final EnergyPanel __instance, final SpriteBatch sb) {
            if (CumPlaceHelper.doStuff) {
                CumPlaceHelper.render(sb);
            }
            SuperTip.render(sb, EasyInfoDisplayPanel.RENDER_TIMING.TIMING_ENERGYPANEL_RENDER);
        }
    }
    @SpirePatch(clz = EnergyPanel.class, method = "update")
    public static class GlobalSpecialUpdatePatch
    {
        public static void Prefix(final EnergyPanel __instance) {
//            if (HexaMod.renderFlames) {
//                GhostflameHelper.update();
//            }
            if (CumPlaceHelper.doStuff) {
                CumPlaceHelper.update();
            }
//            if (StanceHelper.hitboxStance == null) {
//                StanceHelper.init();
//            }
//            StanceHelper.update();
        }
    }
}
