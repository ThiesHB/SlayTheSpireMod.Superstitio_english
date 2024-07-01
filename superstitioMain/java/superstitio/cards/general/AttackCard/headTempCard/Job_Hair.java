package superstitio.cards.general.AttackCard.headTempCard;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;
import superstitioapi.utils.ActionUtility;


public class Job_Hair extends GeneralCard implements FuckJob_Card, GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Hair.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int MAGIC = 3;

    public Job_Hair() {
        this(DAMAGE, UPGRADE_DAMAGE);
        this.cardsToPreview = this.makeCardCopyWithDamageDecrease();
    }

    private Job_Hair(int damage, int upgradeDamage) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(damage, upgradeDamage);
        this.isEthereal = true;
        this.setupMagicNumber(MAGIC);
    }

    private Job_Hair makeCardCopyWithDamageDecrease() {
        Job_Hair card = new Job_Hair(DAMAGE, UPGRADE_DAMAGE);
        card.freeToPlayOnce = true;
        CardModifierManager.addModifier(card, new ExhaustMod());
        card.initializeDescription();
        return card;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, DamageActionMaker.DamageEffect.HeartMultiInOne);
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        Job_Hair job_hair = this.makeCardCopyWithDamageDecrease();
        new CardOrb_WaitCardTrigger(this, cardGroup, this.magicNumber, (orb, card) -> {
            orb.StartHitCreature(AbstractDungeon.player);
            ActionUtility.addToBot_makeTempCardInBattle(job_hair, ActionUtility.BattleCardPlace.Hand, this.upgraded);
        })
                .setDiscardOnEndOfTurn()
                .setTargetType(CardTarget.SELF)
                .addToBot_HangCard();
    }
}
