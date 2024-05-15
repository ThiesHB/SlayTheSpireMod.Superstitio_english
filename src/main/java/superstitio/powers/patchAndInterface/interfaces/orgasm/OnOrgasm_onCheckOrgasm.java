package superstitio.powers.patchAndInterface.interfaces.orgasm;

import superstitio.powers.SexualHeat;

public interface OnOrgasm_onCheckOrgasm extends OnOrgasm {
    /**
     * 检测高潮时的钩子
     */
    @Override
    void onCheckOrgasm(SexualHeat SexualHeatPower);
}
