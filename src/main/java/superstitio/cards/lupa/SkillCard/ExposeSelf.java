package superstitio.cards.lupa.SkillCard;

import superstitio.DataManager;
import superstitio.cards.modifiers.block.SexBlock;
import superstitio.cards.lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 抵消敌人的攻击，转换为精液
 */
public class ExposeSelf extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(ExposeSelf.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 3;

    public ExposeSelf() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK,new SexBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        addToBot_applyPower(new superstitio.powers.ExposeSelf(player, 1));
    }

    @Override
    public void upgradeAuto() {
        upgradeBlock(UPGRADE_BLOCK);
    }
}
