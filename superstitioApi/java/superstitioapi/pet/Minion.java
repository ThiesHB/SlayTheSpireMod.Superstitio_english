package superstitioapi.pet;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import superstitioapi.pet.animationSize.AnimationSize;

import static basemod.ReflectionHacks.getPrivate;
import static basemod.ReflectionHacks.privateMethod;

public abstract class Minion extends CustomMonster {
    public static final int HEALTH_DIV = 10;
    public static final float DEFAULT_DRAW_SCALE = 3f;
    protected final float drawScale;
    protected final AbstractCreature petCore;
    protected Hitbox petCoreHitbox;
    private float oldMX;
    private float oldMY;
    private float simpleAnim = 0.0f;

    protected Minion(AbstractCreature petCore, float drawScale) {
        super(petCore.name, petCore.id, petCore.maxHealth,
                petCore.hb_x, petCore.hb_y, petCore.hb_w / drawScale, petCore.hb_h / drawScale, null, 0, 0, true);
        this.animX = 0.0f;
        this.animY = 0.0f;
        this.drawScale = drawScale;
        this.petCore = petCore;
        if (getPrivate(petCore, AbstractCreature.class, "atlas") != null)
            try {
                AnimationSize.reloadAnimation(petCore, drawScale);
            } catch (Exception e) {
                this.hb_w *= drawScale;
                this.hb_h *= drawScale;
            }
        this.dialogX = this.petCore.dialogX;
        this.dialogY = this.petCore.dialogY;

        privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(petCore);

        this.maxHealth = petCore.maxHealth / HEALTH_DIV;
        this.currentHealth = maxHealth;
        this.flipHorizontal = true;
        this.petCore.maxHealth = petCore.maxHealth / HEALTH_DIV;
        this.petCore.currentHealth = maxHealth;
        this.petCore.flipHorizontal = true;
        this.tips = getPrivate(petCore, AbstractCreature.class, "tips");

        this.img = setupImg();
    }

    public Minion(AbstractCreature petCore) {
        this(petCore, DEFAULT_DRAW_SCALE);
    }

    public static boolean isCreatureHovered(AbstractCreature creature) {
        return creature.hb.hovered || creature.healthHb.hovered;
    }

    protected abstract Texture setupImg();

    @Override
    public abstract void createIntent();

    @Override
    public void init() {
        getPetCore().drawX = drawX;
        getPetCore().drawY = drawY;
        refreshHitBox();
        this.getPetCore().healthHb = new Hitbox(this.hb_w, 72.0F * Settings.scale);
        privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(this.getPetCore());
    }

    private void refreshHitBox() {
        this.getPetCore().hb_x = this.hb_x;
        this.getPetCore().hb_y = this.hb_y;
        this.getPetCore().hb = new Hitbox(this.hb_w, this.hb_h);
        this.getPetCore().hb_w = this.getPetCore().hb.width;
        this.getPetCore().hb_h = this.getPetCore().hb.height;
        this.getPetCore().hb.move(getPetCore().drawX, getPetCore().drawY);
        this.petCoreHitbox = this.getPetCore().hb;
    }

    @Override
    public void showHealthBar() {
        this.getPetCore().showHealthBar();
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        this.getPetCore().damage(info);
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.img != null)
            this.getPetCore().tint.color.a = 0;
        this.getPetCore().render(sb);
        if (this.isDead || this.escaped) return;
        if (this.atlas != null) {
            return;
        }
        sb.setColor(this.tint.color);
        if (this.img != null) {
            drawImg(sb);
        }
//        if (this == AbstractDungeon.getCurrRoom().monsters.hoveredMonster) {
//            sb.setBlendFunction(770, 1);
//            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
//            if (this.img != null) {
//                drawImg(sb);
//                sb.setBlendFunction(770, 771);
//            }
//        }
    }

    private void drawImg(SpriteBatch sb) {
        simpleAnim += Gdx.graphics.getDeltaTime();
        if (simpleAnim >= 1.0f)
            simpleAnim = 0;
        float scaleX = 1.0f;
        float v = 0.005f * MathUtils.sinDeg(MathUtils.sinDeg(simpleAnim * 360) * 15);
        float scaleY = 1.0f + v;
        float rotation = 0;
        sb.draw(this.img, this.drawX - (float) this.img.getWidth() / drawScale * Settings.scale / 2.0F + this.animX,
                this.drawY + this.hb.height * v,
                0, 0,
                (float) this.img.getWidth() / drawScale * Settings.scale,
                (float) this.img.getHeight() / drawScale * Settings.scale,
                scaleX, scaleY, rotation,
                0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
    }

    @Override
    public abstract void applyPowers();

    protected abstract void updatePetCore();

    @Override
    public void update() {
        if (getPetCore().hb != petCoreHitbox)
            refreshHitBox();
        super.update();
        updatePetCore();

        if (!AbstractDungeon.player.isDraggingCard && AbstractDungeon.player.hoveredCard == null && isHovered()) {
            if (InputHelper.justClickedLeft) {
                this.Drag_Press();
            } else if (InputHelper.isMouseDown) {
                this.Drag_Hold();
            } else if (InputHelper.justReleasedClickLeft) {
                this.Drag_Release();
            }
        }
    }

    public boolean isHovered() {
        return isCreatureHovered(this) || isCreatureHovered(this.petCore);
    }

    protected void Drag_Press() {
        this.oldMX = InputHelper.mX;
        this.oldMY = InputHelper.mY;
    }

    protected void Drag_Release() {
        this.refreshHitboxLocation();
        ReflectionHacks.privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(getPetCore());
        this.oldMX = 0.0f;
        this.oldMY = 0.0f;
    }

    protected void Drag_Hold() {
        if (this.oldMX != 0.0f && this.oldMY != 0.0f) {
            final float xDiff = InputHelper.mX - this.oldMX;
            final float yDiff = InputHelper.mY - this.oldMY;
            this.drawX += xDiff;
            this.getPetCore().drawX += xDiff;
            this.drawY += yDiff;
            this.getPetCore().drawY += yDiff;
        }
        this.oldMX = InputHelper.mX;
        this.oldMY = InputHelper.mY;
    }

    @Override
    public abstract void renderTip(SpriteBatch sb);

    @Override
    public abstract void takeTurn();

    public void updateHitBox() {
        getPetCore().hb.update();
        getPetCore().healthHb.update();
    }

    @Override
    public void rollMove() {
        this.getMove(AbstractDungeon.aiRng.random(99));
    }

    @Override
    public abstract void updatePowers();

    @Override
    public abstract void usePreBattleAction();

    @Override
    public abstract void useUniversalPreBattleAction();

    @Override
    protected abstract void getMove(int i);

    public final AbstractCreature getPetCore() {
        return petCore;
    }

}
