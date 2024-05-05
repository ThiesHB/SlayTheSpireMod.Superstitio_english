package superstitio.cards.lupa.PowerCard;

import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class DeathDoor extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(DeathDoor.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGICNumber = 1;
    private static final int UPGRADE_MagicNumber = 1;

    public DeathDoor() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new superstitio.powers.DeathDoor(AbstractDungeon.player,this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MagicNumber);
    }
}

