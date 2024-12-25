package superstitio.orbs.orbgroup

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.megacrit.cardcrawl.cards.CardQueueItem
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.helpers.Hitbox
import com.megacrit.cardcrawl.orbs.AbstractOrb
import superstitio.InBattleDataManager
import superstitio.cards.general.TempCard.GangBang
import superstitio.orbs.SexMarkEmptySlot
import superstitio.orbs.SexMarkOrb
import superstitio.orbs.SexMarkOrb_Inside
import superstitio.orbs.SexMarkOrb_Outside
import superstitio.orbs.actions.GiveSexMarkToOrbGroupInstantAction
import superstitioapi.actions.AutoDoneInstantAction
import java.util.concurrent.atomic.AtomicInteger

class SexMarkOrbGroup(hitbox: Hitbox, var scoreRate: Double) :
    OrbGroup(hitbox, SexMarkSetupOrbMax, SexMarkEmptySlot())
{
    private val fillSide: Boolean

    init
    {
        this.hitbox.moveX(this.hitbox.cX + this.hitbox.width)
        this.hitbox.moveY(this.hitbox.cY - this.hitbox.height * 0.25f)
        this.letEachOrbToSlotPlaces()
        this.fillSide = MathUtils.randomBoolean()
    }

    fun changeScoreRate(scoreRate: Double)
    {
        this.scoreRate = scoreRate
    }

    fun evokeOrb(exampleSexMarkOrb: SexMarkOrb)
    {
        for (i in orbs.indices)
        {
            val orb = orbs[i]!! as? SexMarkOrb ?: continue
            if (orb.sexMarkName != exampleSexMarkOrb.sexMarkName) continue
            evokeOrbAndNotFill(i)
        }
    }

    private fun ScoreTheGangBang(): Int
    {
        return orbs.filterIsInstance<SexMarkOrb>()
            .map(SexMarkOrb::sexMarkName)
            .size
    }

    private fun MapIndex(index: Int): Int
    {
        val countTotal = this.GetMaxOrbs()
        if (countTotal % 2 == 1)
        {
            return MapIndexTool(index, countTotal)
        }
        else
        {
            val plusOneResult = MapIndexTool(index, countTotal + 1)
            return if (plusOneResult == countTotal) 0
            else plusOneResult
        }
    }

    private fun MapIndexTool(index: Int, countTotal: Int): Int
    {
        val mid = (countTotal - 1) / 2
        if (index % 2 == 1)
        {
            if (fillSide) return mid + (index + 1) / 2
            return mid - (index + 1) / 2
        }
        else
        {
            if (fillSide) return mid - index / 2
            return mid + index / 2
        }
    }

    protected fun makeSlotPlaceHeart(scale: Float, slotIndex: Int): Vector2
    {
        return makeHeartLine2(
            scale,
            OffsetPercentageBySlotIndex_Cycle(slotIndex.toFloat()) * 360 + (if ((this.GetMaxOrbs() % 2 == 0)) 0 else -144)
        )
    }

    protected fun makeHeartLine1(scale: Float, angle: Float): Vector2
    {
        return Vector2(1f, 0f)
            .setLength((1 - MathUtils.sinDeg(angle)) * scale)
            .setAngle(angle)
    }

    protected fun makeHeartLine2(scale: Float, angle: Float): Vector2
    {
        val vector2 = Vector2()
        val sinDeg = MathUtils.sinDeg(angle)
        vector2.x = 16 * sinDeg * sinDeg * sinDeg
        vector2.y =
            15 * MathUtils.cosDeg(angle) - 5 * MathUtils.cosDeg(2 * angle) - 2 * MathUtils.cosDeg(3 * angle) - MathUtils.cosDeg(
                4 * angle
            )
        vector2.x = vector2.x * (scale / 32)
        vector2.y = vector2.y * (scale / 32)
        return vector2
    }

    protected fun makeHeartLine3(scale: Float, angle: Float): Vector2
    {
        val vector2 = Vector2()
        val sinDeg = MathUtils.sinDeg(angle)
        vector2.x = 16 * sinDeg
        vector2.y = 15 * MathUtils.cosDeg(angle) - 6 * MathUtils.cosDeg(2 * angle) - 2 * MathUtils.cosDeg(3 * angle)
        vector2.x = vector2.x * (scale / 48)
        vector2.y = vector2.y * (scale / 48)
        return vector2
    }

    override fun findFirstEmptyOrb(): Int
    {
        var index = -1
        for (i in orbs.indices)
        {
            val o = orbs[MapIndex(i)]!!
            if (isEmptySlot(o))
            {
                index = MapIndex(i)
                break
            }
        }
        return index
    }

    override fun makeSlotPlace(slotIndex: Int): Vector2
    {
        return makeSlotPlaceHeart(hitbox.width, slotIndex)
    }

    override fun onOrbChannel(channeledOrb: AbstractOrb?)
    {
        if (this.hasEmptySlot()) return
        val attackAmount = AtomicInteger()
        val blockAmount = AtomicInteger()
        orbs.forEach { orb: AbstractOrb? ->
            if (isEmptySlot(orb)) return@forEach
            if (orb is SexMarkOrb)
            {
                val sexMarkOrb = orb
                attackAmount.addAndGet(sexMarkOrb.attack())
                blockAmount.addAndGet(sexMarkOrb.block())
            }
        }
        val gangBang = GangBang(attackAmount.get(), blockAmount.get(), ScoreTheGangBang(), scoreRate)
        AutoDoneInstantAction.addToBotAbstract {
            AbstractDungeon.actionManager.addCardQueueItem(
                CardQueueItem(
                    gangBang, null, 0, true,
                    true
                ), true
            )
        }
        AutoDoneInstantAction.addToBotAbstract {
            val bound = orbs.size
            for (i in 0 until bound)
            {
                this.evokeOrbAndNotFill(i)
            }
        }
    }

    override fun onOrbEvoke(evokedOrb: AbstractOrb?)
    {
    }

    sealed class SexMarkType
    {
        data object Inside : SexMarkType()
        data object OutSide : SexMarkType()
        companion object
        {
            fun values(): Array<SexMarkType>
            {
                return arrayOf(Inside, OutSide)
            }

            fun valueOf(value: String): SexMarkType
            {
                return when (value)
                {
                    "Inside"  -> Inside
                    "OutSide" -> OutSide
                    else      -> throw IllegalArgumentException("No object superstitio.orbs.orbgroup.SexMarkOrbGroup.SexMarkType.$value")
                }
            }
        }
    }

    companion object
    {
        const val SexMarkSetupOrbMax: Int = 5
        fun makeSexMarkOrb(sexMarkType: SexMarkType): SexMarkOrb
        {
            return when (sexMarkType)
            {
                SexMarkType.OutSide -> SexMarkOrb_Outside()
                SexMarkType.Inside  -> SexMarkOrb_Inside()
                else                -> SexMarkOrb_Inside()
            }
        }

        fun addToBot_GiveMarkToOrbGroup(sexName: String, sexMarkType: SexMarkType)
        {
            AbstractDungeon.actionManager.addToBottom(
                InBattleDataManager.getSexMarkOrbGroup()?.let {
                    GiveSexMarkToOrbGroupInstantAction(it, makeSexMarkOrb(sexMarkType).setSexMarkName(sexName))
                }
            )
        }
    }
}
