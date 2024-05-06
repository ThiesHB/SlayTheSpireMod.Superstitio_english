package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;

public class Fuck_Vaginal extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Fuck_Vaginal.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 14;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int MAGIC = 5;

    public Fuck_Vaginal() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }


    private int getOriginDamage() {
        if (this.upgraded)
            return DAMAGE + UPGRADE_DAMAGE;
        else
            return DAMAGE;
    }

    private void updateDamage() {
        int totalCost = AbstractDungeon.player.hand.group.stream()
                .filter(card -> card.costForTurn >= 1)
                .mapToInt(card -> card.costForTurn).sum();
        float damageDown = this.magicNumber;
        if (!AbstractDungeon.player.hand.group.isEmpty())
            damageDown = (float) totalCost * damageDown / AbstractDungeon.player.hand.group.size();
        if (damageDown >= 1)
            this.isDamageModified = true;
        this.baseDamage = (int) (this.getOriginDamage() - damageDown);

    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        this.calculateCardDamage(null);
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Inside(this.name);
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        this.calculateCardDamage(null);
    }

    @Override
    public void onMoveToDiscard() {
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(final AbstractMonster monster) {
        updateDamage();
        super.calculateCardDamage(monster);
        this.initializeDescription();
    }
}
