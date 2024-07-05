package superstitio.cards.maso.SkillCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.WhenLostDrawCardBlock;
import superstitio.cards.maso.MasoCard;


public class HeavyCuffs extends MasoCard {
    public static final String ID = DataManager.MakeTextID(HeavyCuffs.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;

    public HeavyCuffs() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new WhenLostDrawCardBlock(this.magicNumber));
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
    }

    @Override
    public void upgradeAuto() {
    }
}
