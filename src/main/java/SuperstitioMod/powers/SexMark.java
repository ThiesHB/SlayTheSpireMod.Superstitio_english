package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.powers.interFace.InvisiblePower_StillRenderAmount;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class SexMark extends AbstractPower implements InvisiblePower_StillRenderAmount {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(SexMark.class.getSimpleName() + "Power");
    public static final int MARKNeeded = 5;
    protected static final float BAR_RADIUS = 50.0f * Settings.scale;
    protected static final float BAR_Blank = 20.0f * Settings.scale;
    private static final Color BALLColor = new Color(1f, 0.7529f, 0.7961f, 1.0f);
    public Set<String> sexNames = new HashSet<>();
    public Hitbox hitbox;

    public SexMark(String name, String id, final AbstractCreature owner, final String sexName) {
        this.name = name;
        this.ID = id;
        this.owner = owner;

        this.type = PowerType.BUFF;

        this.amount = 1;
        // 添加一大一小两张能力图
        String path128 = SuperstitioModSetup.makeImgFilesPath_Power("default84");
        String path48 = SuperstitioModSetup.makeImgFilesPath_Power("default32");
        this.region128 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path128), 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(ImageMaster.loadImage(path48), 0, 0, 32, 32);

        this.sexNames.add(sexName);
        this.updateDescription();


        this.hitbox = new Hitbox((BAR_RADIUS + BAR_Blank) * MARKNeeded, BAR_RADIUS);
        this.hitbox.move(this.owner.hb.cX, Height() + this.owner.hb.height / 2 + BAR_RADIUS * 3);

        Optional<SexMark> sexMark =
                this.owner.powers.stream()
                        .filter(power -> Objects.equals(power.ID, this.ID)&& power instanceof SexMark)
                        .map(power -> (SexMark) power).findFirst();
        if (!sexMark.isPresent()) {
            return;
        }
        sexMark.get().sexNames.add(sexName);
    }

    protected abstract float Height();

    private boolean isTrigger(Set<String> sexNames) {
        return sexNames.size() >= MARKNeeded;
    }

    protected void Trigger() {
        this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
    }

    @Override
    public void update(int slot) {
        super.update(slot);
        this.hitbox.update();
        if (this.hitbox.hovered) {
            TipHelper.renderGenericTip(this.hitbox.cX + 96.0F * Settings.scale,
                    this.hitbox.cY + 64.0F * Settings.scale, this.name, this.description);
        }

        this.fontScale = MathHelper.scaleLerpSnap(this.fontScale, 0.7F);
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        float X = this.hitbox.x;// / 2.0F;
        float Y = this.hitbox.cY - BAR_RADIUS;
        this.renderMarks(sb, X, Y);
    }


    public void renderMarks(final SpriteBatch sb, final float x, final float y) {
        sb.setColor(BALLColor);
        for (int i = 0; i < this.sexNames.size(); i++) {
            sb.draw(ImageMaster.ORB_LIGHTNING,
                    x + (BAR_RADIUS + BAR_Blank) * i - BAR_RADIUS,
                    y,
                    ImageMaster.ORB_LIGHTNING.getWidth(),
                    ImageMaster.ORB_LIGHTNING.getHeight());
        }
    }

    @Override
    public void stackPower(final int stackAmount) {
        if (this.amount < 0)
            this.amount = 0;
        //Bubble(false);
        super.stackPower(stackAmount);
        updateDescription();
        if (isTrigger(this.sexNames)) Trigger();
    }

//    private void Bubble(boolean isDebuff) {
////        AbstractPower power = this;
////        String name = this.name;
////        this.addToTop(new AbstractGameAction() {
////            @Override
////            public void update() {
////                this.isDone = true;
////                PowerUtility.BubbleMessage(power, isDebuff, name);
////            }
////        });
//
//    }

    @Override
    public void reducePower(int reduceAmount) {
        if (this.amount < 0)
            this.amount = 0;
        // Bubble(true);
        super.reducePower(reduceAmount);
        updateDescription();
    }

//    public void renderAmountBar(final SpriteBatch sb, final float x, final float y) {
//        sb.setColor(this.barOrginColor);
//        sb.draw(ImageMaster.HEALTH_BAR_L, x - BAR_HEIGHT, y + BAR_OFFSET_Y, BAR_HEIGHT,
//                BAR_HEIGHT);
//        sb.draw(ImageMaster.HEALTH_BAR_B, x, y + BAR_OFFSET_Y, this.barWidth(), BAR_HEIGHT);
//        sb.draw(ImageMaster.HEALTH_BAR_R, x + this.barWidth(), y + BAR_OFFSET_Y, BAR_HEIGHT,
//                BAR_HEIGHT);
//    }
}
