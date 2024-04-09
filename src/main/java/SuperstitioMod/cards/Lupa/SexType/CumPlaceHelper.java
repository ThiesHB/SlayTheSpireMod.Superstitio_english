package SuperstitioMod.cards.Lupa.SexType;


import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.characters.Lupa;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.BobEffect;

import java.util.HashMap;

public class CumPlaceHelper {
    public static final float SEQUENCED_CARD_SIZE = 0.225f;
    public static final float FUNC_CARD_SIZE = 0.45f;
    public static final float BG_X;
    public static final float BG_Y;
    public static final float HEIGHT_SEQUENCE;
    public static final float HEIGHT_SPOT;
    public static final float HEIGHT_FUNCTION;
    public static final Vector2[] cardPositions = new Vector2[]{new Vector2(218.0f * Settings.xScale, CumPlaceHelper.HEIGHT_SEQUENCE),
            new Vector2(293.0f * Settings.xScale,
            CumPlaceHelper.HEIGHT_SEQUENCE), new Vector2(368.0f * Settings.xScale, CumPlaceHelper.HEIGHT_SEQUENCE),
            new Vector2(443.0f * Settings.xScale, CumPlaceHelper.HEIGHT_SEQUENCE)};
    public static final Vector2[] floaterStartPositions = new Vector2[]{new Vector2(177.0f * Settings.xScale, CumPlaceHelper.HEIGHT_SPOT),
            new Vector2(252.0f * Settings.xScale, CumPlaceHelper.HEIGHT_SPOT), new Vector2(327.0f * Settings.xScale,
            CumPlaceHelper.HEIGHT_SPOT), new Vector2(402.0f * Settings.xScale, CumPlaceHelper.HEIGHT_SPOT)};
    public static final Vector2[] funcPositions = new Vector2[]{new Vector2(480.0f * Settings.xScale, CumPlaceHelper.HEIGHT_FUNCTION),
            new Vector2(560.0f * Settings.xScale,
            CumPlaceHelper.HEIGHT_FUNCTION)};
    public static final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";
    private static final Texture bg = ImageMaster.loadImage(SuperstitioModSetup.makeImgFilesPath_UI("sequenceframe"));
    private static final Texture bg_4card = ImageMaster.loadImage(SuperstitioModSetup.makeImgFilesPath_UI("sequenceframe4cards"));
    private static final Texture sequenceSlot = ImageMaster.loadImage(SuperstitioModSetup.makeImgFilesPath_UI("sequenceSlot"));
    public static CardGroup held;
    public static int functionsCompiledThisCombat;
    public static int doExtraNonSpecificCopy;
    public static HashMap<AbstractGameAction, AbstractCard> cardModsInfo;
    public static BobEffect[] bobEffects;
    public static boolean doStuff;
    public static AbstractCard secretStorage;
    @SpireEnum
    public static AbstractCard.CardTags NO_TEXT;

    static {
        CumPlaceHelper.functionsCompiledThisCombat = 0;
        CumPlaceHelper.doExtraNonSpecificCopy = 0;
        CumPlaceHelper.cardModsInfo = new HashMap<>();
        BG_X = 150.0f * Settings.scale;
        BG_Y = 700.0f * Settings.scale;
        HEIGHT_SEQUENCE = 800.0f * Settings.yScale;
        HEIGHT_SPOT = 700.0f * Settings.yScale;
        HEIGHT_FUNCTION = 820.0f * Settings.yScale;
        CumPlaceHelper.bobEffects = new BobEffect[]{new BobEffect(), new BobEffect(), new BobEffect(), new BobEffect()};
        CumPlaceHelper.doStuff = false;
        CumPlaceHelper.secretStorage = null;
    }

    public static int max() {
        int max = 3;
//        if (AbstractDungeon.player.hasRelic(ElectromagneticCoil.ID)) {
//            max = 4;
//        }
        return max;
    }

    public static void init() {
        CumPlaceHelper.held = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        CumPlaceHelper.secretStorage = makeFunction(false);
        CumPlaceHelper.functionsCompiledThisCombat = 0;
        if (AbstractDungeon.player instanceof Lupa) {
            CumPlaceHelper.doStuff = true;
        }
    }

    public static boolean isSequenceEmpty() {
        return CumPlaceHelper.held != null && CumPlaceHelper.held.isEmpty();
    }

    public static void genPreview() {
        CumPlaceHelper.secretStorage = makeFunction(false);
    }

