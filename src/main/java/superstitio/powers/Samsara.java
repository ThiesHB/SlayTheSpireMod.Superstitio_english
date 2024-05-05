package superstitio.powers;

import superstitio.DataManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Samsara extends AbstractLupaPower {
    public static final String POWER_ID = DataManager.MakeTextID(Samsara.class.getSimpleName() );

    public Samsara(final AbstractCreature owner) {
        super(POWER_ID, owner, -1);
    }


    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        this.addToBot(new DrawCardAction(1));
    }

    @Override
    public void updateDescriptionArgs() {

    }
}
