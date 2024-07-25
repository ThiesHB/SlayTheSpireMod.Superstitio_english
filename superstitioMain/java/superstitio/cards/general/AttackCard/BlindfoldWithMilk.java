package superstitio.cards.general.AttackCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.Milk;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.cards.DamageActionMaker;

public class BlindfoldWithMilk extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(BlindfoldWithMilk.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_DAMAGE = 8;
    private static final int MAGIC = 6;

    public BlindfoldWithMilk() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, DamageActionMaker.DamageEffect.HeartMultiInOne);
        if (monster.isDeadOrEscaped()) return;
        AutoDoneInstantAction.addToBotAbstract(() -> {
                    monster.rollMove();
                    monster.createIntent();
                }
        );
        addToBot_applyPower(new Milk(monster, magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }
}
