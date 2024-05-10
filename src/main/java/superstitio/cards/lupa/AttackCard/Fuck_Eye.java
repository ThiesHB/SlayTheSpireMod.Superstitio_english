package superstitio.cards.lupa.AttackCard;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.cards.lupa.TempCard.Fuck_Ear;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.orbs.CardOrb_WaitCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;

public class Fuck_Eye extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Fuck_Eye.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int BLOCK = 4;
    private static final int UPGRADE_BLOCK = 2;
    private static final int MAGIC = 3;

    public Fuck_Eye() {
        this(false);
        this.cardsToPreview = new Fuck_Ear(false);
    }

    public Fuck_Eye(boolean blank) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC);
        this.exhaust = true;
//        CardModifierManager.addModifier(this, new RetainMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_gainBlock();
//        ActionUtility.addToBot_makeTempCardInBattle(new Fuck_Ear(), BattleCardPlace.Hand, this.upgraded);
        AbstractCard fuck_ear = new Fuck_Ear();
        if (upgraded)
            fuck_ear.upgrade();
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_WaitCardTrigger(fuck_ear, (orb,card) -> {
                    orb.StartHitCreature(AbstractDungeon.player);
                    addToBot_gainBlock();
                }, this.magicNumber)
                        .setCardGroupReturnAfterEvoke(AbstractDungeon.player.hand)
                        .setNotEvokeOnEndOfTurn()
                        .setTargetType(CardTarget.SELF)
                        .setDesc(this.rawDescription)
        );
        addToBot_applyPower(new WeakPower(AbstractDungeon.player, 1, false));
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Inside(this.name);
    }

//    @Override
//    public void triggerWhenDrawn() {
//        if (!CardModifierManager.hasModifier(this, RetainMod.ID))
//            CardModifierManager.addModifier(this, new RetainMod());
//    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
