package superstitio.cards.general.AttackCard.headTempCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import superstitio.DataManager;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.AbstractCard_FuckJob;
import superstitio.cards.general.TempCard.Fuck_Ear;
import superstitio.cards.patch.GoSomewhereElseAfterUse;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.orbs.CardOrb_WaitCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;
import superstitio.utils.ActionUtility;

import static superstitio.cards.CardOwnerPlayerManager.*;

public class Fuck_Eye extends AbstractCard_FuckJob implements GoSomewhereElseAfterUse , IsNotLupaCard {
    public static final String ID = DataManager.MakeTextID(Fuck_Eye.class);

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
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_applyPower(new WeakPower(AbstractDungeon.player, 1, false));
        
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_WaitCardTrigger(this,cardGroup, (orb, card) -> {
                    orb.StartHitCreature(AbstractDungeon.player);
                    addToBot_gainBlock();
                    ActionUtility.addToBot_makeTempCardInBattle(new Fuck_Ear(), BattleCardPlace.Hand, this.upgraded);
                }, this.magicNumber)
                        .setNotEvokeOnEndOfTurn()
                        .setTargetType(CardTarget.SELF)
                        .setDesc(this.rawDescription)
        );
    }
}
