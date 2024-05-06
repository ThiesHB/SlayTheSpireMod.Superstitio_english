package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.lupa.AbstractLupaCard;

public class BlindfoldWithMilk extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(BlindfoldWithMilk.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 20;
    private static final int UPGRADE_DAMAGE = 8;

    public BlindfoldWithMilk() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (monster.isDeadOrEscaped()) return;
        AutoDoneInstantAction.addToBotAbstract(() -> {
                    monster.rollMove();
                    monster.createIntent();
                }
        );
    }

    @Override
    public void upgradeAuto() {
    }
}
