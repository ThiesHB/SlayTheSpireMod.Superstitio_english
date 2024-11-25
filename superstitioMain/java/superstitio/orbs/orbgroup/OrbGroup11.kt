package superstitio.orbs.orbgroup

import basemod.ReflectionHacks
import basemod.interfaces.OnPlayerTurnStartSubscriber
import basemod.interfaces.OnPowersModifiedSubscriber
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot
import com.megacrit.cardcrawl.orbs.Plasma
import com.megacrit.cardcrawl.vfx.TextAboveCreatureEffect
import superstitio.orbs.actions.AnimationOrbOnMonsterAction
import superstitio.orbs.actions.ChannelOnOrbGroupAction
import superstitio.orbs.actions.EvokeFirstOnMonsterAction
import superstitio.orbs.actions.FlashOrbEffect
import superstitio.orbs.orbgroup.OrbEventSubscriber.OnOrbChannelSubscriber
import superstitio.orbs.orbgroup.OrbEventSubscriber.OnOrbEvokeSubscriber
import superstitioapi.SuperstitioApiSubscriber.AtEndOfPlayerTurnPreCardSubscriber
import superstitioapi.renderManager.inBattleManager.RenderInBattle
import superstitioapi.utils.ActionUtility
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer

abstract class OrbGroup @JvmOverloads constructor(
    hitbox: Hitbox,
    initMaxOrbs: Int,
    customEmptyOrb: AbstractOrb = EmptyOrbSlot()
) : RenderInBattle, OnPowersModifiedSubscriber, OnPlayerTurnStartSubscriber, AtEndOfPlayerTurnPreCardSubscriber {
    var CustomEmptyOrb: AbstractOrb = customEmptyOrb
    var orbs: MutableList<AbstractOrb?> = ArrayList()
    var hitbox: Hitbox = Hitbox(hitbox.x, hitbox.y, hitbox.width, hitbox.height)
    private var _maxOrbs = 0

    init {
        this.increaseMaxOrbs(initMaxOrbs)
        RenderInBattle.Register(RenderInBattle.RenderType.Normal, this)
    }

    fun <T> forEachOrbInThisOrbGroup(consumer: BiConsumer<AbstractOrb?, T>, arg: T) {
        for (orb in this.orbs) {
            consumer.accept(orb, arg)
        }
    }

    fun forEachOrbInThisOrbGroup(consumer: Consumer<AbstractOrb?>) {
        for (orb in this.orbs) {
            consumer.accept(orb)
        }
    }

    fun <TArg, TOrb : AbstractOrb?> forEachOrbInThisOrbGroup(
        OrbClass: Class<TOrb>,
        consumer: BiConsumer<TOrb?, TArg>, arg: TArg
    ) {
        for (orb in this.orbs) {
            if (OrbClass.isInstance(orb)) {
                consumer.accept(OrbClass.cast(orb),arg)
            }
        }
    }

    fun <TOrb : AbstractOrb?> forEachOrbInThisOrbGroup(
        OrbClass: Class<TOrb>,
        consumer: Consumer<TOrb?>
    ) {
        for (orb in this.orbs) {
            if (OrbClass.isInstance(orb)) {
                consumer.accept(OrbClass.cast(orb))
            }
        }
    }


    fun EnhanceOrb(orb: AbstractOrb, amount: Int) {
        if (this.isEmptySlot(orb) || orb is Plasma) return
        val passive = ReflectionHacks.getPrivate<Int>(orb, AbstractOrb::class.java, "basePassiveAmount")
        val evoke = ReflectionHacks.getPrivate<Int>(orb, AbstractOrb::class.java, "baseEvokeAmount")
        ReflectionHacks.setPrivate(orb, AbstractOrb::class.java, "basePassiveAmount", passive + amount)
        ReflectionHacks.setPrivate(orb, AbstractOrb::class.java, "baseEvokeAmount", evoke + amount)
        orb.updateDescription()
        ActionUtility.addEffect(FlashOrbEffect(orb.cX, orb.cY))
        ActionUtility.addEffect(TextAboveCreatureEffect(orb.hb.cX, orb.hb.cY, TEXT[0], Color.WHITE.cpy()))
    }

    val newCustomEmptyOrb: AbstractOrb
        get() = CustomEmptyOrb.makeCopy()

    fun isEmptySlot(orb: AbstractOrb?): Boolean {
        return orb!!.ID == CustomEmptyOrb.ID
    }

    open fun findFirstEmptyOrb(): Int {
        var index = -1
        for (i in orbs.indices) {
            val o = orbs[i]
            if (isEmptySlot(o)) {
                index = i
                break
            }
        }
        return index
    }

    /**
     * 塞入球
     */
    fun channelOrb(orb: AbstractOrb) {
        if (GetMaxOrbs() <= 0) return
        if (hasNoEmptySlot()) {
            AbstractDungeon.actionManager.addToTop(ChannelOnOrbGroupAction(this, orb))
            AbstractDungeon.actionManager.addToTop(EvokeFirstOnMonsterAction(this, 1))
            AbstractDungeon.actionManager.addToTop(AnimationOrbOnMonsterAction(this, 1))
            return
        }
        addNewOrb(orb)
    }

    /**
     * 激发球
     */
    fun evokeFirstOrb() {
        evokeOrb(0)
    }

    /**
     * 激发指定球，填补空缺
     */
    fun evokeOrb(slotIndex: Int) {
        if (hasNoOrb()) return
        val orbEvoked = orbs[slotIndex]
        orbEvoked!!.onEvoke()
        OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS.forEach(Consumer { sub: OnOrbEvokeSubscriber? ->
            sub!!.onOrbEvoke(
                orbEvoked,
                AbstractDungeon.player
            )
        })
        val newSize = orbs.size - 1
        for (i in slotIndex until newSize) {
            Collections.swap(orbs, i + 1, i)
        }
        orbs[newSize] = newCustomEmptyOrb
        letEachOrbToSlotPlaces()
        this.onOrbEvoke(orbEvoked)
    }

    /**
     * 激发球，不填补空缺
     */
    fun evokeOrbAndNotFill(slotIndex: Int) {
        if (hasNoOrb()) return
        val orbEvoked = orbs[slotIndex]
        orbEvoked!!.onEvoke()
        OrbEventSubscriber.ON_ORB_EVOKE_SUBSCRIBERS.forEach(Consumer { sub: OnOrbEvokeSubscriber? ->
            sub!!.onOrbEvoke(
                orbEvoked,
                AbstractDungeon.player
            )
        })
        orbs[slotIndex] = newCustomEmptyOrb
        letEachOrbToSlotPlaces()
        this.onOrbEvoke(orbEvoked)
    }

    //设置球的位置的函数
    protected open fun makeSlotPlace(slotIndex: Int): Vector2 {
        var orbPlace: Vector2
        orbPlace = makeSlotPlaceAsRound(hitbox.width / 2.0f, slotIndex)
        if (GetMaxOrbs() == 1) {
            orbPlace = makeSlotPlaceAsSingle(hitbox.width / 2.0f)
        }
        return orbPlace
    }

    protected fun addNewOrb(orb: AbstractOrb) {
        val index = findFirstEmptyOrb()
        val target = orbs[index]
        orb.cX = target!!.cX
        orb.cY = target.cY
        orbs[index] = orb
        letOrbToSlotPlace(orb, index)
        orb.updateDescription()
        orb.playChannelSFX()
        AbstractDungeon.actionManager.orbsChanneledThisCombat.add(orb)
        AbstractDungeon.actionManager.orbsChanneledThisTurn.add(orb)
        OrbEventSubscriber.ON_ORB_CHANNEL_SUBSCRIBERS.forEach(Consumer { sub: OnOrbChannelSubscriber? ->
            sub!!.onOrbChannel(
                orb,
                AbstractDungeon.player
            )
        })
        orb.applyFocus()
        this.onOrbChannel(orb)
    }

    protected abstract fun onOrbChannel(channeledOrb: AbstractOrb?)

    //    public final void letOrbToSlotPlace(final int slotIndex) {
    //        letOrbToSlotPlace(orbs.get(slotIndex), slotIndex);
    //    }
    protected abstract fun onOrbEvoke(evokedOrb: AbstractOrb?)

    private fun makeSlotPlaceAsSingle(height: Float): Vector2 {
        return Vector2(0f, height + hitbox.height / 2.0f)
    }

    private fun hasNoOrb(): Boolean {
        return orbs.isEmpty() || orbs.stream().allMatch(this::isEmptySlot)
    }

    //设置球的位置的函数，自动移动球的位置
    fun letOrbToSlotPlace(orb: AbstractOrb?, slotIndex: Int) {
        val orbPlace = makeSlotPlace(slotIndex)
        orb!!.tX = orbPlace.x + hitbox.cX
        orb.tY = orbPlace.y + hitbox.cY
        orb.hb.move(orb.tX, orb.tY)
    }

    fun letEachOrbToSlotPlaces() {
        val bound = orbs.size
        for (i in 0 until bound) {
            letOrbToSlotPlace(orbs[i], i)
        }
    }

    fun hasNoEmptySlot(): Boolean {
        return !hasEmptySlot()
    }

    fun hasEmptySlot(): Boolean {
        return orbs.stream().anyMatch(this::isEmptySlot)
    }

    fun hasOrb(): Boolean {
        return orbs.stream().anyMatch { orb: AbstractOrb? -> !isEmptySlot(orb) }
    }

    fun increaseMaxOrbs(amount: Int) {
        _maxOrbs += amount

        for (i in 0 until amount) {
            orbs.add(newCustomEmptyOrb)
        }

        letEachOrbToSlotPlaces()
    }

    fun decreaseMaxOrbs(amount: Int) {
        if (this._maxOrbs <= 0) return
        _maxOrbs -= amount
        if (this._maxOrbs < 0) this._maxOrbs = 0

        if (orbs.isNotEmpty()) {
            orbs.removeAt(orbs.size - 1)
        }

        letEachOrbToSlotPlaces()
    }

    fun triggerEvokeAnimation(index: Int) {
        if (GetMaxOrbs() <= 0) {
            return
        }
        if (orbs.isNotEmpty()) {
            orbs[index]!!.triggerEvokeAnimation()
        }
    }

    fun GetMaxOrbs(): Int {
        return this._maxOrbs
    }

    protected fun makeSlotPlaceAsSpiral(startRadius: Float, slotIndex: Int): Vector2 {
        val eachAddRadius = 3.0f
        val distanceToOrbGroupCenter = startRadius + slotIndex * eachAddRadius * Settings.scale //螺线
        val fullAngle = 100.0f + GetMaxOrbs() * 12.0f
        return makeSlotPlacePolar(slotIndex, distanceToOrbGroupCenter, fullAngle)
    }

    protected fun makeSlotPlaceAsRound(radius: Float, slotIndex: Int): Vector2 {
        val distanceToOrbGroupCenter = radius + GetMaxOrbs() * 10.0f * Settings.scale //圆
        val fullAngle = 60.0f + GetMaxOrbs() * 12.0f
        return makeSlotPlacePolar(slotIndex, distanceToOrbGroupCenter, fullAngle)
    }

    protected fun makeSlotPlaceLine(totalLength: Float, slotIndex: Int): Vector2 {
        val offsetX = OffsetPercentageBySlotIndex_TwoEnd(slotIndex.toFloat()) * totalLength
        val vector2 = Vector2()
        vector2.x = offsetX
        vector2.y = hitbox.height / 2.0f
        return vector2
    }

    protected fun makeSlotPlacePolar(slotIndex: Int, distanceToOrbGroupCenter: Float, fullAngle: Float): Vector2 {
        var slotAngle = fullAngle
        val offsetAngle = slotAngle / 2.0f
        slotAngle *= slotIndex / (GetMaxOrbs() - 1.0f)
        slotAngle += 90.0f - offsetAngle
        val vector2 = Vector2()
        vector2.x = distanceToOrbGroupCenter * MathUtils.cosDeg(slotAngle)
        vector2.y = distanceToOrbGroupCenter * MathUtils.sinDeg(slotAngle) + hitbox.height / 2.0f
        return vector2
    }

    protected fun OffsetPercentageBySlotIndex_TwoEnd(slotIndex: Float): Float {
        val maxOrbs = GetMaxOrbs().toFloat()
        return ((slotIndex + 1) / (maxOrbs + 1) - 0.5f)
    }

    protected fun OffsetPercentageBySlotIndex_Cycle(slotIndex: Float): Float {
        val maxOrbs = GetMaxOrbs().toFloat()
        return (slotIndex) / (maxOrbs)
    }

    override fun receiveOnPlayerTurnStart() {
        this.forEachOrbInThisOrbGroup { obj: AbstractOrb? -> obj!!.onStartOfTurn() }
    }

    override fun receivePowersModified() {
        this.forEachOrbInThisOrbGroup { obj: AbstractOrb? -> obj!!.updateDescription() }
    }


    override fun render(sb: SpriteBatch) {
        if (ActionUtility.isNotInBattle) return
        this.forEachOrbInThisOrbGroup({ obj: AbstractOrb?, spriteBatch: SpriteBatch? -> obj!!.render(spriteBatch) }, sb)
    }

    override fun update() {
        if (ActionUtility.isNotInBattle) return
        this.forEachOrbInThisOrbGroup { orb: AbstractOrb? ->
            orb!!.update()
            orb.updateAnimation()
        }
    }

    override fun receiveAtEndOfPlayerTurnPreCard() {
        this.forEachOrbInThisOrbGroup { obj: AbstractOrb? -> obj!!.onEndOfTurn() }
    }

    companion object {
        private val TEXT = arrayOf("  A  ")
        private const val MAX_MAX_ORB = 10
        fun <T : AbstractOrb?> makeOrbCopy(orb: T, clazz: Class<T>): T {
            val tmp = orb!!.makeCopy()
            val passive = ReflectionHacks.getPrivate<Int>(orb, AbstractOrb::class.java, "basePassiveAmount")
            val evoke = ReflectionHacks.getPrivate<Int>(orb, AbstractOrb::class.java, "baseEvokeAmount")
            ReflectionHacks.setPrivate(tmp, AbstractOrb::class.java, "basePassiveAmount", passive)
            ReflectionHacks.setPrivate(tmp, AbstractOrb::class.java, "baseEvokeAmount", evoke)
            return clazz.cast(tmp)
        }

        fun monsters(): MutableList<AbstractMonster> {
            return AbstractDungeon.getMonsters().monsters
        }
    }
}
