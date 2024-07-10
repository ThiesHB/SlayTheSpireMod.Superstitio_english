package superstitio.cards.maso.BaseCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.damage.SexDamage;
import superstitio.cards.lupa.LupaCard;
import superstitioapi.actions.AutoDoneInstantAction;

public class Spark extends LupaCard {
    public static final String ID = DataManager.MakeTextID(Spark.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int MAGIC = 8;
    private static final int UPGRADE_MAGIC = -2;

    public Spark() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE,new SexDamage());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster);
        AutoDoneInstantAction.addToBotAbstract(() -> {
            if (sumAllDelayHpLosePower() >= this.magicNumber)
                addToBot_dealDamage(monster);
        });
    }

//    @Override
//    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
//        new CardOrb_WaitCardTrigger(this, cardGroup, WAIT, (orb, playedCard) -> {
//            orb.StartHitCreature(AbstractDungeon.player);
//            addToBot_drawCards(DRAWCard);
//            PowerUtility.BubbleMessage(orb.getOriginCard().hb, false, this.cardStrings.getEXTENDED_DESCRIPTION()[0]);
//        })
//                .setDiscardOnEndOfTurn()
//                .addToBot_HangCard();
//    }

//    @Override
//    public void update() {
//        super.update();
//        if (AbstractDungeon.player != null && AbstractDungeon.player.hoveredCard == this) {
//            calculateBlock();
//            initializeDescription();
//        }
//    }

    @Override
    public void upgradeAuto() {
    }
}
