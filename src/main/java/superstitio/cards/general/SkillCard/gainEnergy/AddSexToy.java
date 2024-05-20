package superstitio.cards.general.SkillCard.gainEnergy;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.general.TempCard.SexToy;
import superstitio.utils.ActionUtility;

public class AddSexToy extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(AddSexToy.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 1;
//    private static final int UPGRADE_MAGIC = 1;

    public AddSexToy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
        this.exhaust = true;
        this.cardsToPreview = new SexToy();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        ActionUtility.addToBot_makeTempCardInBattle(new SexToy(), BattleCardPlace.Hand, this.magicNumber, this.upgraded);
    }

    @Override
    public void upgradeAuto() {
//        upgradeMagicNumber(UPGRADE_MAGIC);
        upgradeCardsToPreview();
    }
}