    public static void addToSequence(final AbstractCard c) {
        if (!CumPlaceHelper.doStuff) {
            CumPlaceHelper.doStuff = true;
        }
//        if (c instanceof FullRelease && CumPlaceHelper.held.group.stream().anyMatch(q -> q.cardID.equals(FullRelease.ID))) {
//            AbstractDungeon.actionManager.addToTop(new MakeTempCardInDiscardAction(c.makeSameInstanceOf(), 1));
//            AbstractDungeon.actionManager.addToTop(new TalkAction(true, AutomatonTextHelper.uiStrings.TEXT[6], 2.0f, 1.5f));
//        }
//        for (final AbstractPower p : AbstractDungeon.player.powers) {
//            if (p instanceof OnAddToFuncPower) {
//                ((OnAddToFuncPower) p).receiveAddToFunc(c);
//            }
//        }
        c.stopGlowing();
        c.resetAttributes();
        c.targetDrawScale = 0.225f;
        c.target_x = CumPlaceHelper.cardPositions[CumPlaceHelper.held.size()].x;
        c.target_y = CumPlaceHelper.cardPositions[CumPlaceHelper.held.size()].y;
        final int r = CumPlaceHelper.held.size();
        CumPlaceHelper.held.addToTop(c);
        if (c instanceof AbstractSexTypeCard) {
            ((AbstractSexTypeCard) c).position = r;
        }
        if (c instanceof AbstractSexTypeCard) {
            ((AbstractSexTypeCard) c).onInput();
        }
        if (CumPlaceHelper.held.size() == max()) {
            output();
        }
        CumPlaceHelper.secretStorage = makeFunction(false);
//        for (final AbstractPower q2 : AbstractDungeon.player.powers) {
//            if (q2 instanceof PostAddToFuncPower) {
//                ((PostAddToFuncPower) q2).receivePostAddToFunc(c);
//            }
//        }
    }

    public static void output() {
        cardModsInfo.keySet().forEach(abstractGameAction -> AbstractDungeon.actionManager.addToBottom(abstractGameAction));
    }

    public static AbstractCard makeFunction(final boolean forGameplay) {
        final AbstractSexTypeCard function = new SexTypeCard();
//        for (final AbstractPower p : AbstractDungeon.player.powers) {
//            if (p instanceof PreCardCompileEffectsPower) {
//                ((PreCardCompileEffectsPower) p).receivePreCardCompileEffects(forGameplay);
//            }
//        }
        for (final AbstractCard c : CumPlaceHelper.held.group) {
            if (c instanceof AbstractSexTypeCard && ((AbstractSexTypeCard) c).doSpecialCompileStuff) {
                ((AbstractSexTypeCard) c).onCompilePreCardEffectEmbed(forGameplay);
            }
        }
        int counter = 0;
        boolean justDoNoun = false;
        boolean foundExactlyOne = false;
        for (final AbstractCard u : CumPlaceHelper.held.group) {
            if (!u.hasTag(NO_TEXT)) {
                justDoNoun = !foundExactlyOne;
                foundExactlyOne = true;
            }
        }
//        for (final AbstractCard c2 : CumPlaceHelper.held.group) {
//            if (c2.hasTag(NO_TEXT)) {
//                CardModifierManager.addModifier(function, (AbstractCardModifier) new CardEffectsCardMod(c2, -99));
//            } else if (justDoNoun) {
//                CardModifierManager.addModifier(function, (AbstractCardModifier) new CardEffectsCardMod(c2, 1));
//            } else {
//                CardModifierManager.addModifier(function, (AbstractCardModifier) new CardEffectsCardMod(c2, counter));
//                ++counter;
//            }
//        }
        for (final AbstractCard c2 : CumPlaceHelper.held.group) {
            if (c2 instanceof AbstractSexTypeCard && ((AbstractSexTypeCard) c2).doSpecialCompileStuff) {
                ((AbstractSexTypeCard) c2).onCompileFirst(function, forGameplay);
            }
        }
        for (final AbstractCard c2 : CumPlaceHelper.held.group) {
            if (c2 instanceof AbstractSexTypeCard && ((AbstractSexTypeCard) c2).doSpecialCompileStuff) {
                ((AbstractSexTypeCard) c2).onCompile(function, forGameplay);
            }
        }
        for (final AbstractCard c2 : CumPlaceHelper.held.group) {
            if (c2 instanceof AbstractSexTypeCard && ((AbstractSexTypeCard) c2).doSpecialCompileStuff) {
                ((AbstractSexTypeCard) c2).onCompileLast(function, forGameplay);
            }
        }
//        for (final AbstractPower p2 : AbstractDungeon.player.powers) {
//            if (p2 instanceof OnCompilePower) {
//                ((OnCompilePower) p2).receiveCompile((AbstractCard) function, forGameplay);
//            }
//        }
//        for (final AbstractRelic r : AbstractDungeon.player.relics) {
//            if (r instanceof OnCompileRelic) {
//                ((OnCompileRelic) r).receiveCompile((AbstractCard) function, forGameplay);
//            }
//        }
        return function;
    }
//
//    public static void output() {
//        //ForceShield.decrementShields();
//        boolean regularOutput = true;
////        for (final AbstractPower p : AbstractDungeon.player.powers) {
////            if (p instanceof OnOutputFunctionPower) {
////                regularOutput = ((OnOutputFunctionPower) p).receiveOutputFunction();
////            }
////        }
//        if (CumPlaceHelper.doExtraNonSpecificCopy > 0) {
//            for (int i = 0; i < CumPlaceHelper.doExtraNonSpecificCopy; ++i) {
//                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(makeFunction(true)));
//            }
//            CumPlaceHelper.doExtraNonSpecificCopy = 0;
//        }
//        if (regularOutput) {
//            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(makeFunction(true)));
//        }
//        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
//            public void update() {
//                CumPlaceHelper.held.clear();
//                CumPlaceHelper.secretStorage = CumPlaceHelper.makeFunction(false);
//                this.isDone = true;
//            }
//        });
////        for (final AbstractPower p : AbstractDungeon.player.powers) {
////            if (p instanceof AfterOutputFunctionPower) {
////                ((AfterOutputFunctionPower) p).receiveAfterOutputFunction();
////            }
////        }
//        ++CumPlaceHelper.functionsCompiledThisCombat;
//    }

