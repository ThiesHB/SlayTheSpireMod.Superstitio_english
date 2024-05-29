package superstitio.powers.patchAndInterface.interfaces;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;

public interface CopyAblePower {
    AbstractPower makeCopy(AbstractCreature newOwner);
}
