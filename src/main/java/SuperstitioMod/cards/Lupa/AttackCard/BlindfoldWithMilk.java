package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BlindfoldWithMilk extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(BlindfoldWithMilk.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int ATTACK_DMG = 20;
    private static final int UPGRADE_PLUS_DMG = 4;

    public BlindfoldWithMilk() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (!monster.isDeadOrEscaped()) {
            {
                this.addToBot(new AbstractGameAction() {
                                  @Override
                                  public void update() {
                                      monster.rollMove();
                                      monster.createIntent();
                                      this.isDone = true;
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
