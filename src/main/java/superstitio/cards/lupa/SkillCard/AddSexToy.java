package superstitio.cards.lupa.SkillCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.cards.lupa.TempCard.SexToy;
import superstitio.utils.ActionUtility;

public class AddSexToy extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(AddSexToy.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGICNumber = 1;
    private static final int UPGRADE_MagicNumber = 1;

    public AddSexToy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
        this.exhaust = true;
        this.cardsToPreview = new SexToy();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        ActionUtility.addToBot_makeTempCardInBattle(new SexToy(), BattleCardPlace.Hand, this.magicNumber, this.upgraded);
    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MagicNumber);
        upgradeCardsToPreview();
    }
}
