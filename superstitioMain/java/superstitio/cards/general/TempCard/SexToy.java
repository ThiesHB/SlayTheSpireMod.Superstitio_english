package superstitio.cards.general.TempCard;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import superstitio.DataManager;
import superstitio.cards.general.AbstractTempCard;
import superstitioapi.utils.ListUtility;

import java.util.stream.IntStream;

import static superstitioapi.utils.CardUtility.getSelfOrEnemyTarget;


public class SexToy extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(SexToy.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.SPECIAL;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;
    public static final String[] sexToyNames = getCardStringsWithSFWAndFlavor(SexToy.ID).getEXTENDED_DESCRIPTION(0).split(",");
    private static final int COST = 0;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;

    public SexToy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    public static String getRandomSexToyName() {
        return ListUtility.getRandomFromList(sexToyNames, AbstractDungeon.cardRandomRng);
    }

    public static String getRandomSexToyNameWithoutRng() {
        Random random = new Random(System.nanoTime());
        return ListUtility.getRandomFromList(sexToyNames, random);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = getSelfOrEnemyTarget(this, monster);
        if (target == null)
            target = AbstractDungeon.player;
        AbstractCreature finalTarget = target;
        IntStream.range(0, this.magicNumber).forEach(i ->
                addToBot_applyPower(new superstitio.powers.SexToy(finalTarget, 1, getRandomSexToyName())));
    }

    @Override
    public void upgradeAuto() {
    }
}
