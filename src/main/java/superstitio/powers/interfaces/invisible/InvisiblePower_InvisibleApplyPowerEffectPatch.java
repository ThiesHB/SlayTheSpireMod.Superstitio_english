package superstitio.powers.interfaces.invisible;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.PowerBuffEffect;
import com.megacrit.cardcrawl.vfx.combat.PowerDebuffEffect;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

public class InvisiblePower_InvisibleApplyPowerEffectPatch {
    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class RemoveApplicationEffectsForInvisiblePower {
        @SpireInsertPatch(locator = Locator.class)
        public static void antiApplicationEffect(final ApplyPowerAction __instance, final AbstractPower ___powerToApply) {
            if (!(___powerToApply instanceof InvisiblePower_InvisibleApplyPowerEffect)) return;
            for (int i = AbstractDungeon.effectList.size() - 1; i > -1; i--) {
                if (___powerToApply.type == AbstractPower.PowerType.DEBUFF) {
                    if (AbstractDungeon.effectList.get(i) instanceof PowerDebuffEffect) {
                        AbstractDungeon.effectList.remove(i);
                    }
                }
                else if (___powerToApply.type == AbstractPower.PowerType.BUFF
                        && AbstractDungeon.effectList.get(i) instanceof PowerBuffEffect) {
                    AbstractDungeon.effectList.remove(i);
                }
            }
        }


        private static class Locator extends SpireInsertLocator {
            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
                final Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "onModifyPower");
                return LineFinder.findAllInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class RemoveFlashEffectsForInvisiblePower {
        @SpireInstrumentPatch
        public static ExprEditor DontFlashInvisiblePower() {
            return new ExprEditor() {
                public void edit(final MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPower.class.getName()) && m.getMethodName().equals("flash")) {
                        m.replace("if (!(powerToApply instanceof "
                                + InvisiblePower_InvisibleApplyPowerEffect.class.getName() + ")) {$_ = $proceed($$);}");
                    }
                }
            };
        }
    }
}
