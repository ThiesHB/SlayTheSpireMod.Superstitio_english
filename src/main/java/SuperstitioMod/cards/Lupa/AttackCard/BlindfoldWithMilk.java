package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.actions.AbstractAutoDoneAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlindfoldWithMilk extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(BlindfoldWithMilk.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int ATTACK_DMG = 20;
    private static final int UPGRADE_PLUS_DMG = 8;

    public BlindfoldWithMilk() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (!monster.isDeadOrEscaped()) {
            {
                this.addToBot(new AbstractAutoDoneAction() {
                                  @Override
                                  public void autoDoneUpdate() {
                                      monster.rollMove();
                                      monster.createIntent();
                                  }
                              }
                );
            }
        }
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
