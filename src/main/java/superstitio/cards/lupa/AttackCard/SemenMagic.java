package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;


public class SemenMagic extends LupaCard {
    public static final String ID = DataManager.MakeTextID(SemenMagic.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_DAMAGE = 6;

    private static final int MAGIC = 4;

    public SemenMagic() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (!hasEnoughSemen(this.magicNumber)) return;
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.LIGHTNING);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber);
    }

    @Override
    public void upgradeAuto() {
    }

}
