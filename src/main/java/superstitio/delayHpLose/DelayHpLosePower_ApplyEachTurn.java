package superstitio.delayHpLose;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.utils.ActionUtility;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

public class DelayHpLosePower_ApplyEachTurn extends DelayHpLosePower {
    private static final String POWER_ID = DataManager.MakeTextID(DelayHpLosePower_ApplyEachTurn.class);
    private static final Color ReadyToRemoveColor = new Color(1.0F, 0.5F, 0.0F, 1.0F);
    private static final Color ForAWhileColor = new Color(0.9412F, 0.4627f, 0.5451f, 1.0f);
    private static final Color OriginColor = new Color(1.0F, 0.85f, 0.90f, 1.0f);
    private static final int TURN_INIT = 1;
    private static final int TURN_READY = 0;

    private int Turn;
    private boolean atEnemyTurn;

    public DelayHpLosePower_ApplyEachTurn(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
        this.Turn = TURN_INIT;
        this.ID = getUniqueID();
    }

    public static void addToBot_removePower(int amount, AbstractCreature target, AbstractCreature source, boolean removeOther) {
        if (amount <= 0) return;
        DelayHpLosePower_ApplyEachTurn.addToBot_removePower(amount, target, source, TURN_READY, removeOther);
    }

    public static Stream<DelayHpLosePower_ApplyEachTurn> findAll(AbstractCreature target) {
        return target.powers.stream().filter(power -> power instanceof DelayHpLosePower_ApplyEachTurn).map(power -> (DelayHpLosePower_ApplyEachTurn) power);
    }

    private static void addToBot_removePower(int amount, AbstractCreature target, AbstractCreature source, int turn, boolean removeOther) {
        Optional<AbstractPower> targetPower = target.powers.stream().filter(power -> Objects.equals(power.ID, getUniqueID(turn))).findAny();
        if (!targetPower.isPresent()) return;
        ActionUtility.addToBot_reducePower(targetPower.get().ID, amount, target, source);
        int lastAmount = amount - targetPower.get().amount;
        if (lastAmount <= 0)
            return;
        if (removeOther) {
            addToBot_removePower(amount, target, source, turn + 1, removeOther);
        }
    }

    private static String getUniqueID(int turn) {
        return POWER_ID + turn;
    }

    public String getUniqueID() {
        return POWER_ID + this.Turn;
    }

    @Override
    public boolean checkShouldInvisibleTips() {
        return this.Turn > 0;
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
    public void atEndOfRound() {
        if (Turn <= 0) {
            addToBot_applyDamage();
        }
        Turn--;
        atEnemyTurn = false;
        this.updateDescription();
    }

    @Override
    public void updateDescription() {
        this.ID = getUniqueID();
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
