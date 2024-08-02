package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.SexualHeat;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_EachCardTrigger;


public class Job_Blow extends LupaCard implements FuckJob_Card, GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Blow.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = 4;
    private static final int HEAT_GIVE = 1;

    public Job_Blow() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, SuperstitioApiSetup.DamageEffect.HeartMultiInOne);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_EachCardTrigger(this, cardGroup, this.magicNumber, (orb, card) -> {
            orb.StartHitCreature(AbstractDungeon.player);
            SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, HEAT_GIVE);
        })
                .setCardPredicate(card -> card instanceof FuckJob_Card)
                .setTargetType(CardTarget.SELF)
                .addToBot_HangCard();
    }
}
