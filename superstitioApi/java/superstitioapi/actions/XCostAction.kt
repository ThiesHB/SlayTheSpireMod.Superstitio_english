package superstitioapi.actions

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.ui.panels.EnergyPanel
import java.util.function.Consumer

class XCostAction(card: AbstractCard, actionType: ActionType?, private val actionMaker: Consumer<Int>) :
    AutoDoneInstantAction()
{
    private var freeToPlay: Boolean
    private var energyOnUse: Int
    private var player: AbstractPlayer = AbstractDungeon.player

    init
    {
        this.freeToPlay = card.freeToPlay()
        this.energyOnUse = card.energyOnUse
        this.actionType = actionType
        this.duration = Settings.ACTION_DUR_XFAST
    }

    fun setPlayer(player: AbstractPlayer): XCostAction
    {
        this.player = player
        return this
    }

    fun setEnergyOnUse(energyOnUse: Int): XCostAction
    {
        this.energyOnUse = energyOnUse
        return this
    }

    fun setFreeToPlay(freeToPlay: Boolean): XCostAction
    {
        this.freeToPlay = freeToPlay
        return this
    }

    override fun autoDoneUpdate()
    {
        var effect = EnergyPanel.totalCount
        if (this.energyOnUse != -1)
        {
            effect = this.energyOnUse
        }
        if (player.hasRelic("Chemical X"))
        {
            effect *= 2
            player.getRelic("Chemical X").flash()
        }
        if (effect > 0)
        {
            actionMaker.accept(effect)
            if (!this.freeToPlay)
            {
                player.energy.use(EnergyPanel.totalCount)
            }
        }
    }
}
