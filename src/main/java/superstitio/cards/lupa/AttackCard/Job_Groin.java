package superstitio.cards.lupa.AttackCard;

import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.powers.SexualHeat;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.concurrent.atomic.AtomicInteger;

@AutoAdd.Ignore
public class Job_Groin extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Groin.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;

    private static final int Magic_Number = 3;

    private static final int Magic_Number_UPGRADE = 1;

    public Job_Groin() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.setupMagicNumber(Magic_Number);
    }

    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
        upgradeMagicNumber(Magic_Number_UPGRADE);
    }

    private int getOriginDamage() {
        if (this.upgraded)
            return ATTACK_DMG + UPGRADE_PLUS_DMG;
        else
            return ATTACK_DMG;
    }

    private void updateDamage() {
        if (!InBattleDataManager.InOrgasm) return;
        AtomicInteger damageUp = new AtomicInteger();
        SexualHeat.getActiveSexualHeat(AbstractDungeon.player).ifPresent(power -> damageUp.set(magicNumber * power.orgasmTime));
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
