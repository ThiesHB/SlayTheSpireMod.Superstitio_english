package superstitio.powers.interfaces;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.powers.AbstractPower;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import superstitio.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DecreaseHealthBarNumberPowerPatch {
    private static final float HIDDEN_DEEPER = 0.2f;

    public static int GetAllDecreaseHealthBarNumber(AbstractCreature abstractCreature) {
        Color hbTextColor = ReflectionHacks.getPrivate(abstractCreature, AbstractCreature.class, "hbTextColor");
        int newBarNumber = abstractCreature.powers.stream()
                .filter(power -> power instanceof DecreaseHealthBarNumberPower)
                .mapToInt(power -> ((DecreaseHealthBarNumberPower) power).getDecreaseAmount()).sum();
        if (abstractCreature.healthHb.hovered && newBarNumber != 0) {
            hbTextColor.a *= HIDDEN_DEEPER;
        }
        return newBarNumber;
    }

    public static boolean isInvisible(AbstractPower power) {
        return power instanceof InvisiblePower || power instanceof InvisiblePower_StillRenderApplyAndRemove;
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderHealthText")
    public static class DecreaseNumberPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(final FieldAccess m) throws CannotCompileException {
                    if (m.getFieldName().equals("currentHealth") && m.isReader()) {
                        m.replace("$_ = -" + DecreaseHealthBarNumberPowerPatch.class.getName() + ".GetAllDecreaseHealthBarNumber( $0 ) + $proceed" +
                                "($$) ;");
                    }
                }
            };
        }
    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderHealthText")
    public static class DecreaseNumberTextPatch {
        public static void Postfix(final AbstractCreature _inst, SpriteBatch spriteBatch, float y) {
            if (_inst.healthHb.hovered) {
                int decreaseNumber = GetAllDecreaseHealthBarNumber(_inst);
                if (decreaseNumber <= 0) return;
                Color hbTextColor = ReflectionHacks.getPrivate(_inst, AbstractCreature.class, "hbTextColor");
                float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
                float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;


                Color renderColor = _inst.powers.stream()
                        .filter(power -> power instanceof HealthBarRenderPower)
                        .map(power -> ((HealthBarRenderPower) power).getColor()).findAny().orElse(hbTextColor);


                FontHelper.renderFontCentered(
                        spriteBatch, FontHelper.healthInfoFont,
                        String.valueOf(decreaseNumber), _inst.hb.cX,
                        y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale, renderColor);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderPowerTips")
    public static class DecreaseNumberTipPatch {
        @SpireInsertPatch(rloc = 5, localvars = {"tips"})
        public static void Insert(final AbstractPlayer _inst, SpriteBatch spriteBatch, final ArrayList<PowerTip> tips) {
//            ArrayList<PowerTip> tips = ReflectionHacks.getPrivate(_inst, AbstractPlayer.class, "tips");

            HashMap<String, AbstractPower> powerNeedAdd = new HashMap<>();
            HashMap<String, Integer> powerNeedAddNumber = new HashMap<>();
            _inst.powers.stream()
                    .filter(power -> power instanceof DecreaseHealthBarNumberPower && DecreaseHealthBarNumberPowerPatch.isInvisible(power))
                    .forEach(power -> {
                        powerNeedAdd.put(power.ID, power);
                        int addAmount = ((DecreaseHealthBarNumberPower) power).getDecreaseAmount();
                        powerNeedAddNumber.put(power.ID, powerNeedAddNumber.getOrDefault(power.ID, 0) + addAmount);
                    });
            if (powerNeedAdd.size() != powerNeedAddNumber.size()) {
                Logger.warning("凭什么这两个map的大小不相等，你来这里看看：" + DecreaseNumberTipPatch.class.getName());
                return;
            }
            for (Map.Entry<String, AbstractPower> entry : powerNeedAdd.entrySet()) {
                String string = entry.getKey();
                AbstractPower power = entry.getValue();
                int temp = ((DecreaseHealthBarNumberPower) power).getDecreaseAmount();
                ((DecreaseHealthBarNumberPower) power).setDecreaseAmount(powerNeedAddNumber.getOrDefault(string, temp));
                power.updateDescription();
                if (power.region48 != null) {
                    Logger.temp(power.name);
                    Logger.temp(power.description);
                    tips.add(new PowerTip(power.name, power.description, power.region48));
                } else {
                    tips.add(new PowerTip(power.name, power.description, power.img));
                }
                ((DecreaseHealthBarNumberPower) power).setDecreaseAmount(temp);
            }


        }
    }

}
