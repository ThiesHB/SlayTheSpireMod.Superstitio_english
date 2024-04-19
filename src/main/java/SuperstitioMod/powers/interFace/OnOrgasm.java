package SuperstitioMod.powers.interFace;

import SuperstitioMod.powers.SexualHeat;

public interface OnOrgasm {
    /**
     * 检测高潮时的钩子
     */
    void onCheckOrgasm(SexualHeat SexualHeatPower);

    /**
     * 高潮时的处理
     */
    void afterOrgasm(SexualHeat SexualHeatPower);

    /**
     * 高潮结束后的处理
     */
    void afterEndOrgasm(SexualHeat SexualHeatPower);

    /**
     * 调用时已经判断高潮成立，如果返回true则禁止本次高潮
     */
    boolean preventOrgasm(SexualHeat SexualHeatPower);

    /**
     * 潮吹之前
     */
    void beforeSquirt(SexualHeat SexualHeatPower);
}
