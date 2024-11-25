package superstitio.delayHpLose

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.powers.AbstractPower
import superstitioapi.actions.AutoDoneInstantAction
import superstitioapi.utils.ActionUtility
import superstitioapi.utils.setDescriptionArgs
import java.util.function.Consumer
import java.util.function.ToIntFunction
import kotlin.math.max

abstract class DelayHpLosePower_ApplyAtEndOfRound(private val OriginId: String, owner: AbstractCreature, amount: Int) :
    DelayHpLosePower(
        OriginId, owner, amount
    ) {
    private var Turn: Int
    private var atEnemyTurn = false

    init {
        this.Turn = TURN_INIT
        this.updateUniqueID()
    }

    fun updateUniqueID() {
        this.ID = this.OriginId + this.Turn
    }

    private fun addToBot_removeEachTurnPower(amount: Int, turnShouldRemove: Int): Int {
        val targetPower = owner.powers.stream()
            .filter { power: AbstractPower? ->
                (power is DelayHpLosePower_ApplyAtEndOfRound
                        && power.Turn == turnShouldRemove)
            }
            .findAny().orElse(null)
        if (targetPower == null) return amount
        ActionUtility.addToBot_reducePower(targetPower.ID, amount, this.owner, this.owner)
        return max((amount - targetPower.amount).toDouble(), 0.0).toInt()
    }

    override fun checkShouldInvisibleTips(): Boolean {
        return this.Turn > 0
    }

    override fun updateDescriptionArgs() {
        setDescriptionArgs(
            this.amount,
            findAll(
                this.owner,
                DelayHpLosePower_ApplyAtEndOfRound::class.java
            ).mapToInt(
                ToIntFunction(DelayHpLosePower_ApplyAtEndOfRound::amount)
            )
                .sum()
        )
    }

    override fun InitializePostApplyThisPower(addedPower: DelayHpLosePower) {
        AutoDoneInstantAction.addToBotAbstract {
            findAll(this.owner, DelayHpLosePower::class.java).forEach(
                Consumer(DelayHpLosePower::updateDescription)
            )
        }
    }

    //    @Override
    //    public void atStartOfTurn() {
    //        if (Turn <= 0) {
    //            addToBot_applyDamage();
    //        }
    //        Turn--;
    //        atEnemyTurn = false;
    //    }
    override fun atEndOfRound() {
        if (Turn <= 0) {
            addToBot_applyDamage()
            AutoDoneInstantAction.addToBotAbstract {
                findAll(this.owner, DelayHpLosePower::class.java).forEach(
                    Consumer(DelayHpLosePower::updateDescription)
                )
            }
        }
        Turn--
        atEnemyTurn = false
        this.updateDescription()
    }

    override fun showDecreaseAmount(): Boolean {
        return this.Turn <= 0
    }

    override fun updateDescription() {
        this.updateUniqueID()
        super.updateDescription()
    }

    override fun atEndOfTurn(isPlayer: Boolean) {
        atEnemyTurn = true
    }

    override fun getColor(): Color {
        if (Turn <= 0) return if (atEnemyTurn) ReadyToRemoveColor else ForAWhileColor
        return OriginColor
    }

    override fun addToBot_removeDelayHpLoss(amount: Int, removeOther: Boolean): Int {
        if (!removeOther) return addToBot_removeEachTurnPower(amount, TURN_READY)
        var lastAmount = amount
        val maxTurn = owner.powers.stream()
            .filter { power: AbstractPower? -> power is DelayHpLosePower_ApplyAtEndOfRound }
            .mapToInt { power: AbstractPower -> (power as DelayHpLosePower_ApplyAtEndOfRound).Turn }.max().orElse(0)
        for (i in TURN_READY until maxTurn + 1) {
            lastAmount = addToBot_removeEachTurnPower(lastAmount, i)
            if (lastAmount <= 0) break
        }
        return lastAmount
    }

    companion object {
        private val ReadyToRemoveColor = Color(1.0f, 0.5f, 0.0f, 1.0f)
        private val ForAWhileColor = Color(0.9412f, 0.4627f, 0.5451f, 1.0f)
        private val OriginColor = Color(1.0f, 0.85f, 0.90f, 1.0f)
        private const val TURN_INIT = 1
        private const val TURN_READY = 0
    }
}
