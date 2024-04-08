package SuperstitioMod.utils;

import SuperstitioMod.SuperstitioModSetup;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;
import java.util.stream.Collectors;

public class CardUtility {


    /**
     * @param ThisMod 当为true时， IsCardColorVanilla()被短路
     * @param Vanilla 当为true时， IsCardColorThisMod()被短路
     * @return 输入为true ture时，输出为满足两个条件之一的结果。输入为1true1false时，输出由true项决定。输入为false false时，输出两者都不满足的结果。
     */
    public static AbstractCard getRandomCurseCard(boolean ThisMod, boolean Vanilla) {
        List<AbstractCard> list = getCardsListForMod(ThisMod, Vanilla).stream()
                .filter(card -> card.type == AbstractCard.CardType.STATUS)
                .collect(Collectors.toList());
        return list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
    }

    public static List<AbstractCard> getCardsListForMod(boolean ThisMod, boolean Vanilla) {
        List<AbstractCard> list;
        list = CardLibrary.cards.values().stream()
                .filter(card -> {
                    if (ThisMod && Vanilla)
                        return !(!IsCardColorVanilla(card) && !IsCardColorThisMod(card));
                    else if (ThisMod)
                        return !IsCardColorVanilla(card) && IsCardColorThisMod(card);
                    else if (Vanilla)
                        return IsCardColorVanilla(card) && !IsCardColorThisMod(card);
                    else
                        return !IsCardColorVanilla(card) && !IsCardColorThisMod(card);
                })
                .collect(Collectors.toList());
        return list;
    }

    public static boolean IsCardColorVanilla(AbstractCard card) {
        return !card.cardID.contains(":");
    }

    public static boolean IsCardColorThisMod(AbstractCard card) {
        return !card.cardID.contains(SuperstitioModSetup.MakeTextID(""));
    }

//    public static void addToSequence(final AbstractCard c) {
//        if (!FunctionHelper.doStuff) {
//            FunctionHelper.doStuff = true;
//        }
//        if (c instanceof FullRelease && FunctionHelper.held.group.stream().anyMatch(q -> q.cardID.equals(FullRelease.ID))) {
//            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new MakeTempCardInDiscardAction(c.makeSameInstanceOf(), 1));
//            AbstractDungeon.actionManager.addToTop((AbstractGameAction)new TalkAction(true, AutomatonTextHelper.uiStrings.TEXT[6], 2.0f, 1.5f));
//        }
////        for (final AbstractPower p : AbstractDungeon.player.powers) {
////            if (p instanceof OnAddToFuncPower) {
////                ((OnAddToFuncPower)p).receiveAddToFunc(c);
////            }
////        }
//        c.stopGlowing();
//        c.resetAttributes();
//        c.targetDrawScale = 0.225f;
//        c.target_x = FunctionHelper.cardPositions[FunctionHelper.held.size()].x;
//        c.target_y = FunctionHelper.cardPositions[FunctionHelper.held.size()].y;
//        final int r = FunctionHelper.held.size();
//        FunctionHelper.held.addToTop(c);
//        if (c instanceof AbstractBronzeCard) {
//            ((AbstractBronzeCard)c).position = r;
//        }
//        if (c instanceof AbstractBronzeCard) {
//            ((AbstractBronzeCard)c).onInput();
//        }
//        if (FunctionHelper.held.size() == max()) {
//            output();
//        }
//        FunctionHelper.secretStorage = makeFunction(false);
////        for (final AbstractPower q2 : AbstractDungeon.player.powers) {
////            if (q2 instanceof PostAddToFuncPower) {
////                ((PostAddToFuncPower)q2).receivePostAddToFunc(c);
////            }
////        }
//    }
//    public static void output() {
//        ForceShield.decrementShields();
//        boolean regularOutput = true;
//        for (final AbstractPower p : AbstractDungeon.player.powers) {
//            if (p instanceof OnOutputFunctionPower) {
//                regularOutput = ((OnOutputFunctionPower)p).receiveOutputFunction();
//            }
//        }
//        if (FunctionHelper.doExtraNonSpecificCopy > 0) {
//            for (int i = 0; i < FunctionHelper.doExtraNonSpecificCopy; ++i) {
//                AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new MakeTempCardInHandAction(makeFunction(true)));
//            }
//            FunctionHelper.doExtraNonSpecificCopy = 0;
//        }
//        if (regularOutput) {
//            AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new MakeTempCardInHandAction(makeFunction(true)));
//        }
//        AbstractDungeon.actionManager.addToBottom((AbstractGameAction)new AbstractGameAction() {
//            public void update() {
//                FunctionHelper.held.clear();
//                FunctionHelper.secretStorage = FunctionHelper.makeFunction(false);
//                this.isDone = true;
//            }
//        });
//        for (final AbstractPower p : AbstractDungeon.player.powers) {
//            if (p instanceof AfterOutputFunctionPower) {
//                ((AfterOutputFunctionPower)p).receiveAfterOutputFunction();
//            }
//        }
//        ++FunctionHelper.functionsCompiledThisCombat;
//    }

}
