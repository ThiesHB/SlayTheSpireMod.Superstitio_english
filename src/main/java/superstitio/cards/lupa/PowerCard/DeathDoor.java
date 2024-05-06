package superstitio.cards.lupa.PowerCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;


public class DeathDoor extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(DeathDoor.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public DeathDoor() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new superstitio.powers.DeathDoor(AbstractDungeon.player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }
}

