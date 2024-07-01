//package superstitioapi.pet.creatureManipulation;
//
//import basemod.ReflectionHacks;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.megacrit.cardcrawl.actions.animations.VFXAction;
//import com.megacrit.cardcrawl.actions.common.InstantKillAction;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.core.CardCrawlGame;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.input.InputHelper;
//import com.megacrit.cardcrawl.localization.UIStrings;
//import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
//import superstitioapi.DataUtility;
//import superstitioapi.inBattleManager.RenderInBattle;
//
//import java.util.ArrayList;
//
//public class CreatureManipulationPanel implements RenderInBattle {
//    public static final String[] TEXT;
//    private static final UIStrings UiStrings;
//
//    static {
//        UiStrings = CardCrawlGame.languagePack.getUIString(DataUtility.MakeTextID("CreatureManipulationPanel"));
//        TEXT = CreatureManipulationPanel.UiStrings.TEXT;
//    }
//
//    public AbstractCreature creature;
//    public ArrayList<CreatureManipulationButton> buttons;
//    public boolean isHidden;
//    private float oldMX;
//    private float oldMY;
//    private float drawXDiff;
//    private float drawYDiff;
//
//    public CreatureManipulationPanel(final AbstractCreature creature) {
//        this.oldMX = 0.0f;
//        this.oldMY = 0.0f;
//        this.drawXDiff = 0.0f;
//        this.drawYDiff = 0.0f;
//        this.isHidden = true;
//        this.creature = creature;
//
//        this.buttons = new ArrayList<>();
//        buttons.add(new CreatureManipulationButton(CreatureManipulationPanel.TEXT[0], () -> {
//            this.oldMX = InputHelper.mX;
//            this.oldMY = InputHelper.mY;
//            this.drawXDiff = creature.drawX - creature.hb_x;
//            this.drawYDiff = creature.drawY - creature.hb_y;
//        }, () -> {
//            ReflectionHacks.privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(creature);
//            this.oldMX = 0.0f;
//            this.oldMY = 0.0f;
//        }, () -> {
//            if (this.oldMX != 0.0f && this.oldMY != 0.0f) {
//                final float xDiff = InputHelper.mX - this.oldMX;
//                final float yDiff = InputHelper.mY - this.oldMY;
//                creature.drawX += xDiff;
//                creature.drawY += yDiff;
//            }
//            this.oldMX = InputHelper.mX;
//            this.oldMY = InputHelper.mY;
//        }));
//        this.buttons.add(new CreatureManipulationButton(CreatureManipulationPanel.TEXT[2], () -> {
//            AbstractDungeon.actionManager.addToTop(new InstantKillAction(creature));
//            if (!Settings.FAST_MODE) {
//                AbstractDungeon.actionManager.addToTop(new VFXAction(new WeightyImpactEffect(creature.hb.cX, creature.hb.cY)));
//            }
//        }, true));
//    }
//
//    @Override
//    public void render(final SpriteBatch sb) {
//        if (this.isHidden) {
//            return;
//        }
//        for (final CreatureManipulationButton cb : this.buttons) {
//            cb.render(sb);
//        }
//    }
//
//    @Override
//    public void update() {
//        if (AbstractDungeon.isScreenUp) {
//            this.isHidden = true;
//        }
//        if (this.isHidden) {
//            return;
//        }
//        boolean isHoveringButtons = false;
//        final float panelX = this.creature.hb.x + this.creature.hb.width / 2.0f;
//        float panelY = this.creature.hb.y + this.creature.hb.height / 2.0f;
//        for (final CreatureManipulationButton cb : this.buttons) {
//            cb.move(panelX, panelY);
//            panelY -= CreatureManipulationButton.ROW_RENDER_HEIGHT;
//            cb.update();
//            if (cb.hb.hovered) {
//                isHoveringButtons = true;
//            }
//        }
//        if (!isHoveringButtons && (InputHelper.justClickedLeft || InputHelper.justClickedRight)) {
//            this.isHidden = true;
//        }
//    }
//
//    public void resetAllButtons() {
//        for (final CreatureManipulationButton cb : this.buttons) {
//            cb.askingConfirmation = false;
//        }
//    }
//}
