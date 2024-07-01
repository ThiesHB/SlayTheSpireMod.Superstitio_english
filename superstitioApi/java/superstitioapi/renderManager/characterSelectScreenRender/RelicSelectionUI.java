package superstitioapi.renderManager.characterSelectScreenRender;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Circlet;
import superstitioapi.renderManager.WithLROptionsArrow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import static superstitioapi.utils.TipsUtility.renderTipsWithMouse;

public class RelicSelectionUI implements WithLROptionsArrow {
    public static final float RELIC_Y_OFFSET = -60.0F * Settings.scale;
    public static final float RELIC_HITBOX_SIZE = 80.0F * Settings.scale;
    public static final Color TEXT_COLOR = Color.GOLDENROD.cpy();
    private static final float RelicImgSize = 128.0F;
    private static final float RelicSize = 64.0F;
    private static final BitmapFont titleFont = FontHelper.cardTitleFont;
    //    private static UIStringsSet uiStrings = CardCrawlGame.languagePack.getUIString(DataUtility.makeID("SkinSystemUI"));
    private final List<AbstractRelic> relics;
    private final String optionTitle;
    private final PowerTip optionTip;
    private final float textWidth;
    private final float cX;
    private final float cY;
    private final LeftArrowButton leftArrowButton;
    private final RightArrowButton rightArrowButton;
    private final Hitbox relicHitbox;
    private final Hitbox textHitbox;
    public int selectIndex;
    private Consumer<AbstractRelic> refreshAfterSelect;

    public RelicSelectionUI(float cX, float cY, final List<AbstractRelic> relics, final PowerTip optionTip) {
        this(cX, cY, relics, optionTip.header, optionTip);
    }

    public RelicSelectionUI(float cX, float cY, final List<AbstractRelic> relics, final String optionTitle, final PowerTip optionTip) {
        this.cX = cX;
        this.cY = cY;
        this.relics = relics;
        this.optionTitle = optionTitle;
        this.optionTip = optionTip;
        this.textWidth = Math.max(FontHelper.getWidth(titleFont, optionTitle, 1.0f), RELIC_HITBOX_SIZE);

        this.relicHitbox = new Hitbox(RELIC_HITBOX_SIZE, RELIC_HITBOX_SIZE);
        relicHitbox.move(this.cX, this.cY + RELIC_Y_OFFSET);

        this.leftArrowButton = new LeftArrowButton(cX, cY, this);
        leftArrowButton.move(cX - this.textWidth / 2 - this.leftArrowButton.w / 2, cY);

        this.rightArrowButton = new RightArrowButton(cX + textWidth, cY, this);
        rightArrowButton.move(cX + textWidth / 2 + this.rightArrowButton.w / 2, cY);

        this.textHitbox = new Hitbox(textWidth, Math.max(this.rightArrowButton.h, this.leftArrowButton.h));
        textHitbox.move(this.cX, this.cY);

        this.selectIndex = 0;
    }

    public RelicSelectionUI setRefreshAfterSelect(Consumer<AbstractRelic> refreshAfterSelect) {
        this.refreshAfterSelect = refreshAfterSelect;
        return this;
    }

    public float getTotalWidth() {
        return textWidth + leftArrowButton.w + rightArrowButton.w;
    }

    public AbstractRelic getSelectRelic() {
        if (selectIndex >= 0 && selectIndex < relics.size()) return relics.get(selectIndex);
        return new Circlet();
    }

    @Override
    public void refreshAfterPageChange() {
        if (refreshAfterSelect == null) return;
        refreshAfterSelect.accept(getSelectRelic());
    }

    @Override
    public boolean isLoop() {
        return true;
    }

    public void render(final SpriteBatch sb) {
        AbstractRelic relic = getSelectRelic();

        if (relicHitbox.hovered) {
            renderTipsWithMouse(relic.tips);
        }
        if (textHitbox.hovered) {
            renderTipsWithMouse(new ArrayList<>(Collections.singletonList(optionTip)));
        }

        FontHelper.renderFontCentered(sb, titleFont, optionTitle, this.cX, this.cY, TEXT_COLOR);
        sb.setColor(new Color(0.0F, 0.0F, 0.0F, 0.25F));
        sb.draw(relic.outlineImg, this.cX - RelicSize, this.cY + RELIC_Y_OFFSET - RelicSize, RelicImgSize, RelicImgSize);
        sb.setColor(Color.WHITE);
        sb.draw(relic.img, this.cX - RelicSize, this.cY + RELIC_Y_OFFSET - RelicSize, RelicImgSize, RelicImgSize);

        WithLROptionsArrow.super.renderArrow(sb);
        relicHitbox.render(sb);
        textHitbox.render(sb);
    }

    public void update() {
        if (relicHitbox.hovered || textHitbox.hovered) {
            if (InputHelper.justClickedLeft) {
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.pageUp();
            } else if (InputHelper.justClickedRight) {
                CardCrawlGame.sound.play("UI_CLICK_1");
                this.pageDown();
            }
        }
        relicHitbox.update();
        textHitbox.update();
        WithLROptionsArrow.super.updateArrow();
    }

    @Override
    public int getSelectIndex() {
        return selectIndex;
    }

    @Override
    public void setSelectIndex(int newSelectIndex) {
        selectIndex = newSelectIndex;
    }

    @Override
    public int getMaxSelectIndex() {
        return relics.size() - 1;
    }

    @Override
    public LeftArrowButton getLeftArrowButton() {
        return leftArrowButton;
    }

    @Override
    public RightArrowButton getRightArrowButton() {
        return rightArrowButton;
    }
}
