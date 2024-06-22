package superstitioapi.cardModifier;

import basemod.ClickableUIElement;
import basemod.ReflectionHacks;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockInstance;
import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.evacipated.cardcrawl.mod.stslib.patches.RenderStackedBlockInstances;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static superstitioapi.cardModifier.RenderStackedBlockInstancesPatch.BlockStackForceShowPatch.GetBlockStack;

public class RenderStackedBlockInstancesPatch {
    protected static final float BLOCK_ICON_X = ReflectionHacks.getPrivateStatic(AbstractCreature.class, "BLOCK_ICON_X");
    protected static final float BLOCK_ICON_Y = ReflectionHacks.getPrivateStatic(AbstractCreature.class, "BLOCK_ICON_Y");
    private static final Texture blankTex = new Texture("images/blank.png");
    private static final float dx = 50.0F * Settings.scale;
    private static final float dy = 54.0F * Settings.scale;

    private static List<BlockStackDividedElement> makeAllBlockModifierIntoDrawElement(AbstractCreature creature) {
        List<BlockInstance> instances = getAllBlockInstances(creature);
        List<BlockStackDividedElement> list = new ArrayList<>();
        for (BlockInstance blockInstance : instances) {
            BlockStackDividedElement blockElement = new BlockStackDividedElement(creature, blockInstance);
            list.add(blockElement);
        }
        Collections.reverse(list);
        return list;
    }

    private static ArrayList<BlockInstance> getAllBlockInstances(AbstractCreature creature) {
        ArrayList<BlockInstance> blockInstances = BlockModifierManager.blockInstances(creature);
        ArrayList<BlockInstance> newBlockInstances = new ArrayList<>();
        newBlockInstances.addAll(blockInstances);
        newBlockInstances.addAll(creature.powers.stream()
                .filter(power -> power instanceof RenderAsBlockPower)
                .map(power -> ((RenderAsBlockPower) power).getBlockInstance()).collect(Collectors.toList()));
        return newBlockInstances;
    }

    private static void DrawBlocks(AbstractCreature creature, SpriteBatch sb, float x, float y) {
        float blockOffset = ReflectionHacks.getPrivate(creature, AbstractCreature.class, "blockOffset");
        float blockScale = ReflectionHacks.getPrivate(creature, AbstractCreature.class, "blockScale");
        Color blockOutlineColor = ReflectionHacks.getPrivate(creature, AbstractCreature.class, "blockOutlineColor");
        Color blockTextColor = ReflectionHacks.getPrivate(creature, AbstractCreature.class, "blockTextColor");

//        List<BlockInstance> allBlockLastTime = BlockStackElementField.blockLastTime.get(creature);
        List<BlockInstance> allBlocks = getAllBlockInstances(creature);
        List<BlockStackDividedElement> elements = BlockStackElementField.element.get(creature);
        if (allBlocks.stream().allMatch(BlockInstance::defaultBlock))
            return;
        if (elements == null || elements.isEmpty()
                || allBlocks.size() != elements.size()
                || !elements.stream().map(element -> element.blockInstance).allMatch(allBlocks::contains)) {
            elements = makeAllBlockModifierIntoDrawElement(creature);
        }

        if (elements.isEmpty())
            return;

//        Collections.reverse(elements);
        int offsetY = 0;
        for (BlockStackDividedElement element : elements) {
            element.move(x + BLOCK_ICON_X - 32.0F, y + BLOCK_ICON_Y - 32.0F + blockOffset + offsetY);
            element.update();
            element.renderBlockIcon(sb, blockScale, blockOutlineColor, blockTextColor);
            offsetY = (int) ((float) offsetY + dy);
        }

        BlockStackElementField.element.set(creature, elements);
    }

    public static class BlockStackDividedElement extends ClickableUIElement {
        public static final float HOVERED_TIMER_INIT = 1.0f;
        public static final float HOVERED_TIMER_MIN = 0.2f;
        private static final float baseHeight = 64.0F * Settings.scale;
        private static final float baseWidth = 64.0F * Settings.scale;
        private final AbstractCreature owner;
        private final BlockInstance blockInstance;
        private PowerTip tip;
        private float HoveredTimer = HOVERED_TIMER_INIT;

        public BlockStackDividedElement(AbstractCreature owner, BlockInstance blockInstance) {
            super(blankTex, 0.0F, 0.0F, baseWidth, baseHeight);
            this.owner = owner;
            this.blockInstance = blockInstance;
//            this.tips.addAll(Arrays.asList(tips));
        }

        public void move(float x, float y) {
            this.move(x, y, 0.0F);
        }

        private void move(float x, float y, float d) {
            this.setX(x - d * Settings.scale);
            this.setY(y - d * Settings.scale);
        }

        public void setHitboxHeight(float height) {
            this.hitbox.resize(baseWidth, baseHeight + height);
        }

