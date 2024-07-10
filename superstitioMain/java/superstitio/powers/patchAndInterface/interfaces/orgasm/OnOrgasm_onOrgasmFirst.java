package superstitio.powers.patchAndInterface.interfaces.orgasm;

import superstitio.powers.SexualHeat;

public interface OnOrgasm_onOrgasmFirst extends OnOrgasm {

    /**
     * 高潮时的优先处理
     */
    @Override
    default void onOrgasmFirst(SexualHeat SexualHeatPower) {
        OnOrgasm.super.onOrgasmFirst(SexualHeatPower);
    }
}
