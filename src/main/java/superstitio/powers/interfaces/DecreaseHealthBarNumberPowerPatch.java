package superstitio.powers.interfaces;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

@SpirePatch(clz = AbstractCreature.class, method = "renderHealthText")
public class DecreaseHealthBarNumberPowerPatch {
    public static ExprEditor Instrument() {
        return new ExprEditor() {
            public void edit(final FieldAccess m) throws CannotCompileException {
                if (m.getFieldName().equals("currentHealth") && m.isReader()) {
                    m.replace("$_ = -" + DecreaseHealthBarNumberPowerPatch.class.getName() + ".GetAllDecreaseHealthBarNumber( $0 ) + $proceed($$) ;");
                }
            }
        };
    }

    public static int GetAllDecreaseHealthBarNumber(AbstractCreature abstractCreature) {
        return abstractCreature.powers.stream()
                        .filter(power -> power instanceof DecreaseHealthBarNumberPower)
                        .mapToInt(power -> ((DecreaseHealthBarNumberPower) power).getDecreaseAmount()).sum();
    }
}
