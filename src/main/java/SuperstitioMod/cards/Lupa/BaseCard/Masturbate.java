package SuperstitioMod.cards.Lupa.BaseCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.powers.SexualHeat;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Masturbate extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Masturbate.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGICNumber = 4;
    private static final int UPGRADE_MagicNumber = 2;
    private static final int DRAWCard = 1;


    public Masturbate() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_drawCards(DRAWCard);
        addToBot_applyPower(new SexualHeat(player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MagicNumber);
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
    }
}
