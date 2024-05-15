package superstitio.cards.general.AttackCard.mouthCost;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.AbstractCard_FuckJob;
import superstitio.cards.patch.GoSomewhereElseAfterUse;
import superstitio.orbs.CardOrb_WaitCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;
import superstitio.powers.SexualHeat;


public class Job_Blow extends AbstractCard_FuckJob implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Blow.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int MAGIC = 4;
    private static final int HEAT_GIVE = 2;

    public Job_Blow() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_WaitCardTrigger(this, cardGroup, (orb, card) -> {
                    addToBot_applyPower(new SexualHeat(AbstractDungeon.player, HEAT_GIVE));
                }, this.magicNumber)
                        .setCardPredicate(card -> card instanceof AbstractCard_FuckJob)
                        .setNotEvokeOnEndOfTurn()
                        .setTargetType(CardTarget.SELF)
        );
    }
}
