package SuperstitioMod.cards.Lupa.TempCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_TempCard;
import SuperstitioMod.utils.CardUtility;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.stream.IntStream;


public class SexToy extends AbstractLupaCard_TempCard {
    public static final String ID = DataManager.MakeTextID(SexToy.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;
    private static final int COST = 0;
    private static final int MagicNumber = 2;
    private static final int UPGRADE_MagicNumber = 1;

    private static final String[] sexToyNames = getCardStringsWithSFWAndFlavor(SexToy.ID).getEXTENDED_DESCRIPTION()[0].split(",");

    public SexToy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, SuperstitioModSetup.TempCardEnums.LUPA_TempCard_CARD);
        this.setupMagicNumber(MagicNumber);
    }

    public static String getRandomSexToyName() {
        return CardUtility.getRandomFromList(sexToyNames, AbstractDungeon.cardRandomRng);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null)
            target = AbstractDungeon.player;
        AbstractCreature finalTarget = target;
        IntStream.range(0, this.magicNumber).forEach(i ->
                addToBot_applyPower(new SuperstitioMod.powers.SexToy(finalTarget, 1, getRandomSexToyName())));
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
