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
     * 调用时已经判断高潮成立，如果返回false则禁止本次高潮
     */
    boolean shouldOrgasm(SexualHeat SexualHeatPower);

    /**
     * 潮吹之前
     */
    void beforeSquirt(SexualHeat SexualHeatPower);
}
