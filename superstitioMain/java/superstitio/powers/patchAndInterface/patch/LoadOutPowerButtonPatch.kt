package superstitio.powers.patchAndInterface.patch

import basemod.ReflectionHacks
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import javassist.CannotCompileException
import javassist.expr.ExprEditor
import javassist.expr.FieldAccess
import javassist.expr.MethodCall
import superstitio.powers.AbstractSuperstitioPower

object LoadOutPowerButtonPatch {
    @SpirePatch2(
        cls = "loadout.screens.PowerSelectScreen\$PowerButton",
        method = SpirePatch.CONSTRUCTOR,
        optional = true
    )
    object LoadOutPowerButtonPatch_CONSTRUCTOR_FieldAccess_powerStrings_Set {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor {
            return object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: FieldAccess) {
                    if (m.fieldName != "powerStrings" || !m.isWriter) return
                    m.replace(
                        "if ( $0.instance instanceof " +AbstractSuperstitioPower::class.qualifiedName +") {" +
                                " $0.powerStrings = (( " + AbstractSuperstitioPower::class.qualifiedName + " ) $0.instance ).powerStrings.getRightVersion() " +
                                "; " +  //                                "superstitio.Logger.temp( $0.instance.description ) ;" +
                                "} else { \$proceed($$);}"
                    )
                }
            }
        }
    }

    @SpirePatch2(
        cls = "loadout.screens.PowerSelectScreen\$PowerButton",
        method = SpirePatch.CONSTRUCTOR,
        optional = true
    )
    object LoadOutPowerButtonPatch_CONSTRUCTOR_FieldAccess_name_Set {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor {
            return object : ExprEditor() {
                private var count = 0

                @Throws(CannotCompileException::class)
                override fun edit(m: FieldAccess) {
                    if (m.fieldName != "name" || !m.isWriter) return
                    if (count > 0) return
                    m.replace(
                        "if ( $0.instance instanceof " +
                                AbstractSuperstitioPower::class.qualifiedName +
                                " ) {" +
                                " $0.name = (( " + AbstractSuperstitioPower::class.qualifiedName + " ) $0.instance ).name ; " +  //                                "superstitio.Logger.temp( $0.instance.description ) ;" +
                                "} else { \$proceed($$);}"
                    )
                    count++
                }
            }
        }
    }

    //    @SpirePatch2(
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
    //                        m.replace("if ( \$_ == null && ( $0.instance instanceof " + AbstractLupaPower.class.getName() + " )) {" +
    //                                "\$_ = ((" + AbstractLupaPower.class.getName() + ") $0.instance ).powerStrings ; " +
    //                                "SuperstitioMod.Logger.temp( $0.powerStrings.toString() ) ; " +
    //                                "$0.name = $0.instance.name ;}"
    //                                +" else { \$_ = $proceed($$);}");
    //                    }
    //                }
    //            };
    //        }
    //    }
    @SpirePatch2(
        cls = "loadout.screens.PowerSelectScreen\$PowerButton",
        method = SpirePatch.CONSTRUCTOR,
        optional = true
    )
    object LoadOutPowerButtonPatch_CONSTRUCTOR_MethodCall_getPrivateStatic {
        @SpireInstrumentPatch
        @JvmStatic
        fun Instrument(): ExprEditor {
            return object : ExprEditor() {
                @Throws(CannotCompileException::class)
                override fun edit(m: MethodCall) {
                    if (m.className == ReflectionHacks::class.qualifiedName && m.methodName == "getPrivateStatic") {
                        m.replace(
                            "if ( ! " + AbstractSuperstitioPower::class.qualifiedName + ".class.isAssignableFrom ( $1 ) )" +
                                    " {  \$_ = \$proceed($$); } "
                        )

                        //                                + "else { SuperstitioMod.Logger.temp( $1.toString() ) ;  }");
                    }
                }
            }
        }
    }
}
