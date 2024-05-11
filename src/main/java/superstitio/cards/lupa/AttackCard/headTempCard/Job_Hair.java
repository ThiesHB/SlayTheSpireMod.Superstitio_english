package superstitio.cards.lupa.AttackCard.headTempCard;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.cards.patch.GoSomewhereElseAfterUse;
import superstitio.orbs.CardOrb_WaitCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;
import superstitio.utils.ActionUtility;

public class Job_Hair extends AbstractLupaCard_FuckJob implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Job_Hair.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int MAGIC = 4;

    public Job_Hair() {
        this(DAMAGE, UPGRADE_DAMAGE);
        this.cardsToPreview = this.makeCardCopyWithDamageDecrease();
    }

    private Job_Hair(int damage, int upgradeDamage) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
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
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        Job_Hair job_hair = this.makeCardCopyWithDamageDecrease();
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_WaitCardTrigger(this,cardGroup, (orb, card) -> {
                    orb.StartHitCreature(AbstractDungeon.player);
                    ActionUtility.addToBot_makeTempCardInBattle(job_hair, BattleCardPlace.Hand, this.upgraded);
                }, this.magicNumber)
                        .setTargetType(CardTarget.SELF)
        );
    }
}
