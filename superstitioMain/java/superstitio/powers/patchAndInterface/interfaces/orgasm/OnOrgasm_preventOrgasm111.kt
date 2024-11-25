package superstitio.powers.patchAndInterface.interfaces.orgasm

import superstitio.powers.SexualHeat

interface OnOrgasm_preventOrgasm : OnOrgasm {
    /**
     * 调用时已经判断高潮成立，如果返回true则禁止本次高潮
     */
    override fun preventOrgasm(SexualHeatPower: SexualHeat): Boolean
}
