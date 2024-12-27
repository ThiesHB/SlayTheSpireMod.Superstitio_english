package superstitioapi.renderManager.inBattleManager

import basemod.interfaces.ISubscriber
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.hangUpCard.HangUpCardGroup
import superstitioapi.pet.PetManager
import superstitioapi.powers.barIndepend.BarRenderManager
import java.util.*
import java.util.function.Consumer

object InBattleDataManager
{
    var costMap: HashMap<UUID, Int> = HashMap()
    var subscribeManageGroups: MutableList<ISubscriber> = ArrayList()

    @JvmStatic
    fun InitializeAtStartOfBattle()
    {
        ResetAll()
        AutoDoneInstantAction.addToTopAbstract {
            Subscribe(HangUpCardGroup(AbstractDungeon.player.hb))
            Subscribe(BarRenderManager())
            Subscribe(PetManager())
        }
    }

    @JvmStatic
    fun ClearOnEndOfBattle()
    {
        ResetAll()
    }

    @JvmStatic
    fun <T : ISubscriber> ApplyAll(subscriberConsumer: Consumer<T>?, tClass: Class<T>)
    {
        subscribeManageGroups.filterIsInstance(tClass).forEach(subscriberConsumer)
    }

    @JvmStatic
    fun InitializeAtStartOfTurn()
    {
    }

    //    public static <T extends ISubscriber> void ApplyAllForPower(Consumer<T> subscriberConsumer, Class<T> tClass) {
    //        Manager.stream()
    //                .filter(tClass::isInstance)
    //                .map(object -> (T) object)
    //                .forEach(subscriberConsumer);
    //    }
    @SafeVarargs
    fun <T : ISubscriber> Subscribe(vararg objectToAdd: T)
    {
        subscribeManageGroups.addAll(objectToAdd)
    }

    @SafeVarargs
    fun <T : ISubscriber> UnSubscribe(vararg objectToAdd: T)
    {
        subscribeManageGroups.removeAll(objectToAdd.toSet())
    }

    fun getHangUpCardOrbGroup(): HangUpCardGroup?
    {
        return subscribeManageGroups
            .filterIsInstance<HangUpCardGroup>()
            .firstOrNull()
    }

    fun getBarRenderManager(): BarRenderManager? = subscribeManageGroups
        .filterIsInstance<BarRenderManager>()
        .firstOrNull()

    fun getPetManager(): PetManager? = subscribeManageGroups
        .filterIsInstance<PetManager>()
        .firstOrNull()

    private fun ResetAll()
    {
        RenderInBattle.clearAll()
        costMap = HashMap()
        subscribeManageGroups.clear()
    }
}
