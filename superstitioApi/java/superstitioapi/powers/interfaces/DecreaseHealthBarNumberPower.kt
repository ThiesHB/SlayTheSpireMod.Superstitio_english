package superstitioapi.powers.interfaces

interface DecreaseHealthBarNumberPower
{
    fun getDecreaseAmount(): Int
    fun setDecreaseAmount(value: Int)

    fun showDecreaseAmount(): Boolean
}
