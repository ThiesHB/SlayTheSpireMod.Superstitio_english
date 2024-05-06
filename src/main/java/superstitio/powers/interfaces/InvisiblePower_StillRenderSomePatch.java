//package superstitio.powers.interfaces;
//
//import com.evacipated.cardcrawl.modthespire.lib.*;
//import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
//import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
//import com.megacrit.cardcrawl.characters.AbstractPlayer;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.monsters.AbstractMonster;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
//import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;
//import javassist.CannotCompileException;
//import javassist.CtBehavior;
//import javassist.expr.ExprEditor;
//import javassist.expr.MethodCall;
//
//import java.util.ArrayList;
//
//public class InvisiblePower_StillRenderSomePatch {
//    @SpirePatch(clz = AbstractCreature.class, method = "renderPowerIcons")
//    public static class DoNotRenderPowerIconsAndSkip
//    {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                public void edit(final MethodCall m) throws CannotCompileException {
//                    if (m.getMethodName().equals("renderIcons") || m.getMethodName().equals("renderAmount")) {
//                        m.replace("if (p instanceof " + InvisiblePower_StillRenderSome.class.getName() + ") {offset -= POWER_ICON_PADDING_X;} else {$proceed($$);}");
//                    }
//                }
//            };
//        }
//    }
//
//    @SpirePatches({ @SpirePatch(clz = AbstractCreature.class, method = "renderPowerTips"), @SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips"), @SpirePatch(clz = AbstractMonster.class, method = "renderTip") })
//    public static class DoNotRenderPowerTips
//    {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                private int count = 0;
//
//                public void edit(final MethodCall m) throws CannotCompileException {
//                    if (m.getFileName().equals("AbstractMonster.java") || m.getFileName().equals("AbstractPlayer.java")) {
//                        if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
//                            if (this.count > 0) {
//                                m.replace("if (!(p instanceof " + InvisiblePower_StillRenderSome.class.getName() + ")) {$_ = $proceed($$);}");
//                            }
//                            ++this.count;
//                        }
//                    }
//                    else if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
//                        m.replace("if (!(p instanceof " + InvisiblePower_StillRenderSome.class.getName() + ")) {$_ = $proceed($$);}");
//                    }
//                }
//            };
//        }
//    }
//
//    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
//    public static class RemoveApplicationEffectsForInvisiblePower
//    {
//        @SpireInsertPatch(locator = Locator.class)
//        public static void antiApplicationEffect(final ApplyPowerAction __instance, final AbstractPower ___powerToApply) {
//            if (___powerToApply instanceof InvisiblePower_StillRenderSome) {
//                for (int i = AbstractDungeon.effectList.size() - 1; i > -1; --i) {
//                    if (___powerToApply.type == AbstractPower.PowerType.DEBUFF) {
//                        if (AbstractDungeon.effectList.get(i) instanceof PowerDebuffEffect) {
//                            AbstractDungeon.effectList.remove(i);
//                        }
//                    }
//                    else if (___powerToApply.type == AbstractPower.PowerType.BUFF && AbstractDungeon.effectList.get(i) instanceof PowerBuffEffect) {
//                        AbstractDungeon.effectList.remove(i);
//                    }
//                }
//            }
//        }
//
////        @SpireInstrumentPatch
//        public static ExprEditor DontFlashInvisiblePower() {
//            return new ExprEditor() {
//                public void edit(final MethodCall m) throws CannotCompileException {
//                    if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("flash")) {
//                        m.replace("if (!(powerToApply instanceof " + InvisiblePower_StillRenderSome.class.getName() + ")) {$_ = $proceed($$);}");
//                    }
//                }
//            };
//        }
//
//        private static class Locator extends SpireInsertLocator
//        {
//            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
//                final Matcher finalMatcher = (Matcher)new Matcher.MethodCallMatcher((Class)AbstractDungeon.class, "onModifyPower");
//                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
//            }
//        }
//    }
//
//    @SpirePatch(clz = RemoveSpecificPowerAction.class, method = "update")
//    public static class HideExpireText
//    {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                public void edit(final MethodCall m) throws CannotCompileException {
//                    if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
//                        m.replace("if (!(removeMe instanceof " + InvisiblePower_StillRenderSome.class.getName() + ")) {$_ = $proceed($$);}");
//                    }
//                }
//            };
//        }
//    }
//}
