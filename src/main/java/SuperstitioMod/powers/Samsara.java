package SuperstitioMod.powers;

import SuperstitioMod.SuperstitioModSetup;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Samsara extends AbstractLupaPower {
    public static final String POWER_ID = SuperstitioModSetup.MakeTextID(Samsara.class.getSimpleName() + "Power");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public Samsara(final AbstractCreature owner) {
        super(POWER_ID,powerStrings,owner,-1);
    }


    @Override
    public void updateDescription() {
        this.description = String.format(Samsara.powerStrings.DESCRIPTIONS[0]);
    }

    @Override
    public void onPlayCard(AbstractCard card, AbstractMonster m) {
        this.addToBot(new DrawCardAction(1));
    }
}
