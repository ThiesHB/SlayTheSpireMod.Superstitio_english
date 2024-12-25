package superstitioapi.hangUpCard

interface ICardOrb_CanEvokeOnEndOfTurn<T> where T : CardOrb
{
    var evokeOnEndOfTurn: Boolean

    @Suppress("UNCHECKED_CAST")
    val self: T
        get() = this as T

    fun setDiscardOnEndOfTurn(): T
    {
        this.evokeOnEndOfTurn = true
        this.setTriggerDiscardIfMoveToDiscard()
        return self
    }

    fun setTriggerDiscardIfMoveToDiscard(): CardOrb

    fun onEndOfTurn()
}