package superstitio.cards.lupa.PowerCard.defend;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.lupaOnly.BeerCupSemen;

//排出，然后喝
public class DrinkSemenBeer extends LupaCard {
    public static final String ID = DataManager.MakeTextID(DrinkSemenBeer.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = 5;

    public DrinkSemenBeer() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new BeerCupSemen(AbstractDungeon.player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

}

