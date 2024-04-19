package SuperstitioMod.cards.Lupa.PowerCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.actions.AbstractAutoDoneAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class ChokeChoker extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(ChokeChoker.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGICNumber = 2;
    private static final int UPGRADE_MagicNumber = 1;


    public ChokeChoker() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        SuperstitioMod.powers.ChokeChoker power = new SuperstitioMod.powers.ChokeChoker(player, this.magicNumber);
        this.addToBot(new AbstractAutoDoneAction() {
            @Override
            public void autoDoneUpdate() {
                power.AddPowers();
            }
        });
        addToBot_applyPowerToPlayer(new SuperstitioMod.powers.ChokeChoker(player, this.magicNumber));
        //CumPlaceHelper.addToSequence(this);
        //gainPowerToPlayer(new SexualHeatNeededModifier(player, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
