package SuperstitioMod.cards.Lupa.PowerCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;


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
        this.addToBot_applyPowerToPlayer(new SuperstitioMod.powers.DeathDoor(AbstractDungeon.player,this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MagicNumber);
    }
}

