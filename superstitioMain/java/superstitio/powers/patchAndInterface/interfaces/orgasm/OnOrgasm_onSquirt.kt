package superstitio.powers.patchAndInterface.interfaces.orgasm

import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.powers.SexualHeat

interface OnOrgasm_onSquirt : OnOrgasm
{
    /**
     * 潮吹之前
     */
    override fun onSquirt(SexualHeatPower: SexualHeat, card: AbstractCard)
}
