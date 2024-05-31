package superstitioapi.powers.interfaces.invisible;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class InvisiblePower_InvisibleIconAndAmountPatch {
    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderPowerIcons"
    )
    public static class RenderPowerIcons {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderIcons") || m.getMethodName().equals("renderAmount")) {
                        m.replace("if (" + InvisiblePower_InvisibleIconAndAmount.class.getName() + ".shouldInvisible(p)) " +
                                "{offset -= " +
                                "POWER_ICON_PADDING_X;} else {$proceed($$);}");
                    }
                    else if (m.getMethodName().equals("renderAmount")) {
                        m.replace("if (" + InvisiblePower_InvisibleIconAndAmount.class.getName() + ".shouldInvisible(p)) " +
                                "{offset -= " +
                                "POWER_ICON_PADDING_X;$proceed($$);} else {$proceed($$);}");
                    }

                }
            };
        }
    }
}
