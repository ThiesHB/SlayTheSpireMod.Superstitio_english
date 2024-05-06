package superstitio.cards.lupa.PowerCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.lupa.AbstractLupaCard;


public class ChokeChoker extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(ChokeChoker.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;


    public ChokeChoker() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        superstitio.powers.ChokeChoker power = new superstitio.powers.ChokeChoker(player, this.magicNumber);
        AutoDoneInstantAction.addToBotAbstract(power::AddPowers);
        addToBot_applyPower(new superstitio.powers.ChokeChoker(player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

}
