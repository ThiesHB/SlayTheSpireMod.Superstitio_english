package superstitioapi.powers.interfaces

import superstitioapi.powers.AllCardCostModifier

interface HasAllCardCostModifyEffect {
    val activeEffectHold: AllCardCostModifier?

    fun IDAsHolder(): String?
}
