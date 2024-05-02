package SuperstitioMod.powers.interFace.OnOrgasm;

import SuperstitioMod.powers.SexualHeat;

public interface OnOrgasm_onCheck extends OnOrgasm {
    /**
     * 检测高潮时的钩子
     */
    @Override
    void onCheckOrgasm(SexualHeat SexualHeatPower);
}
