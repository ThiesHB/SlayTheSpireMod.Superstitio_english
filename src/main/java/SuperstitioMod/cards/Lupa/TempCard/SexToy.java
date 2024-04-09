package SuperstitioMod.cards.Lupa.TempCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class SexToy extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(SexToy.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int MagicNumber = 2;
    private static final int UPGRADE_MagicNumber = 1;

    private static final String[] sexToyNames = getCardStringsWithFlavor(SexToy.ID).EXTENDED_DESCRIPTION[0].split(",");

    public SexToy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, CardColor.COLORLESS);
        this.setupMagicNumber(MagicNumber);
    }

    public static String getRandomSexToyName() {
        return sexToyNames[AbstractDungeon.cardRandomRng.random(sexToyNames.length - 1)];
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (int i = 0; i < this.magicNumber; i++) {
            gainPowerToPlayer(new SuperstitioMod.powers.SexToy(player, 1, getRandomSexToyName()));
        }
    }

//    @Override
//    public void triggerWhenDrawn() {
//        this.name = String.format(cardStrings.EXTENDED_DESCRIPTION[0], SexToyNames);
//        this.rawDescription = String.format(cardStrings.EXTENDED_DESCRIPTION[1], SexToyNames);
//        initializeDescription();
//    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
