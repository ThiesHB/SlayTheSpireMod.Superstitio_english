package superstitio.powers.interfaces.orgasm;

import superstitio.powers.SexualHeat;

public interface OnOrgasm_onCheck extends OnOrgasm {
    /**
     * 检测高潮时的钩子
     */
    @Override
    void onCheckOrgasm(SexualHeat SexualHeatPower);
}
