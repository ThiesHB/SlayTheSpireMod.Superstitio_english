package superstitio.cards.general.AttackCard;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.delayHpLose.DelayHpLosePower;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.cards.DamageActionMaker;

public class EroSion extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(EroSion.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int COST_UPGRADED_NEW = 0;
    private static final int DAMAGE = 0;
    private static final int MAGIC = 5;

    public EroSion() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE);
        this.setupMagicNumber(MAGIC);
    }


    private int getOriginDamage() {
        return DAMAGE;
    }

    private void updateDamage() {
        float damageUp = AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof DelayHpLosePower)
                .mapToInt(power -> power.amount).sum();
//        if (damageUp >= 1)
//            this.isDamageModified = true;
        this.baseDamage = (int) (this.getOriginDamage() + damageUp);
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW);
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber));
        AbstractCard self = this;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            calculateCardDamage(monster);
            addToBot_dealDamage(monster, self.damage, DamageActionMaker.DamageEffect.HeartMultiInOne);
        });
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        updateDamage();
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
