package superstitioapi.powers.interfaces;

import superstitioapi.powers.AllCardCostModifier;

import java.util.Optional;

public interface HasAllCardCostModifyEffect {
    Optional<AllCardCostModifier> getActiveEffectHold();

    String IDAsHolder();
}
