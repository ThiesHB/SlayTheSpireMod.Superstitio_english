package superstitio.cards.lupa

import com.megacrit.cardcrawl.powers.AbstractPower

interface OnAddSemenPower
{
    fun onAddSemen_shouldApply(power: AbstractPower?): Boolean
}
