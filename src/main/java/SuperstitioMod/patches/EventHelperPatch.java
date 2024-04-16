package SuperstitioMod.patches;

//import Goldenglow.modcore.*;
//import SuperstitioMod.cards.Lupa.AbstractLupaCard;
//import com.megacrit.cardcrawl.rooms.*;
//import com.megacrit.cardcrawl.actions.defect.*;
//import com.megacrit.cardcrawl.actions.common.*;
//import com.megacrit.cardcrawl.cards.*;
//import Goldenglow.helper.*;
//import com.megacrit.cardcrawl.characters.*;
//import com.megacrit.cardcrawl.orbs.*;
//import com.megacrit.cardcrawl.core.*;
//import javassist.*;
//import com.megacrit.cardcrawl.screens.*;
//import com.badlogic.gdx.graphics.g2d.*;
//import java.util.*;
//import com.megacrit.cardcrawl.helpers.*;
//import com.evacipated.cardcrawl.modthespire.lib.*;
//import Goldenglow.cards.*;

public class EventHelperPatch
{
//    private static final String[] TEXT;

//    public static void OnShuffle() {
//        EventHelper.ON_SHUFFLE_SUBSCRIBERS.forEach(EventHelper.OnShuffleSubscriber::onShuffle);
//    }
//
//    static {
//        TEXT = CardCrawlGame.languagePack.getUIString(GoldenglowModCore.MakePath("UI")).TEXT;
//    }
//
//    @SpirePatch(clz = AbstractRoom.class, method = "endTurn")
//    public static class EndTurnPatch
//    {
//        public static void Prefix(final AbstractRoom _inst) {
//            for (final EventHelper.EndTurnSubscriber sub : EventHelper.END_TURN_SUBSCRIBERS) {
//                sub.endTurn();
//            }
//        }
//    }
//
//    @SpirePatches({ @SpirePatch(clz = CardGroup.class, method = "addToTop"), @SpirePatch(clz = CardGroup.class, method = "addToBottom"), @SpirePatch(clz = CardGroup.class, method = "addToHand"), @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot") })
//    public static class AddToHandPatch
//    {
//        public static void Postfix(final CardGroup _inst, final AbstractCard c) {
//            if (_inst.type == CardGroup.CardGroupType.HAND) {
//                EventHelper.ADD_TO_HAND_SUBSCRIBERS.forEach(sub -> sub.addToHand(c));
//            }
//        }
//    }

//    @SpirePatch(clz = ShuffleAllAction.class, method = "<ctor>")
//    public static class OnShufflePatch1
//    {
//        public static void Postfix(final ShuffleAllAction _inst) {
//            EventHelperPatch.OnShuffle();
//        }
//    }
//
//    @SpirePatch(clz = EmptyDeckShuffleAction.class, method = "<ctor>")
//    public static class OnShufflePatch2
//    {
//        public static void Postfix(final EmptyDeckShuffleAction _inst) {
//            EventHelperPatch.OnShuffle();
//        }
//    }
//
//    @SpirePatch(clz = ShuffleAction.class, method = "<ctor>", paramtypez = { CardGroup.class, boolean.class })
//    public static class OnShufflePatch3
//    {
//        public static void Postfix(final ShuffleAction _inst, final CardGroup group, final boolean trigger) {
//            if (trigger) {
//                EventHelperPatch.OnShuffle();
//            }
//        }
//    }

//    @SpirePatch(clz = AbstractCard.class, method = "<ctor>", paramtypez = { String.class, String.class, String.class, int.class, String.class, AbstractCard.CardType.class, AbstractCard.CardColor.class, AbstractCard.CardRarity.class, AbstractCard.CardTarget.class, DamageInfo.DamageType.class })
//    public static class onCardCreatePatch
//    {
//        public static void Postfix(final AbstractCard _inst, final String id, final String name, final String imgUrl, final int cost, final String rawDescription, final AbstractCard.CardType type, final AbstractCard.CardColor color, final AbstractCard.CardRarity rarity, final AbstractCard.CardTarget target, final DamageInfo.DamageType dType) {
//            if (Hpr.isInBattle()) {
//                EventHelper.ON_CARD_CREATE_SUBSCRIBERS.forEach(sub -> sub.onCardCreate(_inst));
//            }
//        }
//    }
//
//    @SpirePatch(clz = AbstractPlayer.class, method = "channelOrb")
//    public static class onOrbChannelPatch
//    {
//        @SpireInsertPatch(locator = Locator.class)
//        public static void Insert(final AbstractPlayer _inst, final AbstractOrb orbToSet) {
//            EventHelper.ON_ORB_CHANNEL_SUBSCRIBERS.forEach(sub -> sub.onOrbChannel(orbToSet, (AbstractCreature)_inst));
//        }
//
//        public static class Locator extends SpireInsertLocator
//        {
//            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
//                final Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractOrb.class, "applyFocus");
//                return LineFinder.findInOrder(ctMethodToPatch, matcher);
//            }
//        }
//    }

//    @SpirePatch(clz = AbstractPlayer.class, method = "evokeOrb")
//    public static class onOrbEvokePatch
//    {
//        @SpireInsertPatch(locator = Locator.class)
//        public static void Insert(final AbstractPlayer _inst) {
//            EventHelper.ON_ORB_EVOKE_SUBSCRIBERS.forEach(sub -> sub.onOrbEvoke(_inst.orbs.get(0), (AbstractCreature)_inst));
//        }
//
//        public static class Locator extends SpireInsertLocator
//        {
//            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
//                final Matcher.MethodCallMatcher matcher = new Matcher.MethodCallMatcher(AbstractOrb.class, "onEvoke");
//                return LineFinder.findInOrder(ctMethodToPatch, matcher);
//            }
//        }
//    }

//    @SpirePatch(clz = SingleCardViewPopup.class, method = "renderTips")
//    public static class ShowCreditsPatch
//    {
//        @SpireInsertPatch(locator = Locator.class, localvars = { "card", "t" })
//        public static void Insert(final SingleCardViewPopup _inst, final SpriteBatch sb, final AbstractCard acard, @ByRef final ArrayList<PowerTip>[] t) {
//            if (acard instanceof AbstractLupaCard && ((AbstractLupaCard)acard).showCredits) {
//                t[0].add(new PowerTip(EventHelperPatch.TEXT[8], ((AbstractLupaCard)acard).creditDes));
//            }
//        }
//
//        public static class Locator extends SpireInsertLocator
//        {
//            public int[] Locate(final CtBehavior ctMethodToPatch) throws Exception {
//                final Matcher.MethodCallMatcher methodCallMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
//                return LineFinder.findInOrder(ctMethodToPatch, methodCallMatcher);
//            }
//        }
//    }
}