        public void renderBlockIcon(SpriteBatch sb, float blockScale, Color blockOutlineColor, Color blockTextColor) {
            this.render(sb);

            Color IconColor = blockInstance.getBlockColor() != null ? blockInstance.getBlockColor() : blockOutlineColor;
            Color TextColor = blockInstance.getBlockColor() != null ? blockInstance.getBlockColor() : blockTextColor;

            float tmpIcon = IconColor.a;
            float tmpText = TextColor.a;
            if (hitbox.hovered) {
                IconColor.a *= HoveredTimer;
                TextColor.a *= HoveredTimer;
            }

            sb.setColor(IconColor);


            sb.draw(blockInstance.getBlockImage(), x, y,
                    32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

            FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, Integer.toString(getBlockAmount()),
                    x - (-32.0F), y - (BLOCK_ICON_Y - 32.0F) - 16.0F * Settings.scale, TextColor, blockScale);

            IconColor.a = tmpIcon;
            TextColor.a = tmpText;

            if (hitbox.hovered)
                FontHelper.renderFontCentered(sb, FontHelper.blockInfoFont, Integer.toString(GetBlockStack(owner)),
                        x - (-32.0F), y - (BLOCK_ICON_Y - 32.0F) - 16.0F * Settings.scale, TextColor, blockScale);

        }

        private int getBlockAmount() {
            return blockInstance.getBlockAmount();
        }

        @Override
        public void update() {
            super.update();
        }

        @Override
        protected void onHover() {
            if (HoveredTimer >= HOVERED_TIMER_MIN) {
                HoveredTimer -= Gdx.graphics.getDeltaTime();
            }
            ArrayList<PowerTip> tips = new ArrayList<>();
            tips.add(new TooltipInfo(blockInstance.makeName(), blockInstance.makeDescription()).toPowerTip());
            boolean isInScreen = this.hitbox.width / 2.0F < 1544.0F * Settings.scale;
            TipHelper.queuePowerTips(
                    isInScreen ? this.owner.hb.cX + this.owner.hb.width / 2.0F + 20.0F * Settings.scale
                            : this.owner.hb.cX - this.owner.hb.width / 2.0F + -380.0F * Settings.scale,
                    this.y + this.owner.hb.height + TipHelper.calculateAdditionalOffset(tips, this.y + this.owner.hb.height), tips);
        }

        @Override
        protected void onUnhover() {
            if (HoveredTimer < HOVERED_TIMER_INIT)
                HoveredTimer = Math.min(HoveredTimer + Gdx.graphics.getDeltaTime(), HOVERED_TIMER_INIT);
        }

        @Override
        protected void onClick() {
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "<class>"
    )
    public static class BlockStackElementField {
        public static SpireField<List<BlockStackDividedElement>> element = new SpireField<>(() -> null);
        public static SpireField<Boolean> forceDrawBlock = new SpireField<>(() -> false);
    }

    @SpirePatch(clz = RenderStackedBlockInstances.RenderStackedIcons.class, method = "pls")
    public static class BlockStackPatchStslib {
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y, float ___BLOCK_ICON_X,
                                               float ___BLOCK_ICON_Y, float ___blockOffset, Color ___blockTextColor, Color ___blockOutlineColor,
                                               float ___blockScale) {
            DrawBlocks(__instance, sb, x, y);
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderBlockIconAndValue"
    )
    public static class BlockStackPatchVanilla {
        @SpirePrefixPatch
        public static SpireReturn<Void> Prefix(AbstractCreature __instance, SpriteBatch sb, float x, float y) {
            if (getAllBlockInstances(__instance).stream().allMatch(BlockInstance::defaultBlock))
                return SpireReturn.Continue();
            return SpireReturn.Return();
        }
    }

    @SpirePatch(
            clz = AbstractCreature.class,
            method = "renderHealth"
    )
    public static class BlockStackForceShowPatch {
        public static ExprEditor Instrument() {
            return new ExprEditor() {
                public void edit(final FieldAccess m) throws CannotCompileException {
                    if (!m.getFieldName().equals("currentBlock") || !m.isReader()) return;
                    m.replace("{$_ = " + BlockStackForceShowPatch.class.getName() + ".GetBlockStack( $0 ) ;}");


                }
            };
        }

        public static int GetBlockStack(AbstractCreature creature) {
            if (!BlockStackElementField.forceDrawBlock.get(creature)) return creature.currentBlock;
            int totalBlock = RenderStackedBlockInstancesPatch.getAllBlockInstances(creature).stream()
                    .mapToInt(BlockInstance::getBlockAmount).sum();
            if (totalBlock <= 0) {
                BlockStackElementField.forceDrawBlock.set(creature, false);
                return creature.currentBlock;
            }
            return totalBlock;
        }
    }

}
