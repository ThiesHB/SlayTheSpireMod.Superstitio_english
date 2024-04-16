package SuperstitioMod.powers.interFace;

import SuperstitioMod.powers.TempDecreaseCost;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Optional;

public interface HasTempDecreaseCostEffect {

    public abstract Optional<TempDecreaseCost> getEffectHolder();
//    public abstract void TempCost_OnPlayCard(AbstractCard card, AbstractMonster m);
}
