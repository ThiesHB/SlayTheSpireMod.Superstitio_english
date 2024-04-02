package SuperstitioMod.cards.Lupa.PowerCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;


public class SelfBind extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(SelfBind.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGICNumber = 2;
    private static final int UPGRADE_MagicNumber = 2;
    private static final int DECREASENum = 1;

    public SelfBind() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "default");
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainPowerToPlayer(new StrengthPower(player, this.magicNumber));
        gainPowerToPlayer(new DexterityPower(player, -DECREASENum));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
