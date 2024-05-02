package SuperstitioMod.cards.Lupa.TempCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_TempCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.stream.IntStream;


public class SexToy extends AbstractLupaCard_TempCard {
    public static final String ID = DataManager.MakeTextID(SexToy.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;
    private static final int COST = 1;
    private static final int MagicNumber = 2;
    private static final int UPGRADE_MagicNumber = 1;

    private static final String[] sexToyNames = getCardStringsWithSFWAndFlavor(SexToy.ID).getEXTENDED_DESCRIPTION()[0].split(",");

    public SexToy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MagicNumber);
        this.setTarget_SelfOrEnemy();
    }

    public static String getRandomSexToyName() {
        return sexToyNames[AbstractDungeon.cardRandomRng.random(sexToyNames.length - 1)];
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int bound = this.magicNumber;
        if (this.isTargetSelf(monster)) {
            IntStream.range(0, bound).forEach(i ->
                    addToBot_applyPowerToPlayer(new SuperstitioMod.powers.SexToy(AbstractDungeon.player, 1, getRandomSexToyName())));
        }
        else {
            IntStream.range(0, bound).forEach(i ->
                    addToBot_applyPowerToEnemy(new SuperstitioMod.powers.SexToy(monster, 1, getRandomSexToyName()), monster));
        }
    }

//    @Override
//    public void triggerWhenDrawn() {
//        this.name = String.format(cardStrings.EXTENDED_DESCRIPTION[0], SexToyNames);
//        this.rawDescription = String.format(cardStrings.EXTENDED_DESCRIPTION[1], SexToyNames);
//        initializeDescription();
//    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MagicNumber);
    }
}
