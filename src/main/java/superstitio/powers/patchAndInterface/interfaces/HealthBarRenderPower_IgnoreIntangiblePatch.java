////
//// Source code recreated from a .class file by IntelliJ IDEA
//// (powered by FernFlower decompiler)
////
//
//package superstitio.powers.patchAndInterface.interfaces;
//
//import com.evacipated.cardcrawl.mod.stslib.patches.powerInterfaces.HealthBarRenderPowerPatch;
//import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import javassist.CannotCompileException;
//import javassist.expr.ExprEditor;
//import javassist.expr.MethodCall;
//
//public class HealthBarRenderPower_IgnoreIntangiblePatch {
//    @SpirePatch(clz = HealthBarRenderPowerPatch.RenderPowerHealthBar.class, method = "Insert")
//    public static class IgnoreIntangiblePatch {
//        public static boolean returnFalse() {
//            return false;
//        }
//
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                private int count = 0;
//
//                public void edit(final MethodCall m) throws CannotCompileException {
//                    count++;
//                    if (count != 3) return;
//                    if (m.getClassName().equals(AbstractCreature.class.getName()) && m.getMethodName().equals("hasPower")) {
//                        m.replace("if ( power instanceof " + HealthBarRenderPower_IgnoreIntangible.class.getName() + ")" +
//                                " { $_ = " + false + " ;}" +
//                                "else { $_ = $proceed($$) ;}");
//                    }
//                }
//
//
//            };
//        }
//    }
//}
