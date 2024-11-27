package superstitio.orbs.orbgroup

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.orbs.AbstractOrb

object OrbEventSubscriber {
    val ON_ORB_CHANNEL_SUBSCRIBERS: MutableList<OnOrbChannelSubscriber> = ArrayList()
    val ON_ORB_EVOKE_SUBSCRIBERS: MutableList<OnOrbEvokeSubscriber> = ArrayList()

    fun subscribe(sub: CustomSubscriber) {
        AddToList(ON_ORB_CHANNEL_SUBSCRIBERS, sub, OnOrbChannelSubscriber::class.java)
        AddToList(ON_ORB_EVOKE_SUBSCRIBERS, sub, OnOrbEvokeSubscriber::class.java)
    }

    fun unSubscribe(sub: CustomSubscriber) {
        RemoveFromList(ON_ORB_CHANNEL_SUBSCRIBERS, sub, OnOrbChannelSubscriber::class.java)
        RemoveFromList(ON_ORB_EVOKE_SUBSCRIBERS, sub, OnOrbEvokeSubscriber::class.java)
    }

    private fun <T> AddToList(list: MutableList<T>, sub: CustomSubscriber, clazz: Class<T>) {
        if (clazz.isInstance(sub)) {
            list.add(clazz.cast(sub))
        }
    }

    private fun <T> RemoveFromList(list: MutableList<T>, sub: CustomSubscriber, clazz: Class<T>) {
        if (clazz.isInstance(sub)) {
            list.remove(clazz.cast(sub))
        }
    }

    interface OnOrbEvokeSubscriber : CustomSubscriber {
        fun onOrbEvoke(p0: AbstractOrb?, p1: AbstractCreature)
    }

    interface CustomSubscriber

    interface OnOrbChannelSubscriber : CustomSubscriber {
        fun onOrbChannel(p0: AbstractOrb?, p1: AbstractCreature)
    }
}
