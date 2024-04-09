package SuperstitioMod.cards.Lupa.SexType;

import java.util.*;
import com.megacrit.cardcrawl.core.*;

public abstract class EasyInfoDisplayPanel
{
    public float x;
    public float y;
    public float width;
    public static ArrayList<EasyInfoDisplayPanel> specialDisplays;

    public EasyInfoDisplayPanel(final float x, final float y, final float width) {
        this.x = x * Settings.scale;
        this.y = y * Settings.scale;
        this.width = width * Settings.scale;
    }

    public abstract String getTitle();

    public abstract String getDescription();

    public abstract RENDER_TIMING getTiming();

    static {
        EasyInfoDisplayPanel.specialDisplays = new ArrayList<EasyInfoDisplayPanel>();
    }

    public enum RENDER_TIMING
    {
        TIMING_RENDERSUBSCRIBER,
        TIMING_PLAYER_RENDER,
        TIMING_ENERGYPANEL_RENDER;

//        private static /* synthetic */ RENDER_TIMING[] $values() {
//            return new RENDER_TIMING[] { RENDER_TIMING.TIMING_RENDERSUBSCRIBER, RENDER_TIMING.TIMING_PLAYER_RENDER, RENDER_TIMING.TIMING_ENERGYPANEL_RENDER };
//        }
//
//        static {
//            $VALUES = $values();
//        }
    }
}
