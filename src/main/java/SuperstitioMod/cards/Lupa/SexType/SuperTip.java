package SuperstitioMod.cards.Lupa.SexType;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;

public class SuperTip {
    private static final Color BASE_COLOR;
    private static final float SHADOW_DIST_Y;
    private static final float SHADOW_DIST_X;
    private static final float BOX_EDGE_H;
    private static final float BOX_BODY_H;
    private static final float TEXT_OFFSET_X;
    private static final float HEADER_OFFSET_Y;
    private static final float BODY_OFFSET_Y;
    private static final float TIP_DESC_LINE_SPACING;
    private static final float PADDING_WIDTH_WRAP;

    static {
        BASE_COLOR = new Color(1.0f, 0.9725f, 0.8745f, 1.0f);
        SHADOW_DIST_Y = 14.0f * Settings.scale;
        SHADOW_DIST_X = 9.0f * Settings.scale;
        BOX_EDGE_H = 32.0f * Settings.scale;
        BOX_BODY_H = 57.0f * Settings.scale;
        TEXT_OFFSET_X = 20.0f * Settings.scale;
        HEADER_OFFSET_Y = 12.0f * Settings.scale;
        BODY_OFFSET_Y = -20.0f * Settings.scale;
        TIP_DESC_LINE_SPACING = 26.0f * Settings.scale;
        PADDING_WIDTH_WRAP = 20.0f * Settings.scale;
    }

    public static void render(final SpriteBatch sb, final EasyInfoDisplayPanel.RENDER_TIMING t) {
        if (!AbstractDungeon.isScreenUp) {
            for (final EasyInfoDisplayPanel d : EasyInfoDisplayPanel.specialDisplays) {
                final String s = d.getDescription();
                if (d.getTiming() == t && s != null && !s.equals("") && !s.equals("NORENDER") && !Settings.hidePopupDetails) {
                    renderTipBox(d.x, d.y, d.width, sb, d.getTitle(), d.getDescription());
                }
            }
        }
    }

    private static void renderTipBox(final float x, final float y, final float width, final SpriteBatch sb, final String title,
                                     final String description) {
        final float ourWidth = width - 15.0f * Settings.scale;
        final float h = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, description,
                ourWidth - (SuperTip.PADDING_WIDTH_WRAP + SuperTip.TEXT_OFFSET_X), SuperTip.TIP_DESC_LINE_SPACING);
        sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
        sb.draw(ImageMaster.KEYWORD_TOP, x + SuperTip.SHADOW_DIST_X, y - SuperTip.SHADOW_DIST_Y, ourWidth, SuperTip.BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x + SuperTip.SHADOW_DIST_X, y - h - SuperTip.BOX_EDGE_H - SuperTip.SHADOW_DIST_Y, ourWidth,
                h + SuperTip.BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x + SuperTip.SHADOW_DIST_X, y - h - SuperTip.BOX_BODY_H - SuperTip.SHADOW_DIST_Y, ourWidth,
                SuperTip.BOX_EDGE_H);
        sb.setColor(Color.WHITE.cpy());
        sb.draw(ImageMaster.KEYWORD_TOP, x, y, ourWidth, SuperTip.BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BODY, x, y - h - SuperTip.BOX_EDGE_H, ourWidth, h + SuperTip.BOX_EDGE_H);
        sb.draw(ImageMaster.KEYWORD_BOT, x, y - h - SuperTip.BOX_BODY_H, ourWidth, SuperTip.BOX_EDGE_H);
        FontHelper.renderFontLeftTopAligned(sb, FontHelper.tipHeaderFont, title, x + SuperTip.TEXT_OFFSET_X, y + SuperTip.HEADER_OFFSET_Y,
                Settings.GOLD_COLOR);
        FontHelper.renderSmartText(sb, FontHelper.tipBodyFont, description, x + SuperTip.TEXT_OFFSET_X, y + SuperTip.BODY_OFFSET_Y,
                ourWidth - (SuperTip.PADDING_WIDTH_WRAP + SuperTip.TEXT_OFFSET_X), SuperTip.TIP_DESC_LINE_SPACING, SuperTip.BASE_COLOR);
    }
}
