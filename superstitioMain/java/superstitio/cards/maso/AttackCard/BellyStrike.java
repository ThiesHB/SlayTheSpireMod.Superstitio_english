package superstitio.cards.maso.AttackCard;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.cards.maso.MasoCard;
import superstitioapi.actions.AutoDoneInstantAction;

import static com.evacipated.cardcrawl.mod.stslib.patches.CustomTargeting.targetingMap;


public class BellyStrike extends MasoCard {
    public static final String ID = DataManager.MakeTextID(BellyStrike.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 22;
    private static final int UPGRADE_DAMAGE = 8;

    private static final int MAGIC = 4;

    public BellyStrike() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.tags.add(CardTags.STRIKE);
        this.setupMagicNumber(MAGIC);
    }


    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null)
            target = AbstractDungeon.player;
        addToBot_applyPower(new LoseStrengthPower(target, this.magicNumber));
        if (target instanceof AbstractPlayer)
            addToBot_dealDamage(target, this.damage / 2, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        else
            addToBot_dealDamage(target, this.damage, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot_applyPower(new StrengthPower(target, this.magicNumber));
        if (!target.isPlayer) return;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            if (AbstractDungeon.player.lastDamageTaken > 0) {
                AbstractPower strength = AbstractDungeon.player.getPower(StrengthPower.POWER_ID);
                addToBot_applyPower(new StrengthPower(AbstractDungeon.player, strength.amount));
            }
        });
//        AbstractCreature finalTarget = target;
//        AutoDoneInstantAction.addToBotAbstract(() -> {
//            BlockModifierManager.blockInstances(AbstractDungeon.player)
//                    .forEach(blockInstance -> blockInstance.getBlockTypes().stream()
//                            .filter(blockModifier -> blockModifier instanceof PregnantBlock)
//                            .findFirst()
//                            .ifPresent(blockModifier -> AutoDoneInstantAction.addToBotAbstract(() -> {
//                                ((PregnantBlock) blockModifier).removeUnNaturally(
//                                        new DamageInfo(finalTarget, blockInstance.getBlockAmount()), 0);
//                                BlockModifierManager.removeSpecificBlockType(blockInstance);
//                            })));
//        });
    }

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player.hoveredCard == this) {
            calculateCardDamageForSelfOrEnemyTargeting();
        }
    }

    @Override
    public void unhover() {
        super.unhover();
        super.calculateCardDamage(null);
    }

    @Override
    public void calculateCardDamageForSelfOrEnemyTargeting() {
        if (!(targetingMap.get(this.target) instanceof SelfOrEnemyTargeting)) {
            super.calculateCardDamage(null);
            initializeDescription();
            return;
        }
        SelfOrEnemyTargeting selfOrEnemyTargeting = (SelfOrEnemyTargeting) targetingMap.get(this.target);
        updateSelfOrEnemyTargetingTargetHovered(AbstractDungeon.player, selfOrEnemyTargeting);
        AbstractCreature target = selfOrEnemyTargeting.getHovered();
        if (target instanceof AbstractMonster) {
            super.calculateCardDamage((AbstractMonster) target);
            initializeDescription();
            return;
        }

        if (target == null) {
            super.calculateCardDamage(null);
            initializeDescription();
            return;
        }
        this.applyPowersToBlock();
        final AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        if (!this.isMultiDamage) {
            calculateSingleDamage(player, target);
        } else {
            calculateMultipleDamage(player);
        }
        if (target instanceof AbstractPlayer) {
            this.damage /= 2;
            this.isDamageModified = true;
        }
        selfOrEnemyTargeting.clearHovered();
        initializeDescription();
    }

    @Override
    public void upgradeAuto() {
    }
}
