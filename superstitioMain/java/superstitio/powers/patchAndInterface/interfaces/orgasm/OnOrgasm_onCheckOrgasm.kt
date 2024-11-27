package superstitio.powers.patchAndInterface.interfaces.orgasm

import superstitio.powers.SexualHeat

interface OnOrgasm_onCheckOrgasm : OnOrgasm
{
    /**
     * 检测高潮时的钩子
     */
    override fun onCheckOrgasm(SexualHeatPower: SexualHeat)
}
