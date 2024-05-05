package superstitio.cards.lupa.AttackCard;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.lupa.AbstractLupaCard;


public class BellyStrike extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(BellyStrike.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 12;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int MAGIC_Number = 2;

    public BellyStrike() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.tags.add(CardTags.STRIKE);
        this.setupMagicNumber(MAGIC_Number);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null)
            target = AbstractDungeon.player;
        addToBot_applyPower(new LoseStrengthPower(target, magicNumber));
        addToBot_dealDamage(target);
        addToBot_applyPower(new StrengthPower(target, magicNumber));
        if (!target.isPlayer) return;
        AutoDoneInstantAction.addToBotAbstract(() -> {
            if (AbstractDungeon.player.lastDamageTaken > 0)
                this.addToBot(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, LoseStrengthPower.POWER_ID));
        });
    }

    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
    }
}
