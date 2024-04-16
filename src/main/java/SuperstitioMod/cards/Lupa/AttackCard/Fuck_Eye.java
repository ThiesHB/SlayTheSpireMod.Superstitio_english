package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.utils.CardUtility;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public class Fuck_Eye extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(Fuck_Eye.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 4;
    private static final int UPGRADE_PLUS_DMG = 2;
    private static final int BLOCK = 3;
    private static final int UPGRADE_BLOCK = 1;

    public Fuck_Eye() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.setupBlock(BLOCK);
        AbstractCard card = new Fuck_Ear(false);
        CardModifierManager.addModifier(card, new RetainMod());
        this.cardsToPreview = card;
        this.exhaust = true;
    }

    public Fuck_Eye(boolean blank) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        gainBlock();
        AbstractCard card = new Fuck_Ear();
        CardModifierManager.addModifier(card, new RetainMod());
        if (upgraded)
            card.upgrade();
        makeTempCardInBattle(card, BattleCardPlace.Hand);
        gainPowerToPlayer(new WeakPower(AbstractDungeon.player, 1, false));
        CardUtility.gainSexMark_Inside(this.name);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeBlock(UPGRADE_BLOCK);
            if (cardsToPreview != null)
                this.cardsToPreview.upgrade();
        }
    }
}
