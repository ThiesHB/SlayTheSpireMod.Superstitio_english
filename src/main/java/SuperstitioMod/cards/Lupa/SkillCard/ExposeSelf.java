package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

/**
 * 抵消敌人的攻击，转换为精液
 */
public class ExposeSelf extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(ExposeSelf.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 3;

    public ExposeSelf() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot_gainBlock();
        this.addToBot_applyPowerToPlayer(new SuperstitioMod.powers.ExposeSelf(player,1));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
        }
    }
}
