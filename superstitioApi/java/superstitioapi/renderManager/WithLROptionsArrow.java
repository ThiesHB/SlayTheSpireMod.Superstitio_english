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

public interface WithLROptionsArrow {
    /**
     * 向左箭头点击时调用
     */
    default void pageDown() {
        if (isLoop()) {
            if (this.getSelectIndex() > 0) {
                this.setSelectIndex(getSelectIndex() - 1);
            } else {
                this.setSelectIndex(this.getMaxSelectIndex());
            }
        } else {
            if (this.getSelectIndex() > 0) {
                this.setSelectIndex(getSelectIndex() - 1);
            }
        }
        refreshAfterPageChange();
    }

    /**
     * 向右箭头点击时调用
     */
    default void pageUp() {
        if (isLoop()) {
            if (this.getSelectIndex() < this.getMaxSelectIndex()) {
                this.setSelectIndex(getSelectIndex() + 1);
            } else {
                this.setSelectIndex(0);
            }
        } else {
            if (this.getSelectIndex() < this.getMaxSelectIndex()) {
                this.setSelectIndex(getSelectIndex() + 1);
            }
        }
        refreshAfterPageChange();
    }

    default boolean isLoop() {
        return false;
    }

    void refreshAfterPageChange();

    void render(SpriteBatch sb);

    void update();

    int getSelectIndex();

    void setSelectIndex(int newSelectIndex);

    int getMaxSelectIndex();

    default void renderArrow(SpriteBatch sb) {
        if (this.getSelectIndex() != 0 || isLoop()) {
            getLeftArrowButton().render(sb);
        }
        if (this.getSelectIndex() < this.getMaxSelectIndex() || isLoop()) {
            getRightArrowButton().render(sb);
        }
    }

    default void updateArrow() {
        if (this.getSelectIndex() != 0 || isLoop()) {
            getLeftArrowButton().update();
        }
        if (this.getSelectIndex() < this.getMaxSelectIndex() || isLoop()) {
            getRightArrowButton().update();
        }
    }

    LeftArrowButton getLeftArrowButton();

    RightArrowButton getRightArrowButton();

    /**
     * 点击时调用pageDown()
     */
    class LeftArrowButton implements IUIElement {
        public final float w;
        public final float h;
        private final Texture arrow;
        private final Hitbox hitbox;
        private final WithLROptionsArrow owner;
        private float x;
        private float y;

        public LeftArrowButton(final float x, final float y, WithLROptionsArrow owner) {
            this.arrow = ImageMaster.CF_LEFT_ARROW;
            this.owner = owner;
            this.x = x;
            this.y = y;
            this.w = Settings.scale * this.arrow.getWidth();
            this.h = Settings.scale * this.arrow.getHeight();
            this.hitbox = new Hitbox(x, y, this.w, this.h);
        }

        public void move(final float newX, final float newY) {
            this.x = newX - this.w / 2.0f;
            this.y = newY - this.h / 2.0f;
            this.hitbox.move(newX, newY);
        }

        @Override
        public void render(final SpriteBatch sb) {
            if (this.hitbox.hovered) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            final float width = this.arrow.getWidth();
            final float height = this.arrow.getHeight();
            sb.draw(this.arrow, this.x, this.y, width, height);
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
    class RightArrowButton implements IUIElement {
        public final float w;
        public final float h;
        private final Texture arrow;
        private final Hitbox hitbox;
        private final WithLROptionsArrow owner;
        private float x;
        private float y;

        public RightArrowButton(final float x, final float y, WithLROptionsArrow owner) {
            this.arrow = ImageMaster.CF_RIGHT_ARROW;
            this.owner = owner;
            this.x = x;
            this.y = y;
            this.w = Settings.scale * this.arrow.getWidth();
            this.h = Settings.scale * this.arrow.getHeight();
            this.hitbox = new Hitbox(x, y, this.w, this.h);
        }

        public void move(final float newX, final float newY) {
            this.x = newX - this.w / 2.0f;
            this.y = newY - this.h / 2.0f;
            this.hitbox.move(newX, newY);
        }

        @Override
        public void render(final SpriteBatch sb) {
            if (this.hitbox.hovered) {
                sb.setColor(Color.WHITE);
            } else {
                sb.setColor(Color.LIGHT_GRAY);
            }
            final float width = this.arrow.getWidth();
            final float height = this.arrow.getHeight();
            sb.draw(this.arrow, this.x, this.y, width, height);
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
