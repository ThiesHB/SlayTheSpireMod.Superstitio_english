package superstitioapi.hangUpCard

import basemod.interfaces.OnCardUseSubscriber
import basemod.interfaces.OnPlayerTurnStartSubscriber
import basemod.interfaces.OnPowersModifiedSubscriber
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitioapi.SuperstitioApiSubscriber.AtEndOfPlayerTurnPreCardSubscriber
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.renderManager.inBattleManager.InBattleDataManager
import superstitioapi.renderManager.inBattleManager.RenderInBattle
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.ActionUtility.VoidSupplier
import java.util.function.*
import java.util.stream.Stream

class HangUpCardGroup(hitbox: Hitbox) : RenderInBattle, OnCardUseSubscriber, OnPowersModifiedSubscriber,
    OnPlayerTurnStartSubscriber, AtEndOfPlayerTurnPreCardSubscriber {
    private val hitbox = Hitbox(hitbox.x, hitbox.y, hitbox.width, hitbox.height)
    private val cards = ArrayList<CardOrb>()
    private var remove_check_counter = 10
    private var hoveredCard: CardOrb? = null

    init {
        RenderInBattle.Register(RenderInBattle.RenderType.Normal, this)
        this.hitbox.moveX(this.hitbox.cX + this.hitbox.width * 2)
        this.hitbox.moveY(this.hitbox.cY + this.hitbox.height * 0.5f)
    }

    fun hasOrb(): Boolean {
        return cards.isNotEmpty()
    }

    fun removeCard(cardOrb: CardOrb) {
        cards.remove(cardOrb)
        cardOrb.onRemove()
        letEachOrbToSlotPlaces()
    }

    protected fun hangUpNewCard(orb: CardOrb) {
        cards.add(orb)
        letEachOrbToSlotPlaces()
    }

    fun isCardHovered(cardOrb: CardOrb): Boolean {
        if (hoveredCard == null) return false
        if (hoveredCard === cardOrb) return true
        return false
    }

    private fun <T> forEachOrbInThisOrbGroup(consumer: BiConsumer<CardOrb, T>, arg: T) {
        for (orb in this.cards) {
            consumer.accept(orb, arg)
        }
    }

    private fun forEachOrbInThisOrbGroup(consumer: Consumer<CardOrb>) {
        for (orb in this.cards) {
            consumer.accept(orb)
        }
    }

    private fun <TArg, TOrb : CardOrb> forEachOrbInThisOrbGroup(
        OrbClass: Class<TOrb>, consumer: BiConsumer<TOrb, TArg>, arg: TArg
    ) {
        for (orb in this.cards) {
            if (OrbClass.isInstance(orb)) {
                consumer.accept(orb as TOrb, arg)
            }
        }
    }

    private fun <TOrb : CardOrb> forEachOrbInThisOrbGroup(
        OrbClass: Class<TOrb>, consumer: Consumer<TOrb>
    ) {
        for (orb in this.cards) {
            if (OrbClass.isInstance(orb)) {
                val tOrb = orb as TOrb
                consumer.accept(tOrb)
            }
        }
    }

    private val cardOrbStream: Stream<CardOrb>
        get() = cards.stream()

    private fun removeUselessCard() {
        remove_check_counter--
        if (this.remove_check_counter >= 0) return
        this.remove_check_counter = 10
        this.forEachOrbInThisOrbGroup { orb: CardOrb ->
            orb.checkShouldRemove()
            if (orb.ifShouldRemove() || ActionUtility.isNotInBattle) AutoDoneInstantAction.addToBotAbstract(
                VoidSupplier { removeCard(orb) })
        }
    }

    //设置球的位置的函数，自动移动球的位置
    fun letOrbToSlotPlace(orb: CardOrb, slotIndex: Int) {
        val orbPlace = makeSlotPlaceLine(hitbox.width, slotIndex)
        orb.tX = orbPlace.x + hitbox.cX
        orb.tY = orbPlace.y + hitbox.cY
        orb.hb.move(orb.tX, orb.tY)
    }

    fun initOrbToSlotPlace(orb: CardOrb, slotIndex: Int) {
        val orbPlace = makeSlotPlaceLine(hitbox.width, slotIndex)
        orb.cX = orbPlace.x + hitbox.cX
        orb.cY = orbPlace.y + hitbox.cY
        orb.hb.move(orb.cX, orb.cY)
    }

    fun letEachOrbToSlotPlaces() {
        val bound = cards.size
        for (i in 0 until bound) {
            letOrbToSlotPlace(cards[i], i)
        }
    }

    val cardsAmount: Int
        get() = cards.size

    protected fun makeSlotPlaceLine(totalLength: Float, slotIndex: Int): Vector2 {
        val offsetX = OffsetPercentageBySlotIndex_TwoEnd(slotIndex.toFloat()) * totalLength
        val vector2 = Vector2()
        vector2.x = offsetX
        vector2.y = hitbox.height / 2.0f
        return vector2
    }

    protected fun OffsetPercentageBySlotIndex_TwoEnd(slotIndex: Float): Float {
        val maxOrbs = cardsAmount.toFloat()
        return ((slotIndex + 1) / (maxOrbs + 1) - 0.5f)
    }

    protected fun OffsetPercentageBySlotIndex_Cycle(slotIndex: Float): Float {
        val maxOrbs = cardsAmount.toFloat()
        return (slotIndex) / (maxOrbs)
    }

    override fun receiveOnPlayerTurnStart() {
        this.forEachOrbInThisOrbGroup(CardOrb::onStartOfTurn)
    }

    override fun receivePowersModified() {
        this.forEachOrbInThisOrbGroup(CardOrb::updateDescription)
    }

    override fun render(sb: SpriteBatch) {
        if (ActionUtility.isNotInBattle) return
        //        getCardOrbStream().filter(orb -> orb.drawOrder == CardOrb.DrawOrder.bottom).forEach(orb -> orb.render(sb));
//        getCardOrbStream().filter(orb -> orb.drawOrder == CardOrb.DrawOrder.middle).forEach(orb -> orb.render(sb));
        cardOrbStream.forEach { orb: CardOrb -> orb.render(sb) }
        if (hoveredCard != null) hoveredCard!!.render(sb)
    }

    //    private void checkIfPlayerHoverHandCard() {
    //        AbstractCard hoveredCard = AbstractDungeon.player.hoveredCard;
    //        if (!((boolean) ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "isHoveringCard")))
    //            return;
    //        if (hoveredCard == null) return;
    //        forEachOrbInThisOrbGroup(CardOrb_CardTrigger.class, CardOrb_CardTrigger::onPlayerHoveringHandCard,hoveredCard);
    //    }
    override fun update() {
        if (ActionUtility.isNotInBattle) return

        for (cardOrb in this.cards) {
            if (cardOrb.isCardHovered) {
                this.hoveredCard = cardOrb
                break
            }
        }

        if (hoveredCard != null && hoveredCard!!.ifShouldRemove()) hoveredCard = null

        this.forEachOrbInThisOrbGroup { orb: CardOrb ->
            orb.update()
            orb.updateAnimation()
        }
        //        checkIfPlayerHoverHandCard();
        removeUselessCard()
    }

    override fun receiveCardUsed(abstractCard: AbstractCard) {
        this.forEachOrbInThisOrbGroup(
            CardOrb_CardTrigger::class.java,
            CardOrb_CardTrigger::onCardUsed,
            abstractCard
        )
    }

    override fun receiveAtEndOfPlayerTurnPreCard() {
        this.forEachOrbInThisOrbGroup(CardOrb::onEndOfTurn)
    }

    companion object {
        fun monsters(): MutableList<AbstractMonster> {
            return AbstractDungeon.getMonsters().monsters
        }

        fun addToBot_AddCardOrbToOrbGroup(orb: CardOrb) {
            forHangUpCardGroup { hangUpCardGroup: HangUpCardGroup? -> hangUpCardGroup!!.hangUpNewCard(orb) }
                .addToBotAsAbstractAction()
        }

        fun forHangUpCardGroup(cardGroupConsumer: Consumer<HangUpCardGroup>): VoidSupplier {
            return VoidSupplier { cardGroupConsumer.let { InBattleDataManager.getHangUpCardOrbGroup() } }
        }

        fun <T> forEachHangUpCard(consumer: BiConsumer<CardOrb, T>, arg: T): VoidSupplier {
            return forHangUpCardGroup { hangUpCardGroup: HangUpCardGroup? ->
                for (orb in hangUpCardGroup!!.cards) {
                    consumer.accept(orb, arg)
                }
            }
        }

        fun forEachHangUpCard(consumer: BiConsumer<HangUpCardGroup, CardOrb>): VoidSupplier {
            return forHangUpCardGroup { hangUpCardGroup ->
                hangUpCardGroup.cards.forEach { orb ->
                    consumer.accept(hangUpCardGroup, orb)
                }
            }
        }

        fun forEachHangUpCard(consumer: Consumer<CardOrb>): VoidSupplier {
            return forHangUpCardGroup { hangUpCardGroup ->
                hangUpCardGroup.cards.forEach(consumer::accept)
            }
        }

        fun <TArg, TOrb : CardOrb> forEachHangUpCard(
            OrbClass: Class<TOrb>, consumer: BiConsumer<TOrb, TArg>, arg: TArg
        ): VoidSupplier {
            return forHangUpCardGroup { hangUpCardGroup ->
                hangUpCardGroup.cards.filterIsInstance(OrbClass).forEach {
                    consumer.accept(it, arg)
                }
            }
        }

        fun <TOrb : CardOrb> forEachHangUpCard(
            OrbClass: Class<TOrb>,
            consumer: BiConsumer<HangUpCardGroup, TOrb>
        ): VoidSupplier {
            return forHangUpCardGroup { hangUpCardGroup ->
                hangUpCardGroup.cards.filterIsInstance(OrbClass).forEach {
                    consumer.accept(hangUpCardGroup, it)
                }
            }
        }

        fun <TOrb : CardOrb> forEachHangUpCard(
            OrbClass: Class<TOrb>,
            consumer: Consumer<TOrb>
        ): VoidSupplier {
            return forHangUpCardGroup { hangUpCardGroup ->
                hangUpCardGroup.cards.filterIsInstance(OrbClass).forEach(consumer)
            }
        }
    }
}
