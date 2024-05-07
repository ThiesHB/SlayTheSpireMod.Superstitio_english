package superstitio.cards.lupa.SkillCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.powers.BeerCupSemen;

//排出，然后喝
public class DrinkSemenBeer extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(DrinkSemenBeer.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = 5;

    public DrinkSemenBeer() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
//        AbstractDungeon.player.powers.stream().filter(power -> power instanceof InsideSemen).findAny()
//                .ifPresent(power -> {
//                    addToBot(new HealAction(AbstractDungeon.player, AbstractDungeon.player, power.amount));
//                    addToBot_reducePowerToPlayer(InsideSemen.POWER_ID, upgraded ? (power.amount / 2) : power.amount);
//                });
        addToBot_applyPower(new BeerCupSemen(AbstractDungeon.player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }
}

