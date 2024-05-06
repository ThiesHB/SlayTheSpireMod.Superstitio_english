package superstitio.powers.barIndepend;

import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.function.Predicate;

public class BarRenderOnCreature_Power extends BarRenderOnCreature {
    public BarRenderOnCreature_Power(final HasBarRenderOnCreature_Power<? extends AbstractPower> owner, String uuid) {
        super(owner.getSelf().owner, owner, uuid);
    }

    @Override
    protected Predicate<HasBarRenderOnCreature> isActive() {
        return (owner) -> {
            if (owner instanceof AbstractPower) {
                AbstractPower power = (AbstractPower) owner;
                return power.amount != 0;
            }
            return false;
        };
    }

}