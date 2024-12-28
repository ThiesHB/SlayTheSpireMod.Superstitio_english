package superstitioapi.utils

import kotlin.math.max

/**
 * 当为Int类型时，不能低于0
 */
open class CostSmart @JvmOverloads constructor(
    protected var costType: CostType,
    cost: Int,
    private val whenCostChangeAction: (CostSmart) -> Unit = {}
)
{
    @JvmOverloads
    constructor(cost: Int, whenCostChangeAction: (CostSmart) -> Unit = {}) : this(
        costType = when (cost)
        {
            -2   -> CostType.NaN
            -1   -> CostType.XCost
            else -> CostType.Int
        },
        cost = when (cost != -2 && cost != -1 && cost < 0)
        {
            true -> 0
            else -> cost
        },
        whenCostChangeAction = whenCostChangeAction
    )

    @JvmOverloads
    constructor(costType: CostType, whenCostChangeAction: (CostSmart) -> Unit = {}) : this(
        costType, when (costType)
        {
            CostType.XCost -> -1
            CostType.NaN   -> -2
            else           -> throw IllegalArgumentException("Unsupported cost type: $costType")
        },
        whenCostChangeAction = whenCostChangeAction
    )

    @JvmOverloads
    constructor(costSmart: CostSmart, whenCostChangeAction: (CostSmart) -> Unit = {}) : this(
        costSmart.costType, costSmart.cost,
        whenCostChangeAction = whenCostChangeAction
    )

    companion object
    {
        fun makeZero(): CostSmart
        {
            return CostSmart(0)
        }
    }

    private var _costValue: Int = cost

    protected open var cost: Int
        get() = _costValue
        protected set(value)
        {
            if (_costValue == value)
                return
            _costValue = when (costType)
            {
                CostType.Int   -> max(0, value)
                CostType.XCost -> -1
                CostType.NaN   -> -2
            }
            whenCostChangeAction(this)
        }

    sealed class CostType
    {
        data object NaN : CostType()
        data object Int : CostType()
        data object XCost : CostType()
        companion object
        {
            fun values(): Array<CostType>
            {
                return arrayOf(NaN, Int, XCost)
            }

            fun valueOf(value: String): CostType
            {
                return when (value)
                {
                    "NaN"   -> NaN
                    "Int"   -> Int
                    "XCost" -> XCost
                    else    -> throw IllegalArgumentException("No object superstitioapi.utils.CardUtility.CostSmart.CostType.$value")
                }
            }
        }
    }

    fun isInt(): Boolean
    {
        return costType == CostType.Int
    }

    fun toInt(): Int
    {
        return this.toInt { it }
    }

    /***
     * 不改变实际值，只是把输出转换了一下
     */
    fun toInt(transform: (Int) -> Int): Int
    {
        val constCost = this.cost
        if (costType != CostType.Int)
            return constCost
        return transform(constCost)
    }

    fun toNewCostSmart(transform: (Int) -> Int): CostSmart
    {
        if (costType != CostType.Int)
            return this.makeCopy()
        val constCost = this.cost
        return CostSmart(this.costType, transform(constCost))
    }

    fun changeCost(transform: (Int) -> Int)
    {
        if (costType != CostType.Int)
            return
        val constCost = this.cost
        this.cost = transform(constCost)
    }

    private fun makeCopy(): CostSmart
    {
        return CostSmart(this.costType, this.cost)
    }


    override fun toString(): String
    {
        return cost.toString()
    }

    //        operator fun compareTo(int: Int): Int
//        {
//            return cost.compareTo(int)
//        }
    /**
     * 检测是否是CostType.Int意义上的Zero
     */
    fun isZero(): Boolean
    {
        return if (this.costType != CostType.Int)
            false
        else
            this.equals(0)
    }


    override fun equals(other: Any?): Boolean
    {
        if (other == null) return false
        if (other is Int)
            return this.cost == other
        if (other is CostType)
            return this.costType == other
        if (other is CostSmart)
            return this === other
        return false
    }

    override fun hashCode(): Int
    {
        var result = costType.hashCode()
        result = 31 * result + _costValue
        return result
    }

    operator fun inc(): CostSmart
    {
        if (this.costType == CostType.Int)
            this.cost += 1
        return this
    }

    operator fun dec(): CostSmart
    {
        if (this.costType == CostType.Int)
            this.cost -= 1
        return this
    }

    operator fun timesAssign(timesNumber: Int)
    {
        if (this.costType == CostType.Int)
            this.cost *= timesNumber
    }

    operator fun plusAssign(plusNumber: Int)
    {
        if (this.costType == CostType.Int)
            this.cost += plusNumber
    }

    operator fun minusAssign(minusNumber: Int)
    {
        if (this.costType == CostType.Int)
            this.cost -= minusNumber
    }
}