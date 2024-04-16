package SuperstitioMod.utils;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import basemod.BaseMod;
import basemod.interfaces.ISubscriber;
import basemod.interfaces.PostRenderSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import java.util.ArrayList;

public class EventHelper implements PostRenderSubscriber, PostUpdateSubscriber {
    public static final ArrayList<AbstractCard> showCards;
    //    public static final ArrayList<EndTurnSubscriber> END_TURN_SUBSCRIBERS;
//    public static final ArrayList<AddToHandSubscriber> ADD_TO_HAND_SUBSCRIBERS;
//    public static final ArrayList<OnCardCreateSubscriber> ON_CARD_CREATE_SUBSCRIBERS;
    public static final ArrayList<OnAttractSubscriber> ON_ATTRACT_SUBSCRIBERS;
    public static EventHelper Inst;
//    public static final ArrayList<OnShuffleSubscriber> ON_SHUFFLE_SUBSCRIBERS;
//    public static final ArrayList<OnOrbChannelSubscriber> ON_ORB_CHANNEL_SUBSCRIBERS;
//    public static final ArrayList<OnOrbEvokeSubscriber> ON_ORB_EVOKE_SUBSCRIBERS;
//    private static final String[] TEXT;

    static {
        EventHelper.Inst = new EventHelper();
        showCards = new ArrayList<>();
        ON_ATTRACT_SUBSCRIBERS = new ArrayList<>();
    }

    public EventHelper() {
        BaseMod.subscribe((ISubscriber) this);
    }

    private static <T> void AddToList(final ArrayList<T> list, final CustomSubscriber sub, final Class<T> clazz) {
        if (clazz.isInstance(sub)) {
            list.add(clazz.cast(sub));
        }
    }

    private static <T> void RemoveFromList(final ArrayList<T> list, final CustomSubscriber sub, final Class<T> clazz) {
        if (clazz.isInstance(sub)) {
            list.remove(clazz.cast(sub));
        }
    }

    public static void Subscribe(final CustomSubscriber sub) {
        AddToList(EventHelper.ON_ATTRACT_SUBSCRIBERS, sub, OnAttractSubscriber.class);
    }

    public static void Unsubscribe(final CustomSubscriber sub) {
        RemoveFromList(EventHelper.ON_ATTRACT_SUBSCRIBERS, sub, OnAttractSubscriber.class);
    }

    public static void receiveOnBattleStart() {
        EventHelper.ON_ATTRACT_SUBSCRIBERS.clear();
        SuperstitioModSetup.logger.info("battle start");
        CardUtility.AllCardInBattle().forEach(card -> {
            if (card instanceof AbstractLupaCard)
                ((AbstractLupaCard) card).receiveBattleStart();
        });
//        AbstractDungeon.player.relics.forEach(r -> {
//            if (r instanceof AbstractGoldenglowRelic) {
//                Subscribe(r);
//            }
//            return;
//        });
//        AbstractLupaCard.PulseAmount = 1;
//        if (!GoldenglowModCore.TutorialClosed) {
//            final AbstractPlayer p = AbstractDungeon.player;
//            final float x = p.hb.cX + p.hb.width + 130.0f * Settings.scale;
//            final float y = p.hb.cY;
//            ReflectionHacks.setPrivate((Object)(AbstractDungeon.ftue = new FtueTip(EventHelper.TEXT[2], EventHelper.TEXT[3], x, y, FtueTip
//            .TipType.CREATURE)), FtueTip.class, "m", p);
//            GoldenglowModCore.TutorialClosed = true;
//            try {
//                final SpireConfig config = new SpireConfig("Goldenglow", "Common");
//                config.setBool("tutorialClosed", true);
//                config.save();
//            }
//            catch (IOException e) {
//                Hpr.error(e.toString());
//            }
//        }
    }

    public void receivePostRender(final SpriteBatch sb) {
        for (final AbstractCard card : EventHelper.showCards) {
            card.render(sb);
        }
    }

    public void receivePostUpdate() {
        for (final AbstractCard card : EventHelper.showCards) {
            card.update();
        }
    }

//    public interface OnOrbEvokeSubscriber extends CustomSubscriber
//    {
//        void onOrbEvoke(final AbstractOrb p0, final AbstractCreature p1);
//    }

    public interface CustomSubscriber {
    }

//    public interface OnOrbChannelSubscriber extends CustomSubscriber
//    {
//        void onOrbChannel(final AbstractOrb p0, final AbstractCreature p1);
//    }
//
//    public interface OnShuffleSubscriber extends CustomSubscriber
//    {
//        void onShuffle();
//    }

    public interface OnAttractSubscriber extends CustomSubscriber {
        void onAttract(final AbstractCard p0);
    }

//    public interface OnCardCreateSubscriber extends CustomSubscriber
//    {
//        void onCardCreate(final AbstractCard p0);
//    }
//
//    public interface AddToHandSubscriber extends CustomSubscriber
//    {
//        void addToHand(final AbstractCard p0);
//    }
//
//    public interface EndTurnSubscriber extends CustomSubscriber
//    {
//        void endTurn();
//    }
}
