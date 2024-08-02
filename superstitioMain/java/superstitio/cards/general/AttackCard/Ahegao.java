package superstitio.cards.general.AttackCard;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.damage.SexDamage;
import superstitio.cards.general.GeneralCard;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_EachCardTrigger;
import superstitioapi.utils.CreatureUtility;

public class Ahegao extends GeneralCard implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Ahegao.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 2;
    private static final int COST_UPGRADED_NEW = 1;
    private static final int DAMAGE = 1;
    private static final int MAGIC = 99;

    public Ahegao() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, new SexDamage());
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW);
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_EachCardTrigger(this, cardGroup, this.magicNumber, (orb, card) -> {
            AbstractMonster creature = CreatureUtility.getMonsterOrRandomMonster(orb.lastTarget);
            orb.StartHitCreature(creature);
            DamageActionMaker.maker(orb.getOriginCard().damage, creature).setExampleCard(this)
                    .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                    .addToBot();
        })
                .setCardPredicate(card -> card.type == CardType.ATTACK)
                .addToBot_HangCard();

    }
}
