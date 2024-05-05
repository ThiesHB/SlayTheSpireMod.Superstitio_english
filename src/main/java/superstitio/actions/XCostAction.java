package superstitio.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.function.Consumer;

public class XCostAction extends AutoDoneInstantAction {
    private final Consumer<Integer> actionMaker;
    private boolean freeToPlayOnce;
    private int energyOnUse;
    private AbstractPlayer player = AbstractDungeon.player;

    public XCostAction(AbstractCard card, AbstractGameAction.ActionType actionType, final Consumer<Integer> actionMaker) {
        this.freeToPlayOnce = card.freeToPlayOnce;
        this.energyOnUse = card.energyOnUse;
        this.actionMaker = actionMaker;
        this.actionType = actionType;
        this.duration = Settings.ACTION_DUR_XFAST;
    }

    public XCostAction setPlayer(AbstractPlayer player) {
        this.player = player;
        return this;
    }

    public XCostAction setEnergyOnUse(int energyOnUse) {
        this.energyOnUse = energyOnUse;
        return this;
    }

    public XCostAction setFreeToPlayOnce(boolean freeToPlayOnce) {
        this.freeToPlayOnce = freeToPlayOnce;
        return this;
    }

    @Override
    public void autoDoneUpdate() {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (this.player.hasRelic("Chemical X")) {
            effect *= 2;
            this.player.getRelic("Chemical X").flash();
        }
        if (effect > 0) {
            actionMaker.accept(effect);
            if (!this.freeToPlayOnce) {
                this.player.energy.use(EnergyPanel.totalCount);
            }
        }
    }
}
