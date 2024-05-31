package superstitioapi.powers.barIndepend;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;

import java.util.function.Supplier;

import static superstitioapi.DataUtility.makeShaderPath;
import static superstitioapi.utils.ImgUtility.draw;

public class BarRenderOnThing_Ring extends BarRenderOnThing {
    public final static FrameBuffer buffer =
            new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
    public final static ShaderProgram ringShader = initShader("ring_vertex.glsl", "ring_fragment.glsl");
    protected static final float BAR_SIZE = BAR_DIAMETER * 6.0f;
    protected static final float BAR_THICK = BAR_DIAMETER * 1.2f;
    protected static final float BAR_HALF_THICK_RENORMALIZATION = BAR_THICK / 2.0f / BAR_SIZE;
    //实际上是内径和外径的平均值
    protected static final float BAR_AVERAGE_RADIUS_RENORMALIZATION = 0.5f - BAR_HALF_THICK_RENORMALIZATION;
    protected static final float BAR_TOP_DEGREE =
            BAR_HALF_THICK_RENORMALIZATION * 2 / (BAR_AVERAGE_RADIUS_RENORMALIZATION * 3.14f / 180.0f);
    protected static final float BAR_DEGREE = 240;
    protected static final float INIT_DEGREE = -30;
    protected static final float DEGREE_OFFSET = 90;
    private static final TextureRegion BAR_SHADOW_L = new TextureRegion(ImageMaster.HB_SHADOW_L);
    private static final TextureRegion BAR_SHADOW_B = new TextureRegion(ImageMaster.HB_SHADOW_B);
    private static final TextureRegion BAR_SHADOW_R = new TextureRegion(ImageMaster.HB_SHADOW_R);
    private static final TextureRegion BAR_L = new TextureRegion(ImageMaster.HEALTH_BAR_L);
    private static final TextureRegion BAR_B = new TextureRegion(ImageMaster.HEALTH_BAR_B);
    private static final TextureRegion BAR_R = new TextureRegion(ImageMaster.HEALTH_BAR_R);
    public static ShapeRenderer shapeRenderer = new ShapeRenderer();
    //    private final Mesh rectMesh;
    public ShaderProgram originShader;
    protected float initDegree;
    protected float barAverageRadius_renormalization;
    protected float barHalfThick_renormalization;
    protected float barSize;
    protected float barTopDegree;

    public BarRenderOnThing_Ring(Supplier<Hitbox> hitbox, HasBarRenderOnCreature power) {
        super(hitbox, power);
//        ringShader
//        rectMesh = initMesh();
        this.barLength = BAR_DEGREE;
        this.initDegree = INIT_DEGREE;
        this.barSize = BAR_SIZE;
        this.barHalfThick_renormalization = BAR_HALF_THICK_RENORMALIZATION;
        this.barAverageRadius_renormalization = BAR_AVERAGE_RADIUS_RENORMALIZATION;
        this.barTopDegree = BAR_TOP_DEGREE;
        RingHitBox ringHitBox = new RingHitBox(this);
        ringHitBox.halfThick *= 1.1f;
        ringHitBox.averageRadius *= 1.1f;
        this.hitbox = ringHitBox;
        this.hitbox.width = barSize;
        this.hitbox.height = barSize;
        updateHitBoxPlace(this.hitbox);
    }

    public static ShaderProgram initShader(String vertexShaderName, String fragmentShaderName) { // 初始化着色器程序
        String vertexShader = Gdx.files.internal(makeShaderPath(vertexShaderName)).readString();
        String fragmentShader = Gdx.files.internal(makeShaderPath(fragmentShaderName)).readString();
        ShaderProgram shaderProgram = new ShaderProgram(vertexShader, fragmentShader);
        if (!shaderProgram.isCompiled())
            throw new RuntimeException(shaderProgram.getLog());
        return shaderProgram;
    }

    @Override
    protected float getYDrawStart() {
        return this.hitbox.cY - barSize / 2.0f;
    }

    @Override
    protected float getXDrawStart() {
        return this.hitbox.cX - barSize / 2.0f;
    }

    @Override
    protected void renderBar(SpriteBatch sb) {
        originShader = sb.getShader();
        this.renderAmountBarBackGround(sb, getXDrawStart(), getYDrawStart());
        this.amountChunkWithUuid.values().forEach(amountChunk -> amountChunk.render(sb));
        sb.setShader(originShader);
    }

