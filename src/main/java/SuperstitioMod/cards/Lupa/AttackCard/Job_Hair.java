package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.utils.CardUtility;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Job_Hair extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(Job_Hair.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 9;
    private static final int UPGRADE_PLUS_DMG = 4;

    public Job_Hair() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.isEthereal = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractCard card = new Job_Hair();
        CardModifierManager.addModifier(card, new ExhaustMod());
        this.cardsToPreview = card;
        if (upgraded)
            card.upgrade();
        makeTempCardInBattle(card, BattleCardPlace.Hand);
        CardUtility.addToTop_gainSexMark_Outside(this.cardStrings.EXTENDED_DESCRIPTION[0]);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
        }
    }
}
