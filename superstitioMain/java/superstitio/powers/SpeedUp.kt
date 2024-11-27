package superstitio.powers

import com.megacrit.cardcrawl.actions.common.DrawCardAction
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitioapi.utils.setDescriptionArgs

//减半并且抽一张卡
class SpeedUp(owner: AbstractCreature, amount: Int) : AbstractSuperstitioPower(POWER_ID, owner, amount) {
    override fun updateDescriptionArgs() {
        this.setDescriptionArgs(amount, REMOVE_RATE)
    }

    override fun onPlayCard(card: AbstractCard, m: AbstractMonster?) {
        this.addToBot(DrawCardAction(amount))
        if (this.amount <= 1) this.addToTop_AutoRemoveOne(this)
        else if (this.amount % 2 == 0) this.addToTop_reducePowerToOwner(this.ID, amount / REMOVE_RATE)
        else this.addToTop_reducePowerToOwner(this.ID, (amount + 1) / REMOVE_RATE)
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(SpeedUp::class.java)
        const val REMOVE_RATE: Int = 2
    }
}
