//package superstitioapi.pet.creatureManipulation;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.helpers.FontHelper;
//import com.megacrit.cardcrawl.helpers.Hitbox;
//import com.megacrit.cardcrawl.helpers.ImageMaster;
//import com.megacrit.cardcrawl.helpers.input.InputHelper;
//import com.megacrit.cardcrawl.ui.buttons.CardSelectConfirmButton;
//import superstitioapi.utils.ActionUtility.VoidSupplier;
//import superstitioapi.utils.RenderInBattle;
//
//public class CreatureManipulationButton implements RenderInBattle
//{
//    public Hitbox hb;
//    public boolean isHidden;
//    private String labelText;
//    public VoidSupplier onPress;
//    public VoidSupplier onRelease;
//    public VoidSupplier onHold;
//    private boolean requiresConfirmation;
//    public boolean askingConfirmation;
//    public static final float ROW_HEIGHT = 50.0f * Settings.scale;
//    public static final float ROW_WIDTH = 256.0f * Settings.scale;
//    public static final float ROW_RENDER_HEIGHT = 64.0f * Settings.scale;
//    private static final Color ROW_BG_COLOR = new Color(588124159);
//    private static final Color ROW_HOVER_COLOR = new Color(-193);
//    private static final Color ROW_SELECT_COLOR = new Color(-1924910337);
//    private static final Color TEXT_DEFAULT_COLOR = Settings.CREAM_COLOR;
//    private static final Color TEXT_FOCUSED_COLOR = Settings.GREEN_TEXT_COLOR;
//    private static final Color TEXT_HOVERED_COLOR = Settings.GOLD_COLOR;
//    private static final float ROW_TEXT_Y_OFFSET = 12.0f * Settings.scale;
//    private static final float ROW_TEXT_LEADING_OFFSET = 40.0f * Settings.scale;
//
//    public CreatureManipulationButton(final String labelText, final VoidSupplier onRelease) {
//        this(labelText, () -> {}, onRelease, () -> {});
//    }
//
//    public CreatureManipulationButton(final String labelText, final VoidSupplier onRelease, final boolean requiresConfirmation) {
//        this(labelText, () -> {}, onRelease, () -> {}, requiresConfirmation);
//    }
//
//    public CreatureManipulationButton(final String labelText, final VoidSupplier onPress, final VoidSupplier onRelease, final VoidSupplier onHold) {
//        this.requiresConfirmation = false;
//        this.askingConfirmation = false;
//        this.labelText = labelText;
//        this.onRelease = onRelease;
//        this.onHold = onHold;
//        this.onPress = onPress;
//        this.hb = new Hitbox(CreatureManipulationButton.ROW_WIDTH, CreatureManipulationButton.ROW_RENDER_HEIGHT);
//        this.isHidden = false;
//    }
//
//    public CreatureManipulationButton(final String labelText, final VoidSupplier onPress, final VoidSupplier onRelease, final VoidSupplier onHold, final boolean requiresConfirmation) {
//        this(labelText, onPress, onRelease, onHold);
//        this.requiresConfirmation = requiresConfirmation;
//    }
//
//    @Override
//    public void render(final SpriteBatch sb) {
//        if (!this.isHidden) {
//            sb.setBlendFunction(770, 1);
//            sb.setColor(this.getRowBgColor());
//            sb.draw(ImageMaster.INPUT_SETTINGS_ROW, this.hb.x, this.hb.y, CreatureManipulationButton.ROW_WIDTH, CreatureManipulationButton.ROW_RENDER_HEIGHT);
//            sb.setBlendFunction(770, 771);
//            sb.setColor(Color.WHITE);
//            final Color textColor = this.getTextColor();
//            final float textY = this.hb.cY + CreatureManipulationButton.ROW_TEXT_Y_OFFSET;
//            final float textX = this.hb.x + CreatureManipulationButton.ROW_TEXT_LEADING_OFFSET;
//            final String renderText = this.askingConfirmation ? CardSelectConfirmButton.TEXT[0] : this.labelText;
//            FontHelper.renderFont(sb, FontHelper.topPanelInfoFont, renderText, textX, textY, textColor);
//            this.hb.render(sb);
//        }
//    }
//
//    @Override
//    public void update() {
//        this.hb.update();
//        if (!this.isHidden && this.hb.hovered) {
//            if (InputHelper.justClickedLeft) {
//                this.onPress.get();
//            }
//            else if (InputHelper.isMouseDown) {
//                this.onHold.get();
//            }
//            else if (InputHelper.justReleasedClickLeft) {
//                if (this.requiresConfirmation) {
//                    if (!this.askingConfirmation) {
//                        this.askingConfirmation = true;
//                    }
//                    else {
//                        this.onRelease.get();
//                        this.askingConfirmation = false;
//                    }
//                }
//                else {
//                    this.onRelease.get();
//                }
//            }
//        }
//    }
//
//    protected Color getRowBgColor() {
//        Color bgColor = CreatureManipulationButton.ROW_BG_COLOR;
//        if (this.hb.hovered && InputHelper.isMouseDown) {
//            bgColor = CreatureManipulationButton.ROW_SELECT_COLOR;
//        }
//        else if (this.askingConfirmation) {
//            bgColor = Color.SALMON;
//        }
//        else if (this.hb.hovered) {
//            bgColor = CreatureManipulationButton.ROW_HOVER_COLOR;
//        }
//        return bgColor;
//    }
//
//    protected Color getTextColor() {
//        Color color = CreatureManipulationButton.TEXT_DEFAULT_COLOR;
//        if (this.hb.hovered && InputHelper.isMouseDown) {
//            color = CreatureManipulationButton.TEXT_FOCUSED_COLOR;
//        }
//        else if (this.hb.hovered) {
//            color = CreatureManipulationButton.TEXT_HOVERED_COLOR;
//        }
//        return color;
//    }
//
//    public void move(final float x, final float y) {
//        this.hb.move(x, y);
//    }
//
//}
