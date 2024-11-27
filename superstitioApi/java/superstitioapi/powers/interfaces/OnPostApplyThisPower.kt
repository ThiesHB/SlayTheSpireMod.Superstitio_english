package superstitioapi.powers.interfaces

import com.megacrit.cardcrawl.powers.AbstractPower

interface OnPostApplyThisPower<T : AbstractPower>
{
    /**
     * 真的很想把它设为私有，呃呃
     */
    fun InitializePostApplyThisPower(addedPower: T)

    fun tryInitializePostApplyThisPower(addedPower: AbstractPower)
    {
        (addedPower as? T)?.let(::InitializePostApplyThisPower)
    }
}