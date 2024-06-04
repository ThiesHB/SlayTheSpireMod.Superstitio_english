package superstitio.cards.general.AttackCard.hand;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_EachCardTrigger;

public class Job_Hand extends GeneralCard implements FuckJob_Card, GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Hand.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;
    private static final int STRENGTH_GET = 1;

    public Job_Hand() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_EachCardTrigger(this, cardGroup, this.magicNumber, (orb, card) -> {
            orb.StartHitCreature(AbstractDungeon.player);
            addToBot_applyPower(new StrengthPower(AbstractDungeon.player, STRENGTH_GET));
        })
                .setCardPredicate(card -> card.type == CardType.ATTACK)
                .setNotEvokeOnEndOfTurn()
                .setTargetType(CardTarget.SELF)
                .addToBot_HangCard();
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(STRENGTH_GET);
    }
}
