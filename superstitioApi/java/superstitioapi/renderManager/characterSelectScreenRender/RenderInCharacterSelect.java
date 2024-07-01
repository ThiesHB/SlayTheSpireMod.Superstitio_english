package superstitioapi.renderManager.characterSelectScreenRender;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;

import static superstitioapi.utils.ToolBox.doIfIsInstance;

public interface RenderInCharacterSelect {
//    RelicSelectionUI Relic_Selection_UI = new RelicSelectionUI();

    void renderInCharacterSelectScreen(final CharacterOption characterOption, final SpriteBatch sb);

    void updateInCharacterSelectScreen(final CharacterOption characterOption);

//    void refreshOptionChange(final CharacterOption characterOption);

    @SpirePatch2(clz = CharacterOption.class, method = "renderRelics")
    class RenderPatch {
        @SpirePostfixPatch
        public static void patch(final CharacterOption __instance, final SpriteBatch sb) {
            if (!__instance.selected) return;
            doIfIsInstance(__instance.c, RenderInCharacterSelect.class, RenderInCharacterSelect::renderInCharacterSelectScreen, __instance, sb);
        }
    }

    @SpirePatch(clz = CharacterOption.class, method = "update")
    class UpdatePatch {
        @SpirePostfixPatch
        public static void patch(final CharacterOption __instance) {
            if (!__instance.selected) return;
            doIfIsInstance(__instance.c, RenderInCharacterSelect.class, RenderInCharacterSelect::updateInCharacterSelectScreen, __instance);
        }
    }

//    @SpirePatch2(clz = AbstractCreature.class, method = "loadAnimation")
//    public static class PopulateCache
//    {
//        @SpirePostfixPatch
//        public static void catchAtlas(final AbstractCreature __instance, final String atlasUrl) {
//            if (__instance instanceof ThePackmaster) {
//                SkinSystemPatches.atlasCache.putIfAbsent(atlasUrl, ((ThePackmaster)__instance).getAtlas());
//            }
//        }
//
//        @SpireInstrumentPatch
//        public static ExprEditor skipAtlasLoadingIfExists() {
//            return new AtlasNewExprEditor();
//        }
//
//        public static class AtlasNewExprEditor extends ExprEditor
//        {
//            public void edit(final NewExpr newExpr) throws CannotCompileException {
//                if (newExpr.getClassName().equals(TextureAtlas.class.getName())) {
//                    newExpr.replace(String.format("if(%s.hasAtlas(atlasUrl, this)) {$_ = %s.getAtlas(atlasUrl);} else {$_ = $proceed($$);}",
//                    AtlasNewExprEditor.class.getName(), AtlasNewExprEditor.class.getName()));
//                }
//            }
//
//            public static boolean hasAtlas(final String atlasUrl, final AbstractCreature instance) {
//                return instance instanceof ThePackmaster && SkinSystemPatches.atlasCache.containsKey(atlasUrl);
//            }
//
//            public static TextureAtlas getAtlas(final String atlasUrl) {
//                return SkinSystemPatches.atlasCache.get(atlasUrl);
//            }
//        }
//    }
//
//    @SpirePatch2(clz = AbstractPlayer.class, method = "initializeClass")
//    public static class FixNullUnsafeCall
//    {
//        @SpireInstrumentPatch
//        public static ExprEditor skipAtlasLoadingIfExists() {
//            return new SetImageEditor();
//        }
//
//        public static class SetImageEditor extends ExprEditor
//        {
//            public void edit(final MethodCall match) throws CannotCompileException {
//                if (match.getClassName().equals(ImageMaster.class.getName()) && match.getMethodName().equals("loadImage")) {
//                    match.replace(String.format("if(%s.isPM(this) && $1 == null) {$_ = null;} else {$_ = $proceed($$);}", SetImageEditor.class
//                    .getName()));
//                }
//            }
//
//            public static boolean isPM(final AbstractPlayer instance) {
//                return instance instanceof ThePackmaster;
//            }
//        }
//    }
}
