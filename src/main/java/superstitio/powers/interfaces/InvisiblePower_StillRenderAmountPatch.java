package superstitio.powers.interfaces;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class InvisiblePower_StillRenderAmountPatch {

    @SpirePatch(
            clz = AbstractMonster.class,
            method = "renderTip")
    public static class RenderMonsterPowerTips {

        public static ExprEditor Instrument() {
            return getExprEditor();
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "renderPowerTips")
    public static class RenderPlayerPowerTips {

        public static ExprEditor Instrument() {
            return getExprEditor();
        }


    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderPowerTips",
            paramtypes = {"com.badlogic.gdx.graphics.g2d.SpriteBatch"})
    public static class RenderCreaturePowerTips {

        public static ExprEditor Instrument() {
            return new ExprEditor() {

                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
                        m.replace("if (!(p instanceof " +
                                InvisiblePower_StillRenderAmount.class.getName() +
                                ")) {$_ = $proceed($$);}");
                    }

                }
            };
        }
    }

    private static ExprEditor getExprEditor() {
        return new ExprEditor() {
            private int count = 0;

            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
                    if (this.count > 0) {
                        m.replace("if (!(p instanceof " +
                                InvisiblePower_StillRenderAmount.class.getName() +
                                ")) {$_ = $proceed($$);}");
                    }

                    ++this.count;
                }
            }
        };
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderPowerIcons"
    )
    public static class RenderPowerIcons {

        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getMethodName().equals("renderIcons")) {
                        m.replace("if (p instanceof " + InvisiblePower_StillRenderAmount.class.getName() + ") " +
                                "{offset -= " +
                                "POWER_ICON_PADDING_X;} else {$proceed($$);}");
                    }
                    else if (m.getMethodName().equals("renderAmount")) {
                        m.replace("if (p instanceof " + InvisiblePower_StillRenderAmount.class.getName() + ") " +
                                "{offset -= " +
                                "POWER_ICON_PADDING_X;$proceed($$);} else {$proceed($$);}");
                    }

                }
            };
        }
    }
}
