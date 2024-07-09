package superstitioapi.renderManager;

import basemod.IUIElement;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

public interface WithUDOptionsArrow {
    /**
     * 向左箭头点击时调用
     */
    default void pageDown() {
        if (this.getSelectIndex() > 0) {
            this.setSelectIndex(getSelectIndex() - 1);
        }
        refreshAfterPageChange();
    }

    /**
     * 向右箭头点击时调用
     */
    default void pageUp() {
        if (this.getSelectIndex() < this.getMaxSelectIndex()) {
            this.setSelectIndex(getSelectIndex() + 1);
        }
        refreshAfterPageChange();
    }

    void refreshAfterPageChange();

    void render(SpriteBatch sb);

    void update();

    int getSelectIndex();

    void setSelectIndex(int newSelectIndex);

    int getMaxSelectIndex();

    default void renderArrow(SpriteBatch sb) {
        if (this.getSelectIndex() != 0) {
            getUpArrowButton().render(sb);
        }
        if (this.getSelectIndex() < this.getMaxSelectIndex()) {
            getDownArrowButton().render(sb);
        }
    }

    default void updateArrow() {
        if (this.getSelectIndex() != 0) {
            getUpArrowButton().update();
        }
        if (this.getSelectIndex() < this.getMaxSelectIndex()) {
            getDownArrowButton().update();
        }
    }

    UpArrowButton getUpArrowButton();

    DownArrowButton getDownArrowButton();

    /**
     * 点击时调用pageDown()
     */
    class UpArrowButton implements IUIElement {
        private final Texture arrow;
        private final float width;
        private final float height;
        private final Hitbox hitbox;
        private final WithUDOptionsArrow owner;
        private float x;
        private float y;

        public UpArrowButton(final float x, final float y, WithUDOptionsArrow owner) {
            this.arrow = ImageMaster.CF_LEFT_ARROW;
            this.owner = owner;
            this.x = x;
            this.y = y;
            this.width = Settings.scale * this.arrow.getWidth() / 2.0f;
            this.height = Settings.scale * this.arrow.getHeight() / 2.0f;
            this.hitbox = new Hitbox(x, y, this.width, this.height);
        }

        public void move(final float newX, final float newY) {
            this.x = newX - this.width / 2.0f;
            this.y = newY - this.height / 2.0f;
            this.hitbox.move(newX, newY);
        }

        @Override
        public void render(final SpriteBatch sb) {
            if (this.hitbox.hovered) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            final float halfW = this.arrow.getWidth() / 2.0f;
            final float halfH = this.arrow.getHeight() / 2.0f;
            sb.draw(this.arrow, this.x + 10.0f * Settings.xScale - halfW + halfW * 0.5f * Settings.scale,
                    this.y + 10.0f * Settings.yScale - halfH + halfH * 0.5f * Settings.scale, halfW, halfH, (float) this.arrow.getWidth(),
                    (float) this.arrow.getHeight(), 0.75f * Settings.scale, 0.75f * Settings.scale, -90.0f, 0, 0, this.arrow.getWidth(),
                    this.arrow.getHeight(), false, false);
            this.hitbox.render(sb);
        }

        @Override
        public void update() {
            this.hitbox.update();
            if (this.hitbox.hovered && InputHelper.justClickedLeft) {
                CardCrawlGame.sound.play("UI_CLICK_1");
                owner.pageDown();
            }
        }

        @Override
        public int renderLayer() {
            return 0;
        }

        @Override
        public int updateOrder() {
            return 0;
        }
    }

    /**
     * 点击时调用pageUp()
     */
    class DownArrowButton implements IUIElement {
        private final Texture arrow;
        private final float width;
        private final float height;
        private final Hitbox hitbox;
        private final WithUDOptionsArrow owner;
        private float x;
        private float y;

        public DownArrowButton(final float x, final float y, WithUDOptionsArrow owner) {
            this.arrow = ImageMaster.CF_RIGHT_ARROW;
            this.owner = owner;
            this.x = x;
            this.y = y;
            this.width = Settings.scale * this.arrow.getWidth() / 2.0f;
            this.height = Settings.scale * this.arrow.getHeight() / 2.0f;
            this.hitbox = new Hitbox(x, y, this.width, this.height);
        }

        public void move(final float newX, final float newY) {
            this.x = newX - this.width / 2.0f;
            this.y = newY - this.height / 2.0f;
            this.hitbox.move(newX, newY);
        }

        @Override
        public void render(final SpriteBatch sb) {
            if (this.hitbox.hovered) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            final float halfW = this.arrow.getWidth() / 2.0f;
            final float halfH = this.arrow.getHeight() / 2.0f;
            sb.draw(this.arrow, this.x + 10.0f * Settings.xScale - halfW + halfW * 0.5f * Settings.scale,
                    this.y + 10.0f * Settings.yScale - halfH + halfH * 0.5f * Settings.scale, halfW, halfH, (float) this.arrow.getWidth(),
                    (float) this.arrow.getHeight(), 0.75f * Settings.scale, 0.75f * Settings.scale, -90.0f, 0, 0, this.arrow.getWidth(),
                    this.arrow.getHeight(), false, false);
            this.hitbox.render(sb);
        }

        @Override
        public void update() {
            this.hitbox.update();
            if (this.hitbox.hovered && InputHelper.justClickedLeft) {
                CardCrawlGame.sound.play("UI_CLICK_1");
                owner.pageUp();
            }
        }

        @Override
        public int renderLayer() {
            return 0;
        }

        @Override
        public int updateOrder() {
            return 0;
        }
    }
}
