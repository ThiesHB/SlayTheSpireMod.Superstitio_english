package superstitio.cards.general.AttackCard.hand;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.DamageActionMaker;
import superstitio.cards.general.AbstractCard_FuckJob;
import superstitio.cards.patch.GoSomewhereElseAfterUse;
import superstitio.orbs.CardOrb_WaitCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;

public class Job_Armpit extends AbstractCard_FuckJob implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Armpit.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int MAGIC = 2;

    public Job_Armpit() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    public Job_Armpit(int damage) {
        this();
        this.setupDamage(damage);
    }


    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_WaitCardTrigger(this, cardGroup,(orb, card) -> {
                    AbstractMonster creature = DamageActionMaker.getMonsterOrFirstMonster(orb.lastTarget);
                    orb.StartHitCreature(creature);
                    DamageActionMaker.maker(orb.getOriginCard().damage, creature).setExampleCard(this).addToBot();
                }, this.magicNumber)
                        .setCardPredicate(card -> card.type == CardType.ATTACK)
                        .setNotEvokeOnEndOfTurn()
        );
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_WaitCardTrigger(this.makeCopy(), cardGroup,(orb, card) -> {
                    AbstractMonster creature = DamageActionMaker.getMonsterOrFirstMonster(orb.lastTarget);
                    orb.StartHitCreature(creature);
                    DamageActionMaker.maker(orb.getOriginCard().damage, creature).setExampleCard(this).addToBot();
                }, this.magicNumber)
                        .setCardPredicate(card -> card.type == CardType.ATTACK)
                        .setNotEvokeOnEndOfTurn()
        );
    }
}
