package superstitio.orbs.orbgroup;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.orbs.AbstractOrb;

import java.util.ArrayList;

public class OrbEventSubscriber {
    public static final ArrayList<OnOrbChannelSubscriber> ON_ORB_CHANNEL_SUBSCRIBERS = new ArrayList<>();
    public static final ArrayList<OnOrbEvokeSubscriber> ON_ORB_EVOKE_SUBSCRIBERS = new ArrayList<>();

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

    public static void subscribe(final CustomSubscriber sub) {
        AddToList(OrbEventSubscriber.ON_ORB_CHANNEL_SUBSCRIBERS, sub, OnOrbChannelSubscriber.class);
        AddToList(OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS, sub, OnOrbEvokeSubscriber.class);
    }

    public static void unSubscribe(final CustomSubscriber sub) {
        RemoveFromList(OrbEventSubscriber.ON_ORB_CHANNEL_SUBSCRIBERS, sub, OnOrbChannelSubscriber.class);
        RemoveFromList(OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS, sub, OnOrbEvokeSubscriber.class);
    }

    public interface OnOrbEvokeSubscriber extends CustomSubscriber {
        void onOrbEvoke(final AbstractOrb p0, final AbstractCreature p1);
    }

    public interface CustomSubscriber {
    }

    public interface OnOrbChannelSubscriber extends CustomSubscriber {
        void onOrbChannel(final AbstractOrb p0, final AbstractCreature p1);
    }
}
