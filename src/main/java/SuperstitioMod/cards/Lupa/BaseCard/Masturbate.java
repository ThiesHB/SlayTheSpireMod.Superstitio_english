package SuperstitioMod.cards.Lupa.BaseCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.powers.SexualHeat;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Masturbate extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(Masturbate.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGICNumber = 3;
    private static final int UPGRADE_MagicNumber = 2;
    private static final int DRAWCard = 1;


    public Masturbate() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET,"base");
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.drawCards(DRAWCard);
        this.gainPowerToPlayer(new SexualHeat(player, this.magicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
