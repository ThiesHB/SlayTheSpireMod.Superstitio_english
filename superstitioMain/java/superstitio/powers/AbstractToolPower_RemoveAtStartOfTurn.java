package superstitio.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitioapi.actions.AutoDoneInstantAction;

public abstract class AbstractToolPower_RemoveAtStartOfTurn extends AbstractSuperstitioPower implements NonStackablePower, InvisiblePower {
    public static final String POWER_ID = DataManager.MakeTextID(SexualHeat.class);

    public AbstractToolPower_RemoveAtStartOfTurn(final AbstractCreature owner) {
        super(POWER_ID, owner, -1);
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public void atStartOfTurn() {
        AutoDoneInstantAction.addToBotAbstract(this::atStartOfTurnAction);
        this.addToBot_removeSpecificPower(this);
    }

    protected abstract void atStartOfTurnAction();
}
