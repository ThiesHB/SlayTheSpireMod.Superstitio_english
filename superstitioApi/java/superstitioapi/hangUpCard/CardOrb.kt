package superstitioapi.hangUpCard

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.actions.GameActionManager
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardTarget
import com.megacrit.cardcrawl.cards.CardGroup
import com.megacrit.cardcrawl.cards.CardGroup.CardGroupType
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect
import superstitioapi.DataUtility
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility.FunctionReturnSelfType
import superstitioapi.utils.ActionUtility.VoidSupplier
import superstitioapi.utils.CardUtility
import superstitioapi.utils.CostSmart
import superstitioapi.utils.CreatureUtility
import java.util.concurrent.atomic.AtomicBoolean
import java.util.function.Consumer
import kotlin.math.abs

abstract class CardOrb(card: AbstractCard, cardGroupReturnAfterEvoke: CardGroup?, OrbCounter: CostSmart) :
    AbstractOrb()
{
    val thisCardGroup: CardGroup = CardGroup(CardGroupType.UNSPECIFIED)
    val targetTypeOrigin: HangOnTarget
    val actionTypeOrigin: HangEffectType
    val cardHolder: CardGroup = CardGroup(CardGroupType.UNSPECIFIED)
    val originCard: AbstractCard
    var cardGroupReturnAfterEvoke: CardGroup? = null
    var targetType: HangOnTarget = HangOnTarget.None
    var actionType: HangEffectType = HangEffectType.None
    var movingType: FunctionReturnSelfType?
    var lastTarget: AbstractCreature? = null
    open fun afterOrbCounterChange(field: CostSmart)
    {
        fun setPassiveAmount(amount: Int)
        {
            super.passiveAmount = amount
            super.basePassiveAmount = amount
        }

        fun setEvokeAmount(amount: Int)
        {

            super.evokeAmount = amount
            super.baseEvokeAmount = amount
        }
        setEvokeAmount(field.toInt { it - 1 })
        setPassiveAmount(field.toInt())
        tryUpdateOrbCounterInCard(this.cardRawDescriptionWillShow, field)
    }

    var orbCounter: CostSmart = OrbCounter
        set(value)
        {
            field = value
            afterOrbCounterChange(field)
        }

    protected val fakeCard: AbstractCard
    protected var isNewMovingModeSetup: Boolean = false
    private var cardRawDescriptionWillShow: String? = null
    private var afterEvokeConsumer: Consumer<CardOrb>? = null
    private var shouldRemove = false
    private var isRemoved = false

    private var stopShowOriginCard = false

    init
    {
        this.ID = ORB_ID
        this.name = ""
        super.basePassiveAmount = 0
        super.passiveAmount = 0
        super.baseEvokeAmount = 0
        super.evokeAmount = 0
        this.description = ""


        this.cardGroupReturnAfterEvoke = cardGroupReturnAfterEvoke
        cardHolder.addToTop(card)
        this.originCard = card
        originCard.targetDrawScale = DRAW_SCALE_SMALL
        val fakeCard = card.makeStatEquivalentCopy()
        this.fakeCard = fakeCard
        setUpShownCard(fakeCard) //什么替身文学，绷不住了

        setHoverTypeFromCard(this.fakeCard.target)
        targetTypeOrigin = targetType
        actionTypeOrigin = actionType

        thisCardGroup.addToTop(this.fakeCard)
        this.movingType = State_Idle()

        this.updateDescription()
    }

    fun setCardRawDescriptionWillShow(cardRawDescriptionWillShow: String, vararg args: Any?): CardOrb
    {
        if (args.isNotEmpty())
            this.cardRawDescriptionWillShow = String.format(cardRawDescriptionWillShow, *args)
        else this.cardRawDescriptionWillShow = cardRawDescriptionWillShow
        tryUpdateOrbCounterInCard(this.cardRawDescriptionWillShow, orbCounter)
        return this
    }

    fun setTriggerDiscardIfMoveToDiscard(): CardOrb
    {
        this.setAfterEvokeConsumer { orb: CardOrb ->
            if (orb.orbCounter.isZero()) return@setAfterEvokeConsumer
            if (orb.cardGroupReturnAfterEvoke !== AbstractDungeon.player.discardPile) return@setAfterEvokeConsumer
            orb.originCard.triggerOnManualDiscard()
            GameActionManager.incrementDiscard(false)
        }
        return this
    }

    open fun addToBot_HangCard()
    {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(this)
        AutoDoneInstantAction.addToBotAbstract(AbstractDungeon::onModifyPower)
    }

    /**
     * 这个函数是被addToBot调用的
     */
    fun onRemove()
    {
        if (isRemoved) return
        this.isRemoved = true
        if (cardGroupReturnAfterEvoke != null && cardHolder.contains(originCard))
        {
            originCard.current_x = fakeCard.current_x
            originCard.current_y = fakeCard.current_y
            originCard.drawScale = fakeCard.drawScale
            when (cardGroupReturnAfterEvoke!!.type)
            {
                CardGroupType.DRAW_PILE    -> cardHolder.moveToDeck(originCard, true)
                CardGroupType.HAND         -> CardUtility.moveToHandOrDiscardWhenMaxHand(cardHolder, originCard)
                CardGroupType.EXHAUST_PILE ->
                {
                    AbstractDungeon.effectList.add(ExhaustCardEffect(fakeCard))
                    cardHolder.moveToExhaustPile(originCard)
                }

                CardGroupType.DISCARD_PILE -> cardHolder.moveToDiscardPile(originCard)
                else                       -> cardHolder.moveToDiscardPile(originCard)
            }
        }
        else AbstractDungeon.effectList.add(ExhaustCardEffect(fakeCard))
        onRemoveCard()
        if (afterEvokeConsumer != null)
            afterEvokeConsumer!!.accept(this)
    }

    fun setDesc(description: String?): CardOrb
    {
        this.description = description
        fakeCard.rawDescription = description
        fakeCard.initializeDescription()
        return this
    }

    fun checkShouldRemove()
    {
        if (shouldRemove) return
        this.shouldRemove = this.orbCounter.isZero() && checkShouldStopMoving()
    }

    fun tryMoveTo(vector2: Vector2)
    {
        fakeCard.target_x = vector2.x
        fakeCard.target_y = vector2.y
    }

    fun setShowCard(showCard: AbstractCard): CardOrb
    {
        setUpShownCard(showCard)
        return this
    }

    //
    //    public static void renderCardPreview(){
    //        float tmpScale = this.drawScale * 0.8F;
    //        if (this.current_x > (float) Settings.WIDTH * 0.75F) {
    //            this.cardsToPreview.current_x = this.current_x + (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
    //        } else {
    //            this.cardsToPreview.current_x = this.current_x - (IMG_WIDTH / 2.0F + IMG_WIDTH / 2.0F * 0.8F + 16.0F) * this.drawScale;
    //        }
    //
    //        this.cardsToPreview.current_y = this.current_y + (IMG_HEIGHT / 2.0F - IMG_HEIGHT / 2.0F * 0.8F) * this.drawScale;
    //        this.cardsToPreview.drawScale = tmpScale;
    //        this.cardsToPreview.render(sb);
    //    }
    fun setTargetType(cardTarget: CardTarget?): CardOrb
    {
        setHoverTypeFromCard(cardTarget)
        return this
    }

    private var isZeroActionAccepted: Boolean = false
        get()
        {
            if (!this.orbCounter.isZero())
                return false
            return field
        }

    protected fun tryCheckZeroAndAcceptAction(action: VoidSupplier)
    {
        fun trySetZeroActionAccepted()
        {
            if (this.orbCounter.isZero())
                isZeroActionAccepted = true
        }
        if (isZeroActionAccepted) return
        action.addToBotAsAbstractAction()
        trySetZeroActionAccepted()
    }

    fun StartHitCreature(target: AbstractCreature)
    {
        this.movingType = FunctionReturnSelfType(this::State_Moving)
        val creature = CreatureUtility.getTargetOrRandomMonster(target)
        this.tryMoveTo(
            Vector2(
                this.cX - (this.cX - creature.hb.cX) / 1.2f,
                this.cY - (this.cY - creature.hb.cY) / 1.2f
            )
        )
        fakeCard.superFlash(CardUtility.getColorFormCard(fakeCard))
    }

    abstract fun forceAcceptAction(card: AbstractCard)

    fun setShouldRemove()
    {
        this.shouldRemove = true
    }

    fun ifShouldRemove(): Boolean
    {
        return this.shouldRemove
    }

    fun setAfterEvokeConsumer(afterEvokeConsumer: Consumer<CardOrb>?): CardOrb
    {
        this.afterEvokeConsumer = afterEvokeConsumer
        return this
    }

    protected fun setHoverTypeFromCard(cardTarget: CardTarget?)
    {
        when (cardTarget)
        {
            CardTarget.ENEMY, CardTarget.ALL_ENEMY                     ->
            {
                targetType = HangOnTarget.Enemy
                actionType = HangEffectType.Bad
            }

            CardTarget.SELF                                            ->
            {
                targetType = HangOnTarget.Self
                actionType = HangEffectType.Good
            }

            CardTarget.NONE, CardTarget.ALL, CardTarget.SELF_AND_ENEMY ->
            {
                targetType = HangOnTarget.None
                actionType = HangEffectType.Special
            }

            else                                                       ->
            {
                targetType = HangOnTarget.None
                actionType = HangEffectType.Special
            }
        }
    }

    protected open fun checkAndSetTheHoverType(): VoidSupplier
    {
        return when (targetType)
        {
            HangOnTarget.Enemy -> VoidSupplier(this::State_WhenHoverCard_OnMonster)
            HangOnTarget.Self  -> VoidSupplier(this::State_WhenHoverCard_OnSelf)
            HangOnTarget.None  -> VoidSupplier(this::State_WhenHoverCard_OnNothing)
            else               -> VoidSupplier(this::State_WhenHoverCard_OnNothing)
        }
    }

    protected abstract fun onRemoveCard()

    protected fun checkShouldStopMoving(): Boolean
    {
        return abs((fakeCard.current_y - fakeCard.target_y).toDouble()) < 0.01f && abs((fakeCard.current_x - fakeCard.target_x).toDouble()) < 0.01f
    }

    protected fun updateAnimationIdle()
    {
        if (isCardHoveredInCardGroup && isCardHovered)
        {
            fakeCard.target_x = this.cX
            fakeCard.target_y = this.cY + YOffsetWhenHovered()
            fakeCard.targetDrawScale = DRAW_SCALE_BIG
            //            this.drawOrder = DrawOrder.top;
        }
        else
        {
            fakeCard.target_x = this.cX
            fakeCard.target_y = this.cY + YOffsetWhenHovered()
            fakeCard.targetDrawScale = DRAW_SCALE_SMALL
            //            this.drawOrder = DrawOrder.bottom;
        }
    }

//    fun updateOrbAmount()
//    {
//
//    }


    protected fun YOffsetBoBing(): Float
    {
        return ANIMATION_Y_SCALE * bobEffect.y * fakeCard.drawScale * DRAW_SCALE_BIG / DRAW_SCALE_SMALL
    }

    protected val isCardHoveredInCardGroup: Boolean
        get()
        {
            val isHovered = AtomicBoolean(false)
            HangUpCardGroup.forHangUpCardGroup { hangUpCardGroup: HangUpCardGroup? ->
                if (hangUpCardGroup!!.isCardHovered(
                        this
                    )
                )
                {
                    isHovered.set(true)
                }
            }.get()
            return isHovered.get()
        }

    val isCardHovered: Boolean
        get() = fakeCard.hb.hovered

    protected fun YOffsetWhenHovered(): Float
    {
        return -fakeCard.hb.height * (fakeCard.drawScale) / DRAW_SCALE_BIG / 2
    }

    protected fun showEvokeNum()
    {
//        super.evokeAmount = max(0, super.evokeAmount)
        fakeCard.costForTurn = this.orbCounter.toInt { it - 1 }
        fakeCard.isCostModified = true
        fakeCard.beginGlowing()
    }

    protected fun showPassiveNum()
    {
//        super.passiveAmount = max(0, super.passiveAmount)
        fakeCard.stopGlowing()
        fakeCard.costForTurn = this.orbCounter.toInt()
        fakeCard.isCostModified = false
    }

    protected fun State_Moving(): FunctionReturnSelfType
    {
        showPassiveNum()
        fakeCard.targetDrawScale = DRAW_SCALE_MIDDLE
        if (checkShouldStopMoving())
        {
            return FunctionReturnSelfType(this::State_Idle)
        }
        return FunctionReturnSelfType(this::State_Moving)
    }

    protected fun State_WhenHoverCard_OnMonster()
    {
        updateIfFocusOnMonster()
    }

    protected fun State_WhenHoverCard_OnSelf()
    {
        updateIfFocusOnSelf()
    }

    protected fun State_WhenHoverCard_OnNothing()
    {
        updateIfFocusOnNothing()
    }

    protected fun updateIfFocusOnMonster()
    {
        val target: AbstractMonster = hoveredMonster ?: return
        if (!CreatureUtility.isAlive(target)) return
        this.lastTarget = target
        this.tryMoveTo(
            Vector2(
                this.cX - (this.cX - target.hb.cX) / 3,
                this.cY - (this.cY - target.hb.cY) / 3 + YOffsetWhenHovered()
            )
        )
    }

    protected fun updateIfFocusOnSelf()
    {
        if (!CreatureUtility.isAlive(AbstractDungeon.player)) return
        this.lastTarget = AbstractDungeon.player
        this.tryMoveTo(
            Vector2(
                this.cX - (this.cX - AbstractDungeon.player.hb.cX) / 2.5f,
                this.cY - (this.cY - AbstractDungeon.player.hb.cY) / 2.5f + YOffsetWhenHovered()
            )
        )
    }

    protected fun updateIfFocusOnNothing()
    {
        this.tryMoveTo(Vector2(this.cX, this.cY + YOffsetWhenHovered() + YOffsetWhenHovered()))
    }

    protected fun State_Idle(): FunctionReturnSelfType
    {
        this.targetType = targetTypeOrigin
        this.actionType = actionTypeOrigin
        showPassiveNum()
        updateAnimationIdle()
        if (ifHoveredRightCard())
        {
            return FunctionReturnSelfType(this::State_WhenHoverCard)
        }
        return FunctionReturnSelfType(this::State_Idle)
    }

    protected fun State_WhenHoverCard(): FunctionReturnSelfType
    {
        showEvokeNum()
        val hoveredCard = AbstractDungeon.player.hoveredCard
        if (hoveredCard is Card_TriggerHangCardManually) fakeCard.costForTurn =
            (hoveredCard as Card_TriggerHangCardManually).forceChangeOrbCounterShown(
                this
            )
        fakeCard.targetDrawScale = DRAW_SCALE_MIDDLE
        checkAndSetTheHoverType().get()
        if (!ifHoveredRightCard())
        {
            return FunctionReturnSelfType(this::State_Idle)
        }
        return FunctionReturnSelfType(this::State_WhenHoverCard)
    }

    protected fun ifHoveredRightCard(): Boolean
    {
        val hoveredCard = AbstractDungeon.player.hoveredCard ?: return false
        if (!(ReflectionHacks.getPrivate<Any>(
                AbstractDungeon.player,
                AbstractPlayer::class.java,
                "isHoveringCard"
            ) as Boolean)
        )
        {
            return false
        }
        return TestIfCardIsRight_hover(hoveredCard)
    }

    protected open fun TestIfCardIsRight_hover(hoveredCard: AbstractCard?): Boolean
    {
        if (hoveredCard == null) return false
        if (hoveredCard is Card_TriggerHangCardManually)
        {
            return (hoveredCard as Card_TriggerHangCardManually).forceFilterCardOrbToHoveredMode(this)
        }
        return false
    }

    private fun tryUpdateOrbCounterInCard(cardRawDescriptionWillShow: String?, NewOrbCounter: CostSmart)
    {
        if (cardRawDescriptionWillShow.isNullOrEmpty()) return
        fakeCard.rawDescription =
            cardRawDescriptionWillShow.replace("superstitioApi:!HANGING_TIME!", NewOrbCounter.toString())
    }

    private fun setUpShownCard(card: AbstractCard)
    {
        this.fakeCard.drawScale = card.drawScale
        this.fakeCard.transparency = 1.0f
        this.fakeCard.current_x = card.current_x
        this.fakeCard.current_y = card.current_y

        this.fakeCard.targetDrawScale = DRAW_SCALE_SMALL
        this.fakeCard.isCostModified = false

        this.fakeCard.cost = this.orbCounter.toInt()
        this.fakeCard.costForTurn = this.fakeCard.cost
    }

    override fun onStartOfTurn()
    {
    }

    override fun onEvoke()
    {
    }

    override fun render(spriteBatch: SpriteBatch)
    {
        if (!stopShowOriginCard)
        {
            if (originCard.drawScale != originCard.targetDrawScale)
            {
                originCard.render(spriteBatch)
                return
            }
            else
            {
                stopShowOriginCard = true
            }
        }

        val offset = YOffsetBoBing()
        fakeCard.current_y += offset
        fakeCard.render(spriteBatch)
        if (fakeCard.hb.hovered)
        {
            fakeCard.renderCardTip(spriteBatch)
        }
        fakeCard.current_y -= offset
    }

    override fun update()
    {
        if (!stopShowOriginCard)
        {
            if (originCard.drawScale != originCard.targetDrawScale)
            {
                originCard.target_x = this.cX
                originCard.target_y = this.cY + YOffsetWhenHovered()
                originCard.targetDrawScale = DRAW_SCALE_SMALL
                originCard.current_y += YOffsetBoBing()
                originCard.update()
            }
            else
            {
                stopShowOriginCard = true
            }
        }

        hb.update()
        fakeCard.update()
        fakeCard.updateHoverLogic()
        this.movingType = movingType!!.get()
        fakeCard.glowColor = actionType.color
//        updateOrbAmount()
    }

    open fun onPowerModified()
    {
    }

    override fun updateDescription()
    {
        fakeCard.applyPowers()
        fakeCard.calculateDamageDisplay(hoveredMonsterSafe)
        fakeCard.initializeDescription()
        this.tryUpdateOrbCounterInCard(this.cardRawDescriptionWillShow, orbCounter)

        if (fakeCard.cardsToPreview != null)
        {
            fakeCard.cardsToPreview.applyPowers()
            fakeCard.cardsToPreview.initializeDescription()
            fakeCard.cardsToPreview.calculateDamageDisplay(hoveredMonsterSafe)
        }
        originCard.applyPowers()
        originCard.calculateDamageDisplay(hoveredMonsterSafe)
        originCard.initializeDescription()
    }

    override fun playChannelSFX()
    {
    }

    override fun applyFocus()
    {
    }

    sealed class HangEffectType(color: Color)
    {

        val color: Color = color.cpy()

        data object Good : HangEffectType(Color.GREEN)
        data object Bad : HangEffectType(Color.RED)
        data object Special : HangEffectType(Color.GOLD)
        data object None : HangEffectType(
            Color(
                Color.BLUE.r,
                Color.BLUE.g,
                Color.BLUE.b,
                0f
            )
        )

        companion object
        {
            fun values(): Array<HangEffectType>
            {
                return arrayOf(Good, Bad, Special, None)
            }

            fun valueOf(value: String): HangEffectType
            {
                return when (value)
                {
                    "Good"    -> Good
                    "Bad"     -> Bad
                    "Special" -> Special
                    "None"    -> None
                    else      -> throw IllegalArgumentException("No object superstitioapi.hangUpCard.CardOrb.HangEffectType.$value")
                }
            }
        }
    }

    sealed class HangOnTarget
    {
        data object Enemy : HangOnTarget()
        data object Self : HangOnTarget()
        data object None : HangOnTarget()
        companion object
        {
            fun values(): Array<HangOnTarget>
            {
                return arrayOf(Enemy, Self, None)
            }

            fun valueOf(value: String): HangOnTarget
            {
                return when (value)
                {
                    "Enemy" -> Enemy
                    "Self"  -> Self
                    "None"  -> None
                    else    -> throw IllegalArgumentException("No object superstitioapi.hangUpCard.CardOrb.HangOnTarget.$value")
                }
            }
        }
    }

    companion object
    {
        val ORB_ID: String = DataUtility.MakeTextID(CardOrb::class.java)
        const val ANIMATION_Y_SCALE: Float = 1.0f
        const val DRAW_SCALE_BIG: Float = 0.9f

        const val DRAW_SCALE_MIDDLE: Float = 0.5f
        const val DRAW_SCALE_SMALL: Float = 0.25f
        const val DRAW_SCALE_SMALL_BIGGER: Float = 0.30f
        private const val TIMER_ANIMATION = 2.0f
        val hoveredMonsterSafe: AbstractMonster
            get() = CreatureUtility.getMonsterOrRandomMonster(
                ReflectionHacks.getPrivate(
                    AbstractDungeon.player,
                    AbstractPlayer::class.java,
                    "hoveredMonster"
                )
            )

        val hoveredMonster: AbstractMonster?
            get()
            {
                val hoveredMonster: AbstractMonster? = ReflectionHacks.getPrivate<AbstractMonster>(
                    AbstractDungeon.player,
                    AbstractPlayer::class.java,
                    "hoveredMonster"
                )
                return hoveredMonster
            }
    }
}
