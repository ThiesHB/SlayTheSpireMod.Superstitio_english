package superstitio.powers.patchAndInterface.interfaces;

import superstitio.powers.AllCardCostModifier;

import java.util.Optional;

public interface HasAllCardCostModifyEffect {
    Optional<AllCardCostModifier> getActiveEffectHold();

    String IDAsHolder();
}
