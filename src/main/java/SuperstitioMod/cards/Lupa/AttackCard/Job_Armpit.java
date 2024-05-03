package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_FuckJob;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AutoAdd.Ignore
public class Job_Armpit extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Armpit.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 4;

    public Job_Armpit() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.retain = true;
    }

    public Job_Armpit(int damage) {
        this();
        this.setupDamage(damage);
    }


    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_damageToTarget(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_damageToTarget(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
    }

    @Override
    public void triggerOnEndOfPlayerTurn() {
        if (AbstractDungeon.player.hand.contains(this)) {
            addToBot(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
            for (int i = 0; i < 2; i++) {
                AbstractCard card = new Job_Armpit((this.damage + 1) / 2);
                addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand);
            }
        }
    }

    @Override
    public void atTurnStart() {

    }

    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
    }
}
