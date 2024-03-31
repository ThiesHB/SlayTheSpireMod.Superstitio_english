package SuperstitioMod.utils;

/*import basemod.abstracts.CustomEnergyOrb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Interpolation;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class EnergyFreeFormOrb extends CustomEnergyOrb {
    public static final int SECOND_ORB_W = 128;
    public static final int PRIMARY_ORB_W = 128;
    public static final float SECOND_ORB_IMG_SCALE;
    public static final float PRIMARY_ORB_IMG_SCALE;
    public static final float X_OFFSET;
    public static final float Y_OFFSET;
    protected Texture secondBaseLayer;
    protected Texture[] secondEnergyLayers;
    protected Texture[] secondNoEnergyLayers;
    protected float[] secondLayerSpeeds;
    protected float[] secondAngles;
    private Texture mask;
    private FrameBuffer fbo;
    public static float secondVfxTimer;
    private static float secondEnergyVfxAngle;
    private static float secondEnergyVfxScale;
    private static Color secondEnergyVfxColor;
    private static Hitbox hb;
    private static UIStrings uiStrings;

    public EnergyFreeFormOrb(final String[] orbTexturePaths, final String orbVfxPath, float[] layerSpeeds, final String[] orbTexturePathsAlt, final String orbVfxPathAlt) {
        super(orbTexturePaths, orbVfxPath, layerSpeeds);
        final int numLayers = 5;
        this.secondEnergyLayers = new Texture[numLayers];
        this.secondNoEnergyLayers = new Texture[numLayers];
        assert orbTexturePathsAlt.length >= 3;
        assert orbTexturePathsAlt.length % 2 == 1;
        final int middleIdx = orbTexturePathsAlt.length / 2;
        this.secondEnergyLayers = new Texture[middleIdx];
        this.secondNoEnergyLayers = new Texture[middleIdx];
        for (int i = 0; i < middleIdx; ++i) {
            this.secondEnergyLayers[i] = ImageMaster.loadImage(orbTexturePathsAlt[i]);
            this.secondNoEnergyLayers[i] = ImageMaster.loadImage(orbTexturePathsAlt[i + middleIdx + 1]);
        }
        this.secondBaseLayer = TextureLoader.getTexture("collectorResources/images/char/mainChar/orb/alt/layer6.png");
        this.orbVfx = ImageMaster.loadImage(orbVfxPath);
        if (layerSpeeds == null) {
            layerSpeeds = new float[] { -20.0f, 20.0f, -40.0f, 40.0f, 360.0f };
        }
        this.secondLayerSpeeds = layerSpeeds;
        this.secondAngles = new float[this.secondLayerSpeeds.length];
        assert this.secondEnergyLayers.length == this.secondLayerSpeeds.length;
        this.fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);
        this.mask = TextureLoader.getTexture("collectorResources/images/char/mainChar/orb/mask.png");
    }

    public Texture getEnergyImage() {
        return this.orbVfx;
    }

    public void updateOrb(final int energyCount) {
        super.updateOrb(energyCount);
        final int d = this.secondAngles.length;
        for (int i = 0; i < this.secondAngles.length; ++i) {
            if (energyCount == 0) {
                final float[] secondAngles = this.secondAngles;
                final int n = i;
                secondAngles[n] -= Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d - 1 - i] / 4.0f;
            }
            else {
                final float[] secondAngles2 = this.secondAngles;
                final int n2 = i;
                secondAngles2[n2] -= Gdx.graphics.getDeltaTime() * this.secondLayerSpeeds[d - 1 - i];
            }
        }
        if (EnergyFreeFormOrb.secondVfxTimer != 0.0f) {
            EnergyFreeFormOrb.secondEnergyVfxColor.a = Interpolation.exp10In.apply(0.5f, 0.0f, 1.0f - EnergyFreeFormOrb.secondVfxTimer / 2.0f);
            EnergyFreeFormOrb.secondEnergyVfxAngle += Gdx.graphics.getDeltaTime() * -30.0f;
            EnergyFreeFormOrb.secondEnergyVfxScale = Settings.scale * Interpolation.exp10In.apply(1.0f, 0.1f, 1.0f - EnergyFreeFormOrb.secondVfxTimer / 2.0f);
            EnergyFreeFormOrb.secondVfxTimer -= Gdx.graphics.getDeltaTime();
            if (EnergyFreeFormOrb.secondVfxTimer < 0.0f) {
                EnergyFreeFormOrb.secondVfxTimer = 0.0f;
                EnergyFreeFormOrb.secondEnergyVfxColor.a = 0.0f;
            }
        }
        EnergyFreeFormOrb.hb.update();
    }

    public void renderOrb(final SpriteBatch sb, final boolean enabled, final float current_x, final float current_y) {
        EnergyFreeFormOrb.hb.move(current_x + EnergyFreeFormOrb.X_OFFSET, current_y + EnergyFreeFormOrb.Y_OFFSET);
        sb.setColor(Color.WHITE);
        if (AbstractDungeon.player.chosenClass.equals((Object)CollectorChar.Enums.THE_COLLECTOR) || NewReserves.reserveCount() > 0) {
            if (NewReserves.reserveCount() > 0) {
                for (int i = 0; i < this.secondEnergyLayers.length; ++i) {
                    sb.draw(this.secondEnergyLayers[i], current_x + EnergyFreeFormOrb.X_OFFSET - 64.0f, current_y + EnergyFreeFormOrb.Y_OFFSET - 64.0f, 64.0f, 64.0f, 128.0f, 128.0f, EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE, EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, 128, 128, false, false);
                }
            }
            else {
                for (int i = 0; i < this.secondNoEnergyLayers.length; ++i) {
                    sb.draw(this.secondNoEnergyLayers[i], current_x + EnergyFreeFormOrb.X_OFFSET - 64.0f, current_y + EnergyFreeFormOrb.Y_OFFSET - 64.0f, 64.0f, 64.0f, 128.0f, 128.0f, EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE, EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE, this.secondAngles[i], 0, 0, 128, 128, false, false);
                }
            }
            sb.draw(this.secondBaseLayer, current_x + EnergyFreeFormOrb.X_OFFSET - 64.0f, current_y + EnergyFreeFormOrb.Y_OFFSET - 64.0f, 64.0f, 64.0f, 128.0f, 128.0f, EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE, EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE, 0.0f, 0, 0, 128, 128, false, false);
        }
        sb.setColor(Color.WHITE);
        sb.end();
        this.fbo.begin();
        Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        Gdx.gl.glClear(16640);
        Gdx.gl.glColorMask(true, true, true, true);
        sb.begin();
        sb.setColor(Color.WHITE);
        sb.setBlendFunction(770, 771);
        if (enabled) {
            for (int i = 0; i < this.energyLayers.length; ++i) {
                sb.draw(this.energyLayers[i], current_x - 64.0f, current_y - 64.0f, 64.0f, 64.0f, 128.0f, 128.0f, EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }
        else {
            for (int i = 0; i < this.noEnergyLayers.length; ++i) {
                sb.draw(this.noEnergyLayers[i], current_x - 64.0f, current_y - 64.0f, 64.0f, 64.0f, 128.0f, 128.0f, EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, this.angles[i], 0, 0, 128, 128, false, false);
            }
        }
        if (EnergyFreeFormOrb.hb.hovered && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.isScreenUp) {
            TipHelper.renderGenericTip(50.0f * Settings.scale, 380.0f * Settings.scale, EnergyFreeFormOrb.uiStrings.TEXT[0], EnergyFreeFormOrb.uiStrings.TEXT[1]);
        }
        sb.setBlendFunction(0, 770);
        sb.setColor(new Color(1.0f, 1.0f, 1.0f, 1.0f));
        sb.draw(this.mask, current_x - 64.0f, current_y - 64.0f, 64.0f, 64.0f, 128.0f, 128.0f, 1.0f, 1.0f, 0.0f, 0, 0, 128, 128, false, false);
        sb.setBlendFunction(770, 771);
        sb.end();
        this.fbo.end();
        sb.begin();
        final TextureRegion drawTex = new TextureRegion((Texture)this.fbo.getColorBufferTexture());
        drawTex.flip(false, true);
        sb.draw(drawTex, (float)(-Settings.VERT_LETTERBOX_AMT), (float)(-Settings.HORIZ_LETTERBOX_AMT));
        sb.draw(this.baseLayer, current_x - 64.0f, current_y - 64.0f, 64.0f, 64.0f, 128.0f, 128.0f, EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, 0.0f, 0, 0, 128, 128, false, false);
        EnergyFreeFormOrb.hb.render(sb);
    }

    static {
        SECOND_ORB_IMG_SCALE = 0.75f * Settings.scale;
        PRIMARY_ORB_IMG_SCALE = 1.15f * Settings.scale;
        X_OFFSET = 100.0f * Settings.scale;
        Y_OFFSET = 0.0f * Settings.scale;
        EnergyFreeFormOrb.secondVfxTimer = 0.0f;
        EnergyFreeFormOrb.secondEnergyVfxAngle = 0.0f;
        EnergyFreeFormOrb.secondEnergyVfxScale = Settings.scale;
        EnergyFreeFormOrb.secondEnergyVfxColor = Color.WHITE.cpy();
        EnergyFreeFormOrb.hb = new Hitbox(80.0f * Settings.scale, 80.0f * Settings.scale);
        EnergyFreeFormOrb.uiStrings = CardCrawlGame.languagePack.getUIString("collector:SecondEnergyOrb");
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "<class>")
    public static class DoubleOrbField
    {
        public static SpireField<Boolean> isDoubleOrb;

        static {
            DoubleOrbField.isDoubleOrb = new SpireField<Boolean>(() -> Boolean.FALSE);
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "renderVfx")
    public static class FlashSecondOrbPatch
    {
        @SpirePrefixPatch
        public static void flashSecondOrb(final EnergyPanel __instance, final SpriteBatch sb, final Texture ___gainEnergyImg, final Color ___energyVfxColor, final float ___energyVfxScale, final float ___energyVfxAngle) {
            if ((boolean)DoubleOrbField.isDoubleOrb.get((Object)AbstractDungeon.player) && EnergyFreeFormOrb.secondVfxTimer > 0.0f) {
                sb.setBlendFunction(770, 1);
                sb.setColor(EnergyFreeFormOrb.secondEnergyVfxColor);
                sb.draw(___gainEnergyImg, __instance.current_x + EnergyFreeFormOrb.X_OFFSET - 128.0f, __instance.current_y + EnergyFreeFormOrb.Y_OFFSET - 128.0f, 128.0f, 128.0f, 256.0f, 256.0f, EnergyFreeFormOrb.secondEnergyVfxScale * EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE / EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, EnergyFreeFormOrb.secondEnergyVfxScale * EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE / EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, EnergyFreeFormOrb.secondEnergyVfxAngle - 50.0f, 0, 0, 256, 256, true, false);
                sb.draw(___gainEnergyImg, __instance.current_x + EnergyFreeFormOrb.X_OFFSET - 128.0f, __instance.current_y + EnergyFreeFormOrb.Y_OFFSET - 128.0f, 128.0f, 128.0f, 256.0f, 256.0f, EnergyFreeFormOrb.secondEnergyVfxScale * EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE / EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, EnergyFreeFormOrb.secondEnergyVfxScale * EnergyFreeFormOrb.SECOND_ORB_IMG_SCALE / EnergyFreeFormOrb.PRIMARY_ORB_IMG_SCALE, -EnergyFreeFormOrb.secondEnergyVfxAngle, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }
        }
    }
}
*/