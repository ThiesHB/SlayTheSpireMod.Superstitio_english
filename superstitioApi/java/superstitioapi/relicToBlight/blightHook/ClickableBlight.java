package superstitioapi.relicToBlight.blightHook;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public interface ClickableBlight extends ClickableRelic {
    default void clickUpdate() {
        if (!(this instanceof AbstractBlight)) {
            throw new NotImplementedException();
        } else {
            AbstractBlight blight = (AbstractBlight)this;
            if (HitboxRightClick.rightClicked.get(blight.hb) ||
                    Settings.isControllerMode && blight.hb.hovered
                            && CInputActionSet.topPanel.isJustPressed()) {
                CInputActionSet.topPanel.unpress();
                this.onRightClick();
            }

        }
    }

    default boolean hovered() {
        if (this instanceof AbstractBlight) {
            AbstractBlight blight = (AbstractBlight)this;
            return blight.hb.hovered;
        } else {
            throw new NotImplementedException();
        }
    }

    @SpirePatch(
            clz = OverlayMenu.class,
            method = "update"
    )
    class ClickableBlightUpdatePatch {

        @SpireInsertPatch(
                locator = Locator.class,
                localvars = {"b"}
        )
        public static void Insert(OverlayMenu __instance, AbstractBlight blight) {
            if (blight instanceof ClickableBlight) {
                ((ClickableBlight) blight).clickUpdate();
            }

        }

        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractBlight.class, "update");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

     class ClickableBlightControllerPatches {

        public static boolean isClickableBlightHovered(Object action, Object image) {
            if (!Settings.isControllerMode || action != null && action != CInputActionSet.topPanel || image != null && image != CInputActionSet.topPanel.getKeyImg()) {
                return false;
            } else {
                return AbstractDungeon.player != null && AbstractDungeon.player.blights != null 
                        && AbstractDungeon.player.blights.stream()
                        .anyMatch((b) -> b.hb.hovered && b instanceof ClickableBlight);
            }
        }

        @SpirePatch(
                clz = TopPanel.class,
                method = "renderControllerUi"
        )
        public static class HidePotionButtonPatch {

            @SpireInstrumentPatch
            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("draw")) {
                            m.replace(String.format("if (!%s.isClickableBlightHovered(null, $1)) { $_ = $proceed($$); }", 
                                    ClickableBlightControllerPatches.class.getName()));
                        }

                    }
                };
            }
        }

        @SpirePatch(
                clz = TopPanel.class,
                method = "update"
        )
        public static class DisablePotionButtonPatch {

            @SpireInstrumentPatch
            public static ExprEditor Instrument() {
                return new ExprEditor() {
                    public void edit(MethodCall m) throws CannotCompileException {
                        if (m.getMethodName().equals("isJustPressed")) {
                            m.replace(String.format("$_ = $proceed($$) && !%s.isClickableBlightHovered($0, null);",
                                    ClickableBlightControllerPatches.class.getName()));
                        }

                    }
                };
            }
        }

        @SpirePatch(
                clz = AbstractBlight.class,
                method = "renderInTopPanel",
                paramtypez = {SpriteBatch.class}
        )
        public static class AbstractBlightRenderPatch {

            @SpirePostfixPatch
            public static void Postfix(AbstractBlight __instance, SpriteBatch ___sb) {
                if (!Settings.hideRelics && Settings.isControllerMode && __instance instanceof ClickableBlight && (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.BOSS_REWARD || __instance.isObtained) && __instance.hb.hovered) {
                    float scale = Settings.scale;
                    ___sb.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                    TextureRegion texture = new TextureRegion(CInputActionSet.topPanel.getKeyImg());
                    ___sb.draw(texture, __instance.currentX - 30.0F * scale - (float)texture.getRegionWidth() / 2.0F, __instance.currentY - 35.0F * scale - (float)texture.getRegionHeight() / 2.0F, (float)texture.getRegionWidth() / 2.0F, (float)texture.getRegionHeight() / 2.0F, (float)texture.getRegionWidth(), (float)texture.getRegionHeight(), scale * 0.85F, scale * 0.85F, 0.0F);
                }
            }
        }
    }

}
