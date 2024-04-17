package SuperstitioMod.cards.ChooseSelfOrEnemy;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

import java.util.Arrays;

public class ChooseTargetPatch {
    public static Target Chosen;
    public static boolean isChoosing;

    static {
        ChooseTargetPatch.Chosen = new Target();
        ChooseTargetPatch.isChoosing = false;
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

    public static void releaseCard() {
        if (ChooseTargetPatch.isChoosing) {
            AbstractDungeon.getMonsters().monsters.remove(ChooseTargetPatch.Chosen);
            ChooseTargetPatch.isChoosing = false;
            SuperstitioModSetup.logger.info("have removed monster");
        }
    }

    public static class Target extends AbstractMonster {
        public Target() {
            super("", "Apology Slime", 1, 0.0f, 0.0f, 200.0f, 200.0f, null);
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
                final Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "drawCurvedLine");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
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
                final Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractMonster.class, "damage");
                return LineFinder.findInOrder(ctMethodToPatch, matcher);
            }
        }
    }
}
