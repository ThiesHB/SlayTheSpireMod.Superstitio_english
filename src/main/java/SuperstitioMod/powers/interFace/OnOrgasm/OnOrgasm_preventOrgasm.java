package SuperstitioMod.powers.interFace.OnOrgasm;

import SuperstitioMod.powers.SexualHeat;

public interface OnOrgasm_preventOrgasm extends OnOrgasm {
    /**
     * 调用时已经判断高潮成立，如果返回true则禁止本次高潮
     */
    @Override
    boolean preventOrgasm(SexualHeat SexualHeatPower);
}
