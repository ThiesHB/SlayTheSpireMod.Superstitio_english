package superstitio.powers.patchAndInterface.interfaces;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;


public class DecreaseHealthBarNumberPowerPatch {
    private static final float HIDDEN_DEEPER = 0.2f;

    public static int GetAllDecreaseHealthBarNumberShouldShown(AbstractCreature abstractCreature) {

        return abstractCreature.powers.stream()
                .filter(power -> power instanceof DecreaseHealthBarNumberPower)
                .filter(power -> ((DecreaseHealthBarNumberPower) power).showDecreaseAmount())
                .mapToInt(power -> ((DecreaseHealthBarNumberPower) power).getDecreaseAmount()).sum();
    }

    public static int GetAllDecreaseHealthBarNumberTotall(AbstractCreature abstractCreature) {

        return abstractCreature.powers.stream()
                .filter(power -> power instanceof DecreaseHealthBarNumberPower)
                .mapToInt(power -> ((DecreaseHealthBarNumberPower) power).getDecreaseAmount()).sum();
    }

//    @SpirePatch(clz = AbstractCreature.class, method = "renderHealthText")
//    public static class DecreaseNumberPatch {
//        public static ExprEditor Instrument() {
//            return new ExprEditor() {
//                public void edit(final FieldAccess m) throws CannotCompileException {
//                    if (m.getFieldName().equals("currentHealth") && m.isReader()) {
//                        m.replace("$_ = -" + DecreaseHealthBarNumberPowerPatch.class.getName() + ".GetAllDecreaseHealthBarNumber( $0 ) + $proceed" +
//                                "($$) ;");
//                    }
//                }
//            };
//        }
//    }

    @SpirePatch(clz = AbstractCreature.class, method = "renderHealthText")
    public static class DecreaseNumberTextPatch {
        private static final float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
        private static final float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;

        public static SpireReturn<Void> Prefix(final AbstractCreature _inst, SpriteBatch spriteBatch, float y) {
            float targetHealthBarWidth = ReflectionHacks.getPrivate(_inst, AbstractCreature.class, "targetHealthBarWidth");

            Color hbTextColor = ReflectionHacks.getPrivate(_inst, AbstractCreature.class, "hbTextColor");

            if (targetHealthBarWidth == 0.0F) {
//                FontHelper.renderFontCentered(spriteBatch, FontHelper.healthInfoFont, AbstractCreature.TEXT[0], _inst.hb.cX, y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y - Settings.scale, hbTextColor);
                return SpireReturn.Continue();
            }

            if (_inst.healthHb.hovered) {
                int decreaseNumber = GetAllDecreaseHealthBarNumberTotall(_inst);
                if (decreaseNumber <= 0) return SpireReturn.Continue();

                hbTextColor.a *= HIDDEN_DEEPER;

                Color renderColor = _inst.powers.stream()
                        .filter(power -> power instanceof HealthBarRenderPower)
                        .map(power -> ((HealthBarRenderPower) power).getColor()).findAny().orElse(hbTextColor);


                FontHelper.renderFontCentered(
                        spriteBatch, FontHelper.healthInfoFont,
                        String.valueOf(decreaseNumber), _inst.hb.cX,
                        y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale, renderColor);
            }
            else {
                int decreaseNumber = GetAllDecreaseHealthBarNumberShouldShown(_inst);
                if (decreaseNumber <= 0) return SpireReturn.Continue();
                float HEALTH_BAR_OFFSET_Y = -28.0F * Settings.scale;
                float HEALTH_TEXT_OFFSET_Y = 6.0F * Settings.scale;


                FontHelper.renderFontCentered(
                        spriteBatch, FontHelper.healthInfoFont,
                        _inst.currentHealth + "(-" + decreaseNumber + ")" + "/" + _inst.maxHealth, _inst.hb.cX,
                        y + HEALTH_BAR_OFFSET_Y + HEALTH_TEXT_OFFSET_Y + 5.0F * Settings.scale, hbTextColor);
            }

            return SpireReturn.Return();
        }
    }
}
