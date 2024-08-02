package superstitio.cards.general.AttackCard.hand;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;
import superstitioapi.utils.CreatureUtility;

public class Job_Armpit extends GeneralCard implements FuckJob_Card, GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Armpit.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int MAGIC = 2;

    public Job_Armpit() {
        this(true);
    }

    public Job_Armpit(boolean hasCardToPreview) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
        if (hasCardToPreview)
            this.cardsToPreview = new Job_Armpit(false);
    }


    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_WaitCardTrigger(this, cardGroup, this.magicNumber, (orb, card) -> {
            AbstractMonster creature = CreatureUtility.getMonsterOrRandomMonster(orb.lastTarget);
            orb.StartHitCreature(creature);
            DamageActionMaker.maker(orb.getOriginCard().damage, creature)
                    .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                    .setExampleCard(this).addToBot();
        })
                .setCardPredicate(card -> card.type == CardType.ATTACK)
                .addToBot_HangCard();
        AbstractCard copyCard = this.makeCopy();
        if (upgraded)
            copyCard.upgrade();
        new CardOrb_WaitCardTrigger(copyCard, cardGroup, this.magicNumber, (orb, card) -> {
            AbstractMonster creature = CreatureUtility.getMonsterOrRandomMonster(orb.lastTarget);
            orb.StartHitCreature(creature);
            DamageActionMaker.maker(orb.getOriginCard().damage, creature)
                    .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                    .setExampleCard(this).addToBot();
        })
                .setCardPredicate(card -> card.type == CardType.ATTACK)
                .addToBot_HangCard();
    }
}
