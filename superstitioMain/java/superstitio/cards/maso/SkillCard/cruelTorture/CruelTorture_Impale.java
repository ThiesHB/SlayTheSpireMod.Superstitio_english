package superstitio.cards.maso.SkillCard.cruelTorture;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.card.CruelTortureTag;
import superstitio.cards.maso.MasoCard;

//尖桩贯穿
public class CruelTorture_Impale extends MasoCard {
    public static final String ID = DataManager.MakeTextID(CruelTorture_Impale.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;
    //    public static final int BURN_CARD_NUM = 2;
    private static final int COST = 1;
    private static final int MAGIC = 5;
    private static final int UPGRADE_MAGIC = -2;

    public CruelTorture_Impale() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new CruelTortureTag());
        initializeDescription();
    }

    @Override
    public void applyPowers() {
//        updateRawDescription();
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void initializeDescription() {
        updateRawDescription();
        super.initializeDescription();
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.magicNumber != 0 ? sumAllDelayHpLosePower() / this.magicNumber : 0);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_drawCards(this.magicNumber != 0 ? sumAllDelayHpLosePower() / this.magicNumber : 0);
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber));
        addToBot_drawCards();
    }

    @Override
    public void upgradeAuto() {
    }
}