    public static void render(final SpriteBatch sb) {
        if (CumPlaceHelper.doStuff) {
            sb.setColor(Color.WHITE.cpy());
            if (max() == 4) {
                sb.draw(CumPlaceHelper.bg_4card, CumPlaceHelper.BG_X, CumPlaceHelper.BG_Y, 0.0f, 0.0f,
                        CumPlaceHelper.bg_4card.getWidth() * Settings.scale, CumPlaceHelper.bg_4card.getHeight() * Settings.scale, 1.0f, 1.0f, 0.0f
                        , 0, 0, CumPlaceHelper.bg_4card.getWidth(), CumPlaceHelper.bg_4card.getHeight(), false, false);
            } else {
                sb.draw(CumPlaceHelper.bg, CumPlaceHelper.BG_X, CumPlaceHelper.BG_Y, 0.0f, 0.0f, CumPlaceHelper.bg.getWidth() * Settings.scale,
                        CumPlaceHelper.bg.getHeight() * Settings.scale, 1.0f, 1.0f, 0.0f, 0, 0, CumPlaceHelper.bg.getWidth(),
                        CumPlaceHelper.bg.getHeight(), false, false);
            }
            for (int i = 0; i < max(); ++i) {
                sb.draw(CumPlaceHelper.sequenceSlot, CumPlaceHelper.floaterStartPositions[i].x,
                        CumPlaceHelper.floaterStartPositions[i].y + CumPlaceHelper.bobEffects[i].y, 0.0f, 0.0f,
                        CumPlaceHelper.sequenceSlot.getWidth() * Settings.scale, CumPlaceHelper.sequenceSlot.getHeight() * Settings.scale, 1.0f,
                        1.0f, 0.0f, 0, 0, CumPlaceHelper.sequenceSlot.getWidth(), CumPlaceHelper.sequenceSlot.getHeight(), false, false);
                if (CumPlaceHelper.held.size() - 1 >= i) {
                    CumPlaceHelper.held.group.get(i).render(sb);
                }
            }
            if (CumPlaceHelper.secretStorage != null) {
                CumPlaceHelper.secretStorage.render(sb);
            }
        }
    }

    public static void update() {
        if (!CumPlaceHelper.doStuff) {
            if ((AbstractDungeon.player != null || !CumPlaceHelper.held.isEmpty()) && AbstractDungeon.player instanceof Lupa) {
                CumPlaceHelper.doStuff = true;
            }
        } else if (AbstractDungeon.player == null) {
            CumPlaceHelper.doStuff = false;
        } else if (!(AbstractDungeon.player instanceof Lupa) && CumPlaceHelper.held.isEmpty()) {
            CumPlaceHelper.doStuff = false;
        }
        if (CumPlaceHelper.doStuff) {
            for (final BobEffect b : CumPlaceHelper.bobEffects) {
                b.update();
            }
            for (int i = 0; i < CumPlaceHelper.held.size(); ++i) {
                final AbstractCard c = CumPlaceHelper.held.group.get(i);
                c.target_y = CumPlaceHelper.cardPositions[i].y + CumPlaceHelper.bobEffects[i].y;
                c.update();
                c.updateHoverLogic();
            }
            if (CumPlaceHelper.secretStorage != null) {
                CumPlaceHelper.secretStorage.update();
                CumPlaceHelper.secretStorage.updateHoverLogic();
                final float x = (max() == 3) ? CumPlaceHelper.funcPositions[0].x : CumPlaceHelper.funcPositions[1].x;
                final float y = (max() == 3) ? CumPlaceHelper.funcPositions[0].y : CumPlaceHelper.funcPositions[1].y;
                CumPlaceHelper.secretStorage.target_x = x;
                CumPlaceHelper.secretStorage.current_x = x;
                CumPlaceHelper.secretStorage.target_y = y;
                CumPlaceHelper.secretStorage.current_y = y;
                CumPlaceHelper.secretStorage.targetDrawScale = 0.45f;
                CumPlaceHelper.secretStorage.drawScale = 0.45f;
            }
        }
    }
}
