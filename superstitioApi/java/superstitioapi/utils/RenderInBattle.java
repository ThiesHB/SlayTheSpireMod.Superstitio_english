package superstitioapi.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 在Register之后就可以自动绘制和更新
 */
public interface RenderInBattle {

    ArrayList<RenderInBattle> RENDER_IN_BATTLES = new ArrayList<>();
    ArrayList<RenderInBattle> RENDER_IN_BATTLES_PANEL = new ArrayList<>();
    ArrayList<RenderInBattle> RENDER_IN_BATTLES_ABOVE_PANEL = new ArrayList<>();

    static void clearAll() {
        RENDER_IN_BATTLES.clear();
        RENDER_IN_BATTLES_PANEL.clear();
        RENDER_IN_BATTLES_ABOVE_PANEL.clear();
    }

    static void Register(RenderType renderType, RenderInBattle... renderThings) {
        switch (renderType) {
            case Panel:
                Collections.addAll(RENDER_IN_BATTLES_PANEL, renderThings);
                break;
            case AbovePanel:
                Collections.addAll(RENDER_IN_BATTLES_ABOVE_PANEL, renderThings);
                break;
            case Normal:
            default:
                Collections.addAll(RENDER_IN_BATTLES, renderThings);
                break;
        }

    }

    void render(SpriteBatch sb);

    void update();

    default void updateAnimation() {
    }

    enum RenderType {
        /**
         * 在UI层，但是是优先级最低的UI
         */
        Panel,//UI
        /**
         * 在战斗中，会被UI等遮盖
         */
        Normal,//AbstractRoom.render
        /**
         * 最高层
         */
        AbovePanel//Top
    }

    class RenderInBattlePatch {
        @SpirePatch(clz = AbstractRoom.class, method = "update")
        public static class UpdatePatch {
            public static void Postfix(final AbstractRoom _inst) {
                if (AbstractDungeon.isScreenUp) return;
                if (CardUtility.isNotInBattle()) return;
                RENDER_IN_BATTLES.forEach(RenderInBattle::update);
                RENDER_IN_BATTLES.forEach(RenderInBattle::updateAnimation);
                RENDER_IN_BATTLES_PANEL.forEach(RenderInBattle::update);
                RENDER_IN_BATTLES_PANEL.forEach(RenderInBattle::updateAnimation);
                RENDER_IN_BATTLES_ABOVE_PANEL.forEach(RenderInBattle::update);
                RENDER_IN_BATTLES_ABOVE_PANEL.forEach(RenderInBattle::updateAnimation);
            }
        }

        @SpirePatch(clz = AbstractRoom.class, method = "render", paramtypez = {SpriteBatch.class})
        public static class InGameRenderPatch {
            @SpireInsertPatch(rloc = 13)
            public static void Insert(final AbstractRoom _inst, final SpriteBatch sb) {
                if (CardUtility.isNotInBattle()) return;
                sb.setColor(Color.WHITE);
                RENDER_IN_BATTLES.forEach(renderInBattle -> renderInBattle.render(sb));
            }
        }

        @SpirePatch(clz = OverlayMenu.class, method = "render", paramtypez = {SpriteBatch.class})
        public static class OverlayMenuRenderPatch {

            @SpirePrefixPatch
            public static void Prefix(final OverlayMenu _inst, final SpriteBatch sb) {
                if (Settings.hideLowerElements) return;
                if (CardUtility.isNotInBattle()) return;
                sb.setColor(Color.WHITE);
                RENDER_IN_BATTLES_PANEL.forEach(renderInBattle -> renderInBattle.render(sb));
            }
        }

        @SpirePatch(clz = AbstractRoom.class, method = "renderAboveTopPanel", paramtypez = {SpriteBatch.class})
        public static class RenderAboveTopPanelPatch {
            @SpirePostfixPatch
            public static void Postfix(final AbstractRoom _inst, final SpriteBatch sb) {
                if (AbstractDungeon.isScreenUp) return;
                if (CardUtility.isNotInBattle()) return;
                sb.setColor(Color.WHITE);
                RENDER_IN_BATTLES_ABOVE_PANEL.forEach(renderInBattle -> renderInBattle.render(sb));
            }
        }
    }
}