    @Override
    protected void drawBar(SpriteBatch sb, float x, float y, float startLength, float length) {
        sb.setShader(ringShader);
        sb.getShader().setUniformf("u_degreeStart", initDegree + startLength);
        sb.getShader().setUniformf("u_degreeLength", length);
        sb.getShader().setUniformf("u_radius", barAverageRadius_renormalization);
        sb.getShader().setUniformf("u_halfThick", barHalfThick_renormalization);
        sb.draw(ImageMaster.HEALTH_BAR_B, x, y, barSize, barSize);

        if (length >= 360.0f) return;
        sb.setShader(originShader);
        draw(sb, ImageMaster.HEALTH_BAR_R,
                this.hitbox.cX - barSize * MathUtils.cosDeg(initDegree + startLength) * (barAverageRadius_renormalization + barHalfThick_renormalization),
                this.hitbox.cY + barSize * MathUtils.sinDeg(initDegree + startLength) * (barAverageRadius_renormalization + barHalfThick_renormalization),
                barHalfThick_renormalization * 2 * barSize, barHalfThick_renormalization * 2 * barSize,
                -(DEGREE_OFFSET + initDegree + startLength));
        draw(sb, ImageMaster.HEALTH_BAR_L,
                this.hitbox.cX - barSize * MathUtils.cosDeg(initDegree + startLength + length) * (barAverageRadius_renormalization - barHalfThick_renormalization),
                this.hitbox.cY + barSize * MathUtils.sinDeg(initDegree + startLength + length) * (barAverageRadius_renormalization - barHalfThick_renormalization),
                barHalfThick_renormalization * 2 * barSize, barHalfThick_renormalization * 2 * barSize,
                -(DEGREE_OFFSET + 180 + initDegree + startLength + length), true, true);
    }

