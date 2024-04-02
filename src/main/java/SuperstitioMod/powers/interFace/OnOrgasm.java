package SuperstitioMod.powers.interFace;

import SuperstitioMod.powers.SexualHeat;

public interface OnOrgasm {
    /**
     * 检测高潮时的钩子
     */
    public abstract void onCheckOrgasm(SexualHeat SexualHeatPower);

    /**
     * 高潮时的处理
     */
    public abstract void afterOrgasm(SexualHeat SexualHeatPower);

    /**
     * 调用时已经判断高潮成立，如果返回false则禁止本次高潮
     */
    public abstract boolean shouldOrgasm(SexualHeat SexualHeatPower);

    /**
     * 潮吹之前
     */
    public abstract void beforeSquirt(SexualHeat SexualHeatPower);
}
