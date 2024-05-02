package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_FuckJob;
import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

public class Fuck_Ear extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Fuck_Ear.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int ATTACK_DMG = 2;
    private static final int UPGRADE_PLUS_DMG = 1;

    public Fuck_Ear() {
        this(false);
//        AbstractCard card = new Fuck_Eye(false);
//        CardModifierManager.removeSpecificModifier(card, new RetainMod(), false);
        this.cardsToPreview = new Fuck_Eye(false);
    }

    public Fuck_Ear(boolean blank) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.exhaust = true;
//        CardModifierManager.addModifier(this, new RetainMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractCard card = new Fuck_Eye();
//        CardModifierManager.removeSpecificModifier(card, new RetainMod(), false);
        if (upgraded)
            card.upgrade();
        addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand);
        addToBot_applyPowerToPlayer(new FrailPower(AbstractDungeon.player, 1, false));
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Inside(this.name);
    }

    @Override
    public void triggerWhenDrawn() {
        if (!CardModifierManager.hasModifier(this, RetainMod.ID))
            CardModifierManager.addModifier(this, new RetainMod());
    }

    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
        if (cardsToPreview != null)
            this.cardsToPreview.upgrade();
    }
}