    @Override
    protected void drawBarShadow(SpriteBatch sb, float x, float y, float startLength, float length) {
        sb.setShader(ringShader);
        sb.getShader().setUniformf("u_degreeStart", initDegree + startLength);
        sb.getShader().setUniformf("u_degreeLength", length);
        sb.getShader().setUniformf("u_radius", barAverageRadius_renormalization);
        sb.getShader().setUniformf("u_halfThick", barHalfThick_renormalization);
        sb.draw(ImageMaster.HB_SHADOW_B, x, y, barSize, barSize);

        if (length >= 360.0f) return;
        sb.setShader(originShader);
        draw(sb, ImageMaster.HB_SHADOW_R,
                this.hitbox.cX - barSize * MathUtils.cosDeg(initDegree + startLength) * (barAverageRadius_renormalization + barHalfThick_renormalization),
                this.hitbox.cY + barSize * MathUtils.sinDeg(initDegree + startLength) * (barAverageRadius_renormalization + barHalfThick_renormalization),
                barHalfThick_renormalization * 2 * barSize, barHalfThick_renormalization * 2 * barSize,
                -(DEGREE_OFFSET + initDegree + startLength));
        sb.setShader(originShader);
        draw(sb, ImageMaster.HB_SHADOW_L,
                this.hitbox.cX - barSize * MathUtils.cosDeg(initDegree + startLength + length) * (barAverageRadius_renormalization - barHalfThick_renormalization),
                this.hitbox.cY + barSize * MathUtils.sinDeg(initDegree + startLength + length) * (barAverageRadius_renormalization - barHalfThick_renormalization),
                barHalfThick_renormalization * 2 * barSize, barHalfThick_renormalization * 2 * barSize,
                -(DEGREE_OFFSET + 180 + initDegree + startLength + length), true, true);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void render(SpriteBatch sb) {
        renderBar(sb);
        renderBarText(sb, getXDrawStart(), getYDrawStart());
        if (Settings.isDebug || Settings.isInfo) {
            renderDebug(sb);
        }
    }

    @Override
    protected AmountChunk makeNewAmountChunk(BarRenderUpdateMessage message) {
        AmountChunk amountChunk = super.makeNewAmountChunk(message);
        if (!(amountChunk instanceof BarAmountChunk)) return amountChunk;
        BarAmountChunk barAmountChunk = (BarAmountChunk) amountChunk;
        RingHitBox ringHitBox = new RingHitBox(this);
        ringHitBox.startDegree = initDegree + barAmountChunk.startLength - this.barTopDegree;
        ringHitBox.lengthDegree = this.barTopDegree * 2 + barAmountChunk.length;
        ringHitBox.averageRadius = this.barSize * barAverageRadius_renormalization;
        ringHitBox.halfThick = this.barSize * barHalfThick_renormalization;
        barAmountChunk.hitbox = ringHitBox;
        return amountChunk;
    }

    @Override
    protected void chunkHitBoxReSize(BarAmountChunk amountChunk) {
        if (!(amountChunk.hitbox instanceof RingHitBox)) {
            super.chunkHitBoxReSize(amountChunk);
            return;
        }
        RingHitBox ringHitBox = (RingHitBox) amountChunk.hitbox;
        ringHitBox.startDegree = initDegree + amountChunk.startLength - this.barTopDegree;
        ringHitBox.lengthDegree = this.barTopDegree * 2 + amountChunk.length;
        ringHitBox.averageRadius = this.barSize * barAverageRadius_renormalization;
        ringHitBox.halfThick = this.barSize * barHalfThick_renormalization;
        ringHitBox.width = this.barSize;
        ringHitBox.height = this.barSize;
        ringHitBox.move(this.hitbox.cX, this.hitbox.cY);
    }

    public static class RingHitBox extends Hitbox {
        protected float startDegree;
        protected float lengthDegree;
        protected float halfThick;
        protected float averageRadius;
        protected ShaderProgram originShader;

        public RingHitBox(float averageRadius, float halfThick, float startDegree, float lengthDegree) {
            super(2 * (averageRadius + halfThick), 2 * (averageRadius + halfThick));
            this.averageRadius = averageRadius;
            this.halfThick = halfThick;
            this.startDegree = startDegree;
            this.lengthDegree = lengthDegree;
        }

        public RingHitBox(BarRenderOnThing_Ring ring) {
            this(
                    ring.barSize * ring.barAverageRadius_renormalization,
                    ring.barSize * ring.barHalfThick_renormalization,
                    ring.initDegree - ring.barTopDegree,
                    ring.barTopDegree * 2 + ring.barLength);
        }

        @Override
        public void update(float x, float y) {
            if (AbstractDungeon.isFadingOut) return;
            this.translate(x, y);
            if (this.justHovered) {
                this.justHovered = false;
            }

            if (this.hovered) {
                this.hovered = isHovered(x, y);
            }
            else {
                this.hovered = isHovered(x, y);
                if (this.hovered) {
                    this.justHovered = true;
                }
            }
        }

        @Override
        public void render(SpriteBatch sb) {
            if (!Settings.isDebug && !Settings.isInfo) return;
            if (this.clickStarted) {
                sb.setColor(Color.CHARTREUSE);
            }
            else {
                sb.setColor(Color.RED);
            }

//            originShader = sb.getShader();
//            sb.setShader(ringShader);
//            sb.getShader().setUniformf("u_degreeStart", startDegree);
//            sb.getShader().setUniformf("u_degreeLength", startDegree - endDegree);
//            sb.getShader().setUniformf("u_radius", averageRadius / width);
//            sb.getShader().setUniformf("u_halfThick", halfThick / width);

            sb.draw(ImageMaster.DEBUG_HITBOX_IMG, this.x, this.y, this.width, this.height);
//            sb.setShader(originShader);
        }

        private boolean isHovered(float x, float y) {
            float mx = InputHelper.mX - this.cX;
            float my = InputHelper.mY - this.cY;
            float theta = (float) (MathUtils.radiansToDegrees * Math.atan2(my, -mx));
            float radius = (float) Math.sqrt(mx * mx + my * my);
            theta += 180.0f;
            theta = Math.floorMod((long) theta, (long) 360.0);
            theta -= 180.0f;

            return ((startDegree < theta && theta < startDegree + lengthDegree)
                    || (startDegree < theta - 360 && theta - 360 < startDegree + lengthDegree)
                    || (startDegree < theta + 360 && theta + 360 < startDegree + lengthDegree))
                    && averageRadius - halfThick < radius && radius < averageRadius + halfThick;
        }
    }
}
