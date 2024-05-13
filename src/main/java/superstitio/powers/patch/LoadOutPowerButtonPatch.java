package superstitio.powers.patch;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import superstitio.powers.AbstractLupaPower;

public class LoadOutPowerButtonPatch {
    @SpirePatch(
            cls = "loadout.screens.PowerSelectScreen$PowerButton",
            method = SpirePatch.CONSTRUCTOR,
            optional = true
    )
    public static class LoadOutPowerButtonPatch_CONSTRUCTOR_FieldAccess_powerStrings_Set {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(FieldAccess m) throws CannotCompileException {
                    if (!m.getFieldName().equals("powerStrings") || !m.isWriter()) return;
                    m.replace("if ( $0.instance instanceof " +
                            AbstractLupaPower.class.getName() +
                            " ) {" +
                            " $0.powerStrings = (( " + AbstractLupaPower.class.getName() + " ) $0.instance ).powerStrings.getRightVersion() ; " +
//                                "superstitio.Logger.temp( $0.instance.description ) ;" +
                            "} else { $proceed($$);}");
                }
            };
        }
    }

    @SpirePatch(
            cls = "loadout.screens.PowerSelectScreen$PowerButton",
            method = SpirePatch.CONSTRUCTOR,
            optional = true
    )
    public static class LoadOutPowerButtonPatch_CONSTRUCTOR_FieldAccess_name_Set {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                private int count = 0;

                public void edit(FieldAccess m) throws CannotCompileException {
                    if (!m.getFieldName().equals("name") || !m.isWriter()) return;
                    if (count > 0) return;
                    m.replace("if ( $0.instance instanceof " +
                            AbstractLupaPower.class.getName() +
                            " ) {" +
                            " $0.name = (( " + AbstractLupaPower.class.getName() + " ) $0.instance ).name ; " +
//                                "superstitio.Logger.temp( $0.instance.description ) ;" +
                            "} else { $proceed($$);}");
                    count++;
                }
            };
        }
    }

//    @SpirePatch(
//            cls = "loadout.screens.PowerSelectScreen$PowerButton",
//            method = SpirePatch.CONSTRUCTOR
//    )
//    public static class LoadOutPowerButtonPatch_CONSTRUCTOR_FieldAccess_powerStrings_Get {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                public void edit(FieldAccess m) throws CannotCompileException {
//                    Logger.temp("Field: " + m.getFieldName());
//                    if (m.getFieldName().equals("powerStrings") && m.isReader()) {
//                        Logger.temp("Field: " + m.getFieldName() + " get!");
//                        m.replace("if ( $_ == null && ( $0.instance instanceof " + AbstractLupaPower.class.getName() + " )) {" +
//                                "$_ = ((" + AbstractLupaPower.class.getName() + ") $0.instance ).powerStrings ; " +
//                                "SuperstitioMod.Logger.temp( $0.powerStrings.toString() ) ; " +
//                                "$0.name = $0.instance.name ;}"
//                                +" else { $_ = $proceed($$);}");
//                    }
//                }
//            };
//        }
//    }

    @SpirePatch(
            cls = "loadout.screens.PowerSelectScreen$PowerButton",
            method = SpirePatch.CONSTRUCTOR,
            optional = true
    )
    public static class LoadOutPowerButtonPatch_CONSTRUCTOR_MethodCall_getPrivateStatic {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ReflectionHacks.class.getName()) && m.getMethodName().equals("getPrivateStatic")) {
                        m.replace("if ( ! " + superstitio.powers.AbstractLupaPower.class.getName() + ".class.isAssignableFrom ( $1 ) )" +
                                " {  $_ = $proceed($$); } ");
//                                + "else { SuperstitioMod.Logger.temp( $1.toString() ) ;  }");

                    }
                }
            };
        }
    }
}
