package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.powers.SexualHeat;

import java.util.concurrent.atomic.AtomicInteger;

public class Job_Groin extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Groin.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 16;
    private static final int UPGRADE_DAMAGE = 5;

    private static final int MAGIC = 2;

    private static final int UPGRADE_MAGIC = 1;

    public Job_Groin() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void upgradeAuto() {
    }

    private int getOriginDamage() {
        if (this.upgraded)
            return DAMAGE + UPGRADE_DAMAGE;
        else
            return DAMAGE;
    }

    private void updateDamage() {
        if (!InBattleDataManager.InOrgasm) return;
        AtomicInteger damageUp = new AtomicInteger();
        SexualHeat.getActiveSexualHeat(AbstractDungeon.player).ifPresent(power -> damageUp.set(this.magicNumber * InBattleDataManager.OrgasmTimesTotal));
        if (damageUp.get() >= 1)
            this.isDamageModified = true;
        this.baseDamage = this.getOriginDamage() + damageUp.get();

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
