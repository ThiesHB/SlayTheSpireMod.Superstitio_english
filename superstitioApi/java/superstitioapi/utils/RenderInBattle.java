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
import com.megacrit.cardcrawl.stances.AbstractStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import superstitioapi.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;

/**
 * 在Register之后就可以自动绘制和更新
 */
public interface RenderInBattle {

    ArrayList<RenderInBattle> RENDER_IN_BATTLES = new ArrayList<>();
    ArrayList<RenderInBattle> RENDER_IN_BATTLES_STANCE = new ArrayList<>();
    ArrayList<RenderInBattle> RENDER_IN_BATTLES_PANEL = new ArrayList<>();
    ArrayList<RenderInBattle> RENDER_IN_BATTLES_ABOVE_PANEL = new ArrayList<>();
    ArrayList<RenderInBattle> ShouldRemove = new ArrayList<>();

    static void clearAll() {
        RENDER_IN_BATTLES.clear();
        RENDER_IN_BATTLES_STANCE.clear();
        RENDER_IN_BATTLES_PANEL.clear();
        RENDER_IN_BATTLES_ABOVE_PANEL.clear();
        ShouldRemove.clear();
    }

    static void Register(RenderType renderType, RenderInBattle... renderThings) {
        Collections.addAll(getRenderGroup(renderType), renderThings);
        Logger.info("register " + Arrays.toString(renderThings) + " to " + renderType.name());
    }

    static ArrayList<RenderInBattle> getRenderGroup(RenderType renderType) {
        switch (renderType) {
            case Panel:
                return RENDER_IN_BATTLES_PANEL;
            case AbovePanel:
                return RENDER_IN_BATTLES_ABOVE_PANEL;
            case Stance:
                return RENDER_IN_BATTLES_STANCE;
            case Normal:
            default:
                return RENDER_IN_BATTLES;
        }
    }

    static void forEachRenderInBattle(Consumer<RenderInBattle> consumer) {
        RENDER_IN_BATTLES_STANCE.forEach(consumer);
        RENDER_IN_BATTLES.forEach(consumer);
        RENDER_IN_BATTLES_PANEL.forEach(consumer);
        RENDER_IN_BATTLES_ABOVE_PANEL.forEach(consumer);
    }

    static void forEachRenderInBattleGroup(Consumer<List<RenderInBattle>> consumer) {
        consumer.accept(RENDER_IN_BATTLES_STANCE);
        consumer.accept(RENDER_IN_BATTLES);
        consumer.accept(RENDER_IN_BATTLES_PANEL);
        consumer.accept(RENDER_IN_BATTLES_ABOVE_PANEL);
    }

    default boolean shouldRemove() {
        return false;
    }

    void render(SpriteBatch sb);

    void update();

    default void updateAnimation() {
    }

    enum RenderType {
        /**
         * 比姿态还靠前一点点
         */
        Stance,
        /**
         * 在UI层，但是是优先级最低的UI
         */
        Panel,
        /**
         * 在战斗中，会被UI等遮盖
         */
        Normal,//AbstractRoom.render
        /**
         * 最高层
         */
        AbovePanel,//Top
    }

    class RenderInBattlePatch {
        @SpirePatch(clz = AbstractRoom.class, method = "update")
        public static class UpdatePatch {
            public static void Postfix(final AbstractRoom _inst) {
                if (AbstractDungeon.isScreenUp) return;
                if (CardUtility.isNotInBattle()) return;
                forEachRenderInBattle(RenderInBattle::update);
                forEachRenderInBattle(RenderInBattle::updateAnimation);
                forEachRenderInBattle(renderInBattle -> {
                    if (renderInBattle.shouldRemove() && !ShouldRemove.contains(renderInBattle)) {
                        ShouldRemove.add(renderInBattle);
                        addToBotAbstract(() ->
                                forEachRenderInBattleGroup(renderInBattles -> {
                                    Logger.info("stopRender" + renderInBattle);
                                    renderInBattles.remove(renderInBattle);
                                    ShouldRemove.remove(renderInBattle);
                                })
                        );
                    }

                });
            }
        }

        @SpirePatch(clz = AbstractStance.class, method = "render", paramtypez = {SpriteBatch.class})
        public static class StanceRenderPatch1 {
            @SpirePrefixPatch
            public static void Prefix(final AbstractStance _inst, final SpriteBatch sb) {
                if (CardUtility.isNotInBattle()) return;
                sb.setColor(Color.WHITE);
                RENDER_IN_BATTLES_STANCE.forEach(renderInBattle -> renderInBattle.render(sb));
            }
        }

        @SpirePatch(clz = NeutralStance.class, method = "render", paramtypez = {SpriteBatch.class})
        public static class StanceRenderPatch2 {
            @SpirePrefixPatch
            public static void Prefix(final NeutralStance _inst, final SpriteBatch sb) {
                if (CardUtility.isNotInBattle()) return;
                sb.setColor(Color.WHITE);
                RENDER_IN_BATTLES_STANCE.forEach(renderInBattle -> renderInBattle.render(sb));
            }
        }


        @SpirePatch(clz = AbstractRoom.class, method = "render", paramtypez = {SpriteBatch.class})
        public static class InGameRenderPatch {
            @SpireInsertPatch(rloc = 16)
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
