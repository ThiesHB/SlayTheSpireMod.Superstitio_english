package superstitio.cards.lupa.BaseCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.DamageActionMaker;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.orbs.CardOrb_CardTrigger;
import superstitio.orbs.CardOrb_EachCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;
import superstitio.utils.CardUtility;

import java.util.Arrays;

public class Job_Hand extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Hand.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int MAGIC = 2;

    public Job_Hand() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
//        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_EachCardTrigger(this, (orb, playedCard) -> {
                    AbstractMonster creature = CardOrb_CardTrigger.getHoveredMonsterSafe();
                    orb.StartHitCreature(creature);
                    DamageActionMaker.maker(orb.card.damage, creature).setCard(this).addToBot();
                }, this.magicNumber).setCardPredicate(card -> card.type == CardType.ATTACK));
        AutoDoneInstantAction.addToBotAbstract(()-> Arrays.stream(CardUtility.AllCardGroupInBattle()).forEach(cardGroup -> cardGroup.removeCard(this)));
    }

    @Override
    public void upgradeAuto() {
    }
}
