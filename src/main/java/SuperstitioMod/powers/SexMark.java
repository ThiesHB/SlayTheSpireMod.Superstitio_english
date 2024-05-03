package SuperstitioMod.powers;

import SuperstitioMod.powers.interFace.InvisiblePower_StillRenderAmount;
import SuperstitioMod.powers.interFace.OnPostApplyThisPower;
import SuperstitioMod.utils.CardUtility;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.TipHelper;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public abstract class SexMark extends AbstractLupaPower implements InvisiblePower_StillRenderAmount, OnPostApplyThisPower {
    public static final int MARKNeeded = 5;
    protected static final float BAR_RADIUS = 50.0f * Settings.scale;
    protected static final float BAR_Blank = 20.0f * Settings.scale;
    private static final Color BALLColor = new Color(1f, 0.7529f, 0.7961f, 1.0f);
    public Set<String> sexNames = new HashSet<>();
    public Hitbox hitbox;

    public String tempSexName;

    public SexMark(String id, final AbstractCreature owner, final String sexName) {
        super(id, owner, 1, PowerType.BUFF, false);

        this.hitbox = new Hitbox((BAR_RADIUS + BAR_Blank) * MARKNeeded, BAR_RADIUS);
        this.hitbox.move(this.owner.hb.cX, Height() + this.owner.hb.height / 2 + BAR_RADIUS * 3);
        this.tempSexName = sexName;
        this.sexNames.add(tempSexName);
    }

    public static int FindJobAndFuckCard() {
        return (int) CardUtility.AllCardInBattle_ButWithoutCardInUse().stream()
                .filter(card -> card.cardID.contains("Job") || card.cardID.contains("Fuck")).count();
    }

    @Override
    public void InitializePostApplyThisPower() {
        Optional<SexMark> sexMark =
                this.owner.powers.stream()
                        .filter(power -> Objects.equals(power.ID, this.ID) && power instanceof SexMark)
                        .map(power -> (SexMark) power).findAny();
        sexMark.ifPresent(mark -> {
            mark.sexNames.add(tempSexName);
//            SuperstitioModSetup.logger.info(mark+"  "+tempSexName);
        });
        this.updateDescription();
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
        update_showTips(this.hitbox);
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
        super.stackPower(stackAmount);
        updateDescription();
        if (isTrigger(this.sexNames)) Trigger();
    }

    @Override
    public void reducePower(int reduceAmount) {
        if (this.amount < 0)
            this.amount = 0;
        super.reducePower(reduceAmount);
        updateDescription();
    }
}
