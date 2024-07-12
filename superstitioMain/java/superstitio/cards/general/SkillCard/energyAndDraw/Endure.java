package superstitio.cards.general.SkillCard.energyAndDraw;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.SexBlock;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.DelaySexualHeat;

public class Endure extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(Endure.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = 5;
    private static final int BLOCK = 5;
    private static final int UPGRADE_BLOCK = 0;
    private static final int DRAWCard_NUM = 2;


    public Endure() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new SexBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        addToBot_applyPower(new DelaySexualHeat(player, this.magicNumber));
        addToBot_drawCards(DRAWCard_NUM);
    }

    @Override
    public void upgradeAuto() {
    }
}
