package superstitio.cards.maso.AttackCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.TempCard.Fuck_Ear;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;
import superstitioapi.utils.ActionUtility;

import static superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard;

public class Fuck_Eye extends MasoCard implements FuckJob_Card, GoSomewhereElseAfterUse, IsNotLupaCard {
    public static final String ID = DataManager.MakeTextID(Fuck_Eye.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF_AND_ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 2;
    private static final int MAGIC = 3;

    public Fuck_Eye() {
        this(false);
        this.cardsToPreview = new Fuck_Ear(false);
    }

    public Fuck_Eye(boolean blank) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupBlock(BLOCK, UPGRADE_BLOCK);
        this.setupMagicNumber(MAGIC);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, DamageActionMaker.DamageEffect.HeartMultiInOne);
        addToBot_dealDamage(monster, DamageActionMaker.DamageEffect.HeartMultiInOne);
        addToBot_dealDamage(AbstractDungeon.player, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_dealDamage(AbstractDungeon.player, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_applyPower(new WeakPower(AbstractDungeon.player, 1, false));
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_WaitCardTrigger(this, cardGroup, this.magicNumber, (orb, card) -> {
            orb.StartHitCreature(AbstractDungeon.player);
            addToBot_gainCustomBlock(new RemoveDelayHpLoseBlock());
            ActionUtility.addToBot_makeTempCardInBattle(new Fuck_Ear(), ActionUtility.BattleCardPlace.Hand, this.upgraded);
        })
                .setTargetType(CardTarget.SELF)
                .setDesc(this.rawDescription)
                .addToBot_HangCard();
    }
}
