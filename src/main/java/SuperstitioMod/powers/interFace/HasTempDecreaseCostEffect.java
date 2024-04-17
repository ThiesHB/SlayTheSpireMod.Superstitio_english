package SuperstitioMod.powers.interFace;

import SuperstitioMod.powers.TempDecreaseCost;

import java.util.Optional;

public interface HasTempDecreaseCostEffect {

    Optional<TempDecreaseCost> getActiveEffectHold();
    String IDAsHolder();

//    public abstract void TempCost_OnPlayCard(AbstractCard card, AbstractMonster m);
}
