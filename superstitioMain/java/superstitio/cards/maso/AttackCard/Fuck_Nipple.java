package superstitio.cards.maso.AttackCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.Milk;
import superstitioapi.utils.ActionUtility;


public class Fuck_Nipple extends MasoCard implements FuckJob_Card {
    public static final String ID = DataManager.MakeTextID(Fuck_Nipple.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int MAGIC = 2;
    private static final int DAMAGE_TIME = 2;

    public Fuck_Nipple() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (int i = 0; i < DAMAGE_TIME; i++) {
            AbstractMonster randomMonster = ActionUtility.getRandomMonsterSafe();
            addToBot_dealDamage(randomMonster);
            addToBot_applyPower(new Milk(randomMonster, this.magicNumber));
        }
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(DAMAGE_TIME);
    }

    @Override
    public void upgradeAuto() {
    }
}
