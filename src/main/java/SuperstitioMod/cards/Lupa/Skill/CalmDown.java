package SuperstitioMod.cards.Lupa.Skill;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CalmDown extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(CalmDown.class.getSimpleName());
    //从.json文件中提取键名为Strike_Lupa的信息

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MagicNumber = 2;
    private static final int UPGRADE_MagicNumber = 1;

    public CalmDown() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "default");
        this.setupMagicNumber(MagicNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.drawCards(MagicNumber);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
