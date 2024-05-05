package superstitio.cards.lupa.SkillCard;

import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.powers.DelaySexualHeat;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Endure extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Endure.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int MagicNum = 10;
    private static final int MagicNum_Update = 5;

    private static final int DRAWCard_NUM = 2;


    public Endure() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MagicNum);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new DelaySexualHeat(player, this.magicNumber));
        addToBot_drawCards(DRAWCard_NUM);
    }

    @Override
    public void upgradeAuto() {
        this.upgradeMagicNumber(MagicNum_Update);
    }
}
