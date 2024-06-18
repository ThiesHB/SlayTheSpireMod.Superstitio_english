package superstitioapi.powers.interfaces;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface OnPostApplyThisPower<T extends AbstractPower> {
    /**
     * 真的很想把它设为私有，呃呃
     */
    void InitializePostApplyThisPower(T addedPower);

    /**
     * 这玩意是类型安全的，随便用就行了
     */
    default <TCheck> void tryInitializePostApplyThisPower(TCheck addedPower, Class<T> thisClass) {
        if (thisClass.isInstance(addedPower))
            InitializePostApplyThisPower((T) addedPower);
    }
}
