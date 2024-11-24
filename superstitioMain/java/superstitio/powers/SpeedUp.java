package superstitio.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;

//减半并且抽一张卡
public class SpeedUp extends AbstractSuperstitioPower {
    public static final String POWER_ID = DataManager.MakeTextID(SpeedUp.class);
    public static final int REMOVE_RATE = 2;

    public SpeedUp(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        this.setDescriptionArgs(amount, REMOVE_RATE);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        this.addToBot(new DrawCardAction(amount));
        if (this.amount <= 1)
            this.addToTop_AutoRemoveOne(this);
        else if (this.amount % 2 == 0)
            this.addToTop_reducePowerToOwner(this.ID, amount / REMOVE_RATE);
        else
            this.addToTop_reducePowerToOwner(this.ID, (amount + 1) / REMOVE_RATE);
    }
}
