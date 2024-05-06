package superstitio.cards.lupa.AttackCard;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.utils.ActionUtility;

public class Job_Hair extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Hair.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 10;
    private static final int UPGRADE_DAMAGE = 4;

    private static final int MAGIC = 2;

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
        Job_Hair card = new Job_Hair(this.baseDamage - this.magicNumber, 0);
        CardModifierManager.addModifier(card, new ExhaustMod());
        return card;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        Job_Hair card = this.makeCardCopyWithDamageDecrease();
//        card.cardsToPreview = card.makeCardCopyWithDamageDecrease();
//        card.cardsToPreview.baseDamage = (card.baseDamage - card.magicNumber);
//        card.cardsToPreview.initializeDescription();
        ActionUtility.addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand, upgraded);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
