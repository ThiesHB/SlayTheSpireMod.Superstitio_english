package superstitioapi.powers.barIndepend

import basemod.interfaces.OnPowersModifiedSubscriber
import basemod.interfaces.PostPowerApplySubscriber
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.renderManager.inBattleManager.RenderInBattle
import superstitioapi.utils.CreatureUtility
import java.util.function.Consumer
import java.util.function.Supplier

class BarRenderManager : RenderInBattle, OnPowersModifiedSubscriber, PostPowerApplySubscriber {
    //    private final AbstractCreature creature;
    private val bars: MutableList<RenderOnThing> = ArrayList()

    init {
//        this.creature = creature;
        RenderInBattle.Register(RenderInBattle.RenderType.Panel, this)
    }

    /**
     * 如果多个显示在同一个BAR上面，则必须调用这个函数，否则会显示错误
     */
    fun removeChunk(hasBarRenderOnCreature: HasBarRenderOnCreature) {
        bars.forEach(Consumer { bar: RenderOnThing -> bar.removeChunk(hasBarRenderOnCreature) })
    }

    fun findPowers(): List<HasBarRenderOnCreature> {
        return allPowers.filter { power: AbstractPower -> power is HasBarRenderOnCreature }
            .map { power: AbstractPower -> power as HasBarRenderOnCreature }
    }

    fun findMatch_powerPointToBar(power: HasBarRenderOnCreature): RenderOnThing? {
        return bars.firstOrNull { bar: RenderOnThing -> bar.uuid_self == power.uuidPointTo() }
    }

    fun findMatch_barHasPower(bar: RenderOnThing): HasBarRenderOnCreature? {
        return findPowers().firstOrNull { power: HasBarRenderOnCreature ->
            bar.isUuidInThis(
                power.uuidOfSelf()
            )
        }
    }

    fun AutoRegisterAndRemove() {
        val power_HasNoBar =
            findPowers().filter { power: HasBarRenderOnCreature -> findMatch_powerPointToBar(power) == null }.toList()
        val bar_HasNoPower =
            bars.filter { bar: RenderOnThing -> findMatch_barHasPower(bar) == null }.toList()
        for (renderOnThing in bar_HasNoPower) {
            bars.remove(renderOnThing)
        }
        for (power in power_HasNoBar) {
            val added = power.makeNewBarRenderOnCreature().apply(Supplier(power::getBarRenderHitBox), power)
            bars.add(added)
        }
    }

    fun AutoMakeMessage() {
        findPowers().forEach { power: HasBarRenderOnCreature ->
            bars.forEach(
                Consumer { barRenderOnCreature: RenderOnThing ->
                    barRenderOnCreature.tryApplyMessage(
                        power.makeMessage()
                    )
                })
        }
    }

    private val allPowers: MutableList<AbstractPower>
        get() = CreatureUtility.getListMemberFromPlayerAndEachMonsters { creature: AbstractCreature ->
            creature.powers.filterNotNull()
        }

    override fun render(sb: SpriteBatch) {
        bars.forEach(Consumer { bar: RenderOnThing -> bar.render(sb) })
    }

    override fun update() {
        bars.forEach(Consumer(RenderOnThing::update))
    }

    override fun receivePowersModified() {
        AutoRegisterAndRemove()
        AutoMakeMessage()
    }

    override fun receivePostPowerApplySubscriber(
        abstractPower: AbstractPower?,
        abstractCreature: AbstractCreature?,
        abstractCreature1: AbstractCreature?
    ) {
        AutoRegisterAndRemove()
        AutoMakeMessage()
    }
}
