package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CoitalVocal extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(CoitalVocal.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 9;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int Magic_Number = 3;

    public CoitalVocal() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.setupMagicNumber(Magic_Number);
        this.retain = true;
    }

    private int getOriginDamage() {
        if (this.upgraded)
            return ATTACK_DMG + UPGRADE_PLUS_DMG;
        else
            return ATTACK_DMG;
    }

//    @Override
//    public void use(AbstractPlayer player, AbstractMonster monster) {
//
//        damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
//    }

//    @Override
//    public void update() {
//        super.update();
//
//    }

    private void updateDamage() {
        int totalCost = AbstractDungeon.player.hand.group.stream()
                .filter(card -> card.costForTurn >= 1)
                .mapToInt(card -> card.costForTurn).sum();
        float damageDown = this.magicNumber;
        if (!AbstractDungeon.player.hand.group.isEmpty())
            damageDown = (float) totalCost * damageDown / AbstractDungeon.player.hand.group.size();
        if (damageDown * this.magicNumber >= 1)
            this.isDamageModified = true;
        this.baseDamage = (int) (this.getOriginDamage() - damageDown);

    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        this.calculateCardDamage(null);
        damageToAllEnemies(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
    }

    @Override
    public void applyPowers() {
//        int frostCount = 0;
//        for (final AbstractOrb o : AbstractDungeon.actionManager.orbsChanneledThisCombat) {
//            if (o instanceof Frost) {
//                ++frostCount;
//            }
//        }
//        if (frostCount > 0) {
//            this.baseDamage = frostCount * this.magicNumber;
//        updateDamage();
        super.applyPowers();
//            this.rawDescription = this.cardStrings.DESCRIPTION + this.cardStrings.EXTENDED_DESCRIPTION[0];
        this.calculateCardDamage(null);
    }

    @Override
    public void onMoveToDiscard() {
//        this.rawDescription = this.cardStrings.DESCRIPTION;
        this.initializeDescription();
    }

    @Override
    public void calculateCardDamage(final AbstractMonster monster) {
        updateDamage();
        super.calculateCardDamage(monster);
        this.initializeDescription();
    }
}
