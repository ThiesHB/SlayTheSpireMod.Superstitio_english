package superstitio.cards.general.AttackCard.breast;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.DamageActionMaker;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.patch.GoSomewhereElseAfterUse;
import superstitio.orbs.CardOrb_EachCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;

@AutoAdd.Ignore
public class Job_Breast extends GeneralCard implements FuckJob_Card, GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Breast.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_DAMAGE = 7;
    private static final int MAGIC = 2;

    public Job_Breast() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_EachCardTrigger(this, cardGroup, (orb, card) -> {
                    AbstractMonster creature = DamageActionMaker.getMonsterOrFirstMonster(orb.lastTarget);
                    orb.StartHitCreature(creature);
                    DamageActionMaker.maker(orb.getOriginCard().damage, creature).setExampleCard(this).addToBot();
                }, this.magicNumber)
                        .setCardPredicate(card -> card.type == CardType.ATTACK)
        );
    }
}
