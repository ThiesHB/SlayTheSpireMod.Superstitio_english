package superstitio.cards.general.PowerCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;


public class SelfBind extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(SelfBind.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;
    private static final int DECREASENum = 1;

    public SelfBind() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new StrengthPower(player, this.magicNumber));
        addToBot_applyPower(new DexterityPower(player, -DECREASENum));
    }

    @Override
    public void upgradeAuto() {
    }
}

