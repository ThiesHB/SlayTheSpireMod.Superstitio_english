package superstitio.cards.lupa.SkillCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;

/**
 * 抵消敌人的攻击，转换为精液
 */
public class ExposeSelf extends LupaCard {
    public static final String ID = DataManager.MakeTextID(ExposeSelf.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 3;

    public ExposeSelf() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        addToBot_applyPower(new superstitio.powers.lupaOnly.ExposeSelf(player, 1));
    }

    @Override
    public void upgradeAuto() {
    }
}
