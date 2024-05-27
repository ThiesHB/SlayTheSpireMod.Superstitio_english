package superstitio.delayHpLose;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.powers.patchAndInterface.interfaces.OnPostApplyThisPower;
import superstitio.utils.ActionUtility;

public abstract class DelayHpLosePower_ApplyAtEndOfRound extends DelayHpLosePower implements OnPostApplyThisPower {
    private static final Color ReadyToRemoveColor = new Color(1.0F, 0.5F, 0.0F, 1.0F);
    private static final Color ForAWhileColor = new Color(0.9412F, 0.4627f, 0.5451f, 1.0f);
    private static final Color OriginColor = new Color(1.0F, 0.85f, 0.90f, 1.0f);
    private static final int TURN_INIT = 1;
    private static final int TURN_READY = 0;
    private final String OriginId;

    private int Turn;
    private boolean atEnemyTurn;

    public DelayHpLosePower_ApplyAtEndOfRound(String originId, final AbstractCreature owner, int amount) {
        super(originId, owner, amount);
        this.OriginId = originId;
        this.Turn = TURN_INIT;
        this.updateUniqueID();
    }

    @Override
    protected int addToBot_removeDelayHpLoss(final int amount, boolean removeOther) {
        if (!removeOther) return addToBot_removeEachTurnPower(amount, TURN_READY);
        int lastAmount = amount;
        int maxTurn = owner.powers.stream()
                .filter(power -> power instanceof DelayHpLosePower_ApplyAtEndOfRound)
                .mapToInt(power -> ((DelayHpLosePower_ApplyAtEndOfRound) power).Turn).max().orElse(0);
        for (int i = TURN_READY; i < maxTurn + 1; i++) {
            lastAmount = addToBot_removeEachTurnPower(lastAmount, i);
            if (lastAmount <= 0) break;
        }
        return lastAmount;
    }

    private int addToBot_removeEachTurnPower(int amount, int turnShouldRemove) {
        AbstractPower targetPower = owner.powers.stream()
                .filter(power -> power instanceof DelayHpLosePower_ApplyAtEndOfRound
                        && ((DelayHpLosePower_ApplyAtEndOfRound) power).Turn == turnShouldRemove)
                .findAny().orElse(null);
        if (targetPower == null) return amount;
        ActionUtility.addToBot_reducePower(targetPower.ID, amount, this.owner, this.owner);
        return Math.max(amount - targetPower.amount, 0);
    }

    public void updateUniqueID() {
        this.ID = this.OriginId + this.Turn;
    }

    @Override
    public boolean checkShouldInvisibleTips() {
        return this.Turn > 0;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount, findAll(this.owner, DelayHpLosePower_ApplyAtEndOfRound.class).mapToInt(power -> power.amount).sum());
    }

    //    @Override
//    public void atStartOfTurn() {
//        if (Turn <= 0) {
//            addToBot_applyDamage();
//        }
//        Turn--;
//        atEnemyTurn = false;
//    }

    @Override
    public void InitializePostApplyThisPower(AbstractPower addedPower) {
        AutoDoneInstantAction.addToBotAbstract(() ->
                findAll(this.owner, DelayHpLosePower.class).forEach(DelayHpLosePower::updateDescription));
    }

    @Override
    public void atEndOfRound() {
        if (Turn <= 0) {
            addToBot_applyDamage();
            AutoDoneInstantAction.addToBotAbstract(() ->
                    findAll(this.owner, DelayHpLosePower.class).forEach(DelayHpLosePower::updateDescription));
        }
        Turn--;
        atEnemyTurn = false;
        this.updateDescription();
    }

    @Override
    public boolean showDecreaseAmount() {
        return this.Turn <= 0;
    }


    @Override
    public void updateDescription() {
        this.updateUniqueID();
        super.updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        atEnemyTurn = true;
    }

    @Override
    public Color getColor() {
        if (Turn <= 0)
            return atEnemyTurn ? ReadyToRemoveColor : ForAWhileColor;
        return OriginColor;
    }
}
