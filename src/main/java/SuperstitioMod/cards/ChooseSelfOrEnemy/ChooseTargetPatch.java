package SuperstitioMod.cards.ChooseSelfOrEnemy;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.utils.CardUtility;
import SuperstitioMod.utils.EventHelper;
import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.Arrays;

public class ChooseTargetPatch {
    public static Target Chosen;
    public static boolean isChoosing;
    public static boolean isShowingCards;

    static {
        ChooseTargetPatch.Chosen = new Target();
        ChooseTargetPatch.isChoosing = false;
        ChooseTargetPatch.isShowingCards = false;
    }

    public static void checkCard(final AbstractCard card) {
        if (!(card instanceof AbstractLupaCard))
            return;
        final AbstractLupaCard c = (AbstractLupaCard) card;
        if (!c.checkIsTargetSelfOrEnemy() || ChooseTargetPatch.isChoosing)
            return;
        final AbstractPlayer p = AbstractDungeon.player;
        if (!AbstractDungeon.getMonsters().monsters.contains(ChooseTargetPatch.Chosen)) {
            AbstractDungeon.getMonsters().monsters.add(ChooseTargetPatch.Chosen);
        }
        ChooseTargetPatch.Chosen.hb.width = p.hb.width;
        ChooseTargetPatch.Chosen.hb.height = p.hb.height;
        ChooseTargetPatch.Chosen.hb.move(p.hb.cX + 100.0f, p.hb.cY + 100.0f);
        ChooseTargetPatch.Chosen.drawX = p.drawX;
        ChooseTargetPatch.Chosen.drawY = p.drawY;
        ChooseTargetPatch.isChoosing = true;
        SuperstitioModSetup.logger.info("have added monster");
    }

    public static void showAttractNum(final AbstractCard card) {
        if (!(card instanceof AbstractLupaCard))
            return;
        final AbstractLupaCard c = (AbstractLupaCard) card;
        SuperstitioModSetup.logger.info(String.format("%s attract number is %d", c, c.getShowAttractNum()));
        if (c.getShowAttractNum() <= 0 || ChooseTargetPatch.isShowingCards)
            return;
        for (int i = 0; i < c.getShowAttractNum(); ++i) {
            final int index = AbstractDungeon.player.discardPile.size() - i - 1;
            if (index < 0 || i > 5) {
                break;
            }
            if (i + AbstractDungeon.player.hand.size() - 1 >= BaseMod.MAX_HAND_SIZE) {
                break;
            }
            final AbstractCard target = CardUtility.makeStatEquivalentCopy(AbstractDungeon.player.discardPile.group.get(index));
            target.drawScale = 0.6f;
            target.targetDrawScale = 0.6f;
            target.current_x = Settings.WIDTH + AbstractCard.IMG_WIDTH;
            target.target_x = Settings.WIDTH - 200.0f * Settings.scale;
            target.current_y = (150.0f + 30.0f * i) * Settings.scale;
            target.target_y = (150.0f + 30.0f * i) * Settings.scale;
            EventHelper.showCards.add(0, target);
        }
        ChooseTargetPatch.isShowingCards = true;
        SuperstitioModSetup.logger.info("have added cards");
    }

    public static void releaseCard() {
        if (ChooseTargetPatch.isChoosing) {
            AbstractDungeon.getMonsters().monsters.remove(ChooseTargetPatch.Chosen);
            ChooseTargetPatch.isChoosing = false;
            SuperstitioModSetup.logger.info("have removed monster");
        }
        if (ChooseTargetPatch.isShowingCards) {
            EventHelper.showCards.clear();
            ChooseTargetPatch.isShowingCards = false;
            SuperstitioModSetup.logger.info("have cleared cards");
        }
    }

    public static class Target extends AbstractMonster {
        public Target() {
            super("", "Apology Slime", 1, 0.0f, 0.0f, 200.0f, 200.0f, (String) null);
            this.setMove((byte) 1, AbstractMonster.Intent.DEBUG, 0);
        }

        protected void getMove(final int arg0) {
        }

        public void takeTurn() {
        }

        public void damage(final DamageInfo info) {
        }

        public void render(final SpriteBatch sb) {
            this.hb.render(sb);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class SimTargetPatch1 {
        @SpireInsertPatch(rloc = 123)
        public static SpireReturn<Boolean> Insert(final AbstractPlayer _inst) {
            ChooseTargetPatch.checkCard(_inst.hoveredCard);
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "clickAndDragCards")
    public static class SimTargetPatch2 {
        @SpireInsertPatch(rloc = 146)
        public static SpireReturn<Boolean> Insert(final AbstractPlayer _inst) {
            ChooseTargetPatch.checkCard(_inst.hoveredCard);
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "updateInput")
    public static class ShowAttractNumPatch1 {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(final AbstractPlayer _inst) {
            ChooseTargetPatch.showAttractNum(_inst.hoveredCard);
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
                final Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher((Class) AbstractCard.class, "flash");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher) matcher);
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "manuallySelectCard")
    public static class ShowAttractNumPatch2 {
        public static void Postfix(final AbstractPlayer _inst, final AbstractCard card) {
            ChooseTargetPatch.showAttractNum(card);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "playCard")
    public static class RemoveTargetPatch1 {
        public static void Postfix(final AbstractPlayer _inst) {
            ChooseTargetPatch.releaseCard();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "releaseCard")
    public static class RemoveTargetPatch2 {
        public static void Postfix(final AbstractPlayer _inst) {
            ChooseTargetPatch.releaseCard();
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "renderTargetingUi")
    public static class ArrowColorPatch {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert(final AbstractPlayer _inst, final SpriteBatch sb, final AbstractMonster ___hoveredMonster) {
            if (___hoveredMonster instanceof Target) {
                sb.setColor(Color.CYAN);
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
                final Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher((Class) AbstractPlayer.class, "drawCurvedLine");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher) matcher);
            }
        }
    }

    @SpirePatch(clz = DamageAllEnemiesAction.class, method = "update")
    public static class DamageAllEnemiesActionPatch {
        @SpireInsertPatch(locator = Locator.class, localvars = {"i"})
        public static void Insert(final DamageAllEnemiesAction _inst, final int i) {
            if (i >= _inst.damage.length) {
                final int[] copy = Arrays.copyOf(_inst.damage, _inst.damage.length + 1);
                copy[copy.length - 1] = 0;
                _inst.damage = copy;
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
                final Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher((Class) AbstractMonster.class, "damage");
                return LineFinder.findInOrder(ctMethodToPatch, (Matcher) matcher);
            }
        }
    }
}
