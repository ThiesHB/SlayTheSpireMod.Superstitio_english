package superstitioapi.powers.interfaces.invisible;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class InvisiblePower_InvisibleRemovePowerEffectPatch {

    @SpirePatch(clz = RemoveSpecificPowerAction.class, method = "update")
    public static class HideExpireText {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(final MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
                        m.replace("if (!(" + InvisiblePower_InvisibleRemovePowerEffect.class.getName() +
                                ".shouldInvisibleRemovePowerEffect(removeMe))) {$_ = $proceed($$);}");
                    }
                }
            };
        }
    }
}
