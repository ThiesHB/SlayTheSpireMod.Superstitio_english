//package superstitioapi.pet.creatureManipulation;
//
//import basemod.abstracts.CustomMonster;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpireField;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.input.InputHelper;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//
//public class CreatureManipulationPatch {
//    @SpirePatch(clz = AbstractCreature.class, method = "<class>")
//    public static class PanelField {
//        public static SpireField<CreatureManipulationPanel> manipulationPanel;
//        public static SpireField<Boolean> isCurrentHPLocked;
//        public static SpireField<Integer> currentHPLockAmount;
//        public static SpireField<Boolean> isMaxHPLocked;
//        public static SpireField<Integer> maxHPLockAmount;
//        public static SpireField<Boolean> isBlockLocked;
//        public static SpireField<Integer> blockLockAmount;
//
//        static {
//            PanelField.manipulationPanel = (SpireField<CreatureManipulationPanel>) new SpireField(() -> null);
//        }
//    }
//
//    @SpirePatch(clz = AbstractCreature.class, method = "<ctor>")
//    public static class CtorPatch {
//        @SpirePostfixPatch
//        public static void PostFix(final AbstractCreature __instance) {
//            PanelField.manipulationPanel.set(__instance, new CreatureManipulationPanel(__instance));
//        }
//    }
//
//    @SpirePatches({@SpirePatch(clz = AbstractMonster.class, method = "render", paramtypez = {SpriteBatch.class}), @SpirePatch(clz = AbstractPlayer.class, method = "render", paramtypez = {SpriteBatch.class}), @SpirePatch(clz = CustomMonster.class, method = "render", paramtypez = {SpriteBatch.class})})
//    public static class RenderPatch {
//        @SpirePostfixPatch
//        public static void PostFix(final AbstractCreature __instance, final SpriteBatch sb) {
//            PanelField.manipulationPanel.get(__instance).render(sb);
//        }
//    }
//
//    @SpirePatches({@SpirePatch(clz = AbstractMonster.class, method = "update"), @SpirePatch(clz = AbstractPlayer.class, method = "update")})
//    public static class UpdatePatch {
//        @SpirePostfixPatch
//        public static void PostFix(final AbstractCreature __instance) {
//            final CreatureManipulationPanel panel = PanelField.manipulationPanel.get(__instance);
//            if (__instance.hb.hovered && InputHelper.justClickedRight && !InputHelper.isMouseDown && !__instance.isDead && !__instance.isEscaping) {
//                panel.isHidden = !panel.isHidden;
//            }
//            if (__instance.isDead || __instance.isEscaping || AbstractDungeon.player.cardInUse != null || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode) {
//                panel.isHidden = true;
//            }
//            if (panel.isHidden) {
//                panel.resetAllButtons();
//            }
//            panel.update();
//        }
//    }
//}
