package superstitio.powers.patchAndInterface.interfaces.invisible;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class InvisiblePower_InvisibleTipsPatch {

    private static ExprEditor getExprEditor() {
        return new ExprEditor() {
            private int count = 0;

            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(ArrayList.class.getName()) && m.getMethodName().equals("add")) {
                    if (this.count > 0) {
                        m.replace("if (!( " + InvisiblePower_InvisibleTips.class.getName() + ".shouldInvisible(p)" +
                                ")) {$_ = $proceed($$);}");
                    }

                    ++this.count;
                }
            }
        };
    }


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
                    if (!m.getClassName().equals(ArrayList.class.getName()) || !m.getMethodName().equals("add")) return;
                    m.replace("if (!(" + InvisiblePower_InvisibleTips.class.getName() + ".shouldInvisible(p)" +
                            ")) {$_ = $proceed($$);}");

                }
            };
        }
    }


}
