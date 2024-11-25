package superstitioapi.powers.interfaces

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower

interface CopyAblePower {
    fun makeCopy(newOwner: AbstractCreature): AbstractPower?
}
