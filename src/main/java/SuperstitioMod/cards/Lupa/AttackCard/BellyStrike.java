package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.LoseStrengthPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

//TODO
public class BellyStrike extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(BellyStrike.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 12;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int MAGIC_Number = 2;

    public BellyStrike() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.tags.add(CardTags.STRIKE);
        this.setTarget_SelfOrEnemy();
        this.setupMagicNumber(MAGIC_Number);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        if (this.isTargetSelf(monster)) {
            addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(AbstractDungeon.player, this.damage),
                    AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            addToBot_gainPowerToPlayer(new StrengthPower(monster, magicNumber));
        } else {
            addToBot_damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            addToBot_applyPowerToEnemy(new StrengthPower(monster, magicNumber), monster);
            addToBot_applyPowerToEnemy(new LoseStrengthPower(monster, magicNumber), monster);
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
