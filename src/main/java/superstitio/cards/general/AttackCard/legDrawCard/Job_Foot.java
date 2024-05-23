package superstitio.cards.general.AttackCard.legDrawCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.BaseCard.Masturbate;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.lupa.LupaCard;
import superstitio.cards.patch.GoSomewhereElseAfterUse;
import superstitio.hangUpCard.CardOrb_EachCardTrigger;
import superstitio.hangUpCard.HangUpCardGroup;
import superstitio.utils.PowerUtility;

public class Job_Foot extends GeneralCard implements FuckJob_Card, GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Foot.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 4;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int DRAW_CARD = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;


    public Job_Foot() {
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
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_EachCardTrigger(this, cardGroup, (orb, card) -> {
                    orb.StartHitCreature(AbstractDungeon.player);
                    addToBot_drawCards(DRAW_CARD);
                    PowerUtility.BubbleMessage(orb.getOriginCard().hb, false,
                            LupaCard.getCardStringsWithSFWAndFlavor(
                                            DataManager.MakeTextID(Masturbate.class))
                                    .getEXTENDED_DESCRIPTION()[0]);
                }, this.magicNumber)
                        .setCardPredicate(card -> card instanceof FuckJob_Card)
                        .setTargetType(CardTarget.SELF)
        );
    }
}
