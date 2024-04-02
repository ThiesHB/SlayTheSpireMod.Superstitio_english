package SuperstitioMod.cards.Lupa.PowerCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;


public class Samsara extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(Samsara.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;

    private static final int MAGICNumber = 8;
    private static final int UPGRADE_MagicNumber = -2;

    public Samsara() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "default");
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainPowerToPlayer(new EquilibriumPower(player, 99));
        gainPowerToPlayer(new SuperstitioMod.powers.Samsara(player));
        this.addToBot(new MakeTempCardInDrawPileAction(new Wound(), this.magicNumber, true, true));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
