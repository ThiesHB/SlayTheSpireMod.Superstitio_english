package superstitio.powers.barIndepend;

import com.megacrit.cardcrawl.powers.AbstractPower;

public interface HasBarRenderOnCreature_Power extends HasBarRenderOnCreature {

    AbstractPower getSelf();

    default int getAmountForDraw() {
        return getSelf().amount;
    }

    int maxBarAmount();

    default String getName() {
        return getSelf().name;
    }

    default String getDescription() {
        return getSelf().description;
    }
}
