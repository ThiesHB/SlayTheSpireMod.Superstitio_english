package superstitio.cards.lupa.AttackCard;

import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.utils.ActionUtility;
import basemod.AutoAdd;
import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AutoAdd.Ignore
public class Job_Hair extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Hair.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 10;
    private static final int UPGRADE_PLUS_DMG = 4;

    private static final int MagicNumber = 2;

    public Job_Hair() {
        this(ATTACK_DMG);
        this.cardsToPreview = makeCardsToPreview();
    }

    private Job_Hair(int damage) {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.isEthereal = true;
        this.setupMagicNumber(MagicNumber);
    }

    private Job_Hair makeCardsToPreview() {
        Job_Hair card = new Job_Hair(this.baseDamage - this.magicNumber);
        CardModifierManager.addModifier(this.cardsToPreview, new ExhaustMod());
        return card;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        Job_Hair card = this.makeCardsToPreview();
        card.cardsToPreview = card.makeCardsToPreview();
        ActionUtility.addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand, upgraded);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
    }

    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
        upgradeCardsToPreview();
    }
}
