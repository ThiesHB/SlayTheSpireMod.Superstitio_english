package superstitio.cards.maso.AttackCard;

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cardModifier.modifiers.block.PregnantBlock;
import superstitio.cards.maso.MasoCard;


public class BellyStrike extends MasoCard {
    public static final String ID = DataManager.MakeTextID(BellyStrike.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 22;
    private static final int UPGRADE_DAMAGE = 8;

    private static final int MAGIC = 5;

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
        addToBot_dealDamage(target);
        addToBot_applyPower(new StrengthPower(target, this.magicNumber));
        if (!target.isPlayer) return;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            if (AbstractDungeon.player.lastDamageTaken > 0)
                this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, LoseStrengthPower.POWER_ID));
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
    public void upgradeAuto() {
    }
}
