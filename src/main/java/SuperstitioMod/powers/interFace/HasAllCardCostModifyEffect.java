package SuperstitioMod.powers.interFace;

import SuperstitioMod.powers.AllCardCostModifier;

import java.util.Optional;

public interface HasAllCardCostModifyEffect {
    Optional<AllCardCostModifier> getActiveEffectHold();

    String IDAsHolder();
}
