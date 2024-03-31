package SuperstitioMod.cards.Lupa.base;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import SuperstitioMod.powers.SexualHeat;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BaseSkill_Lupa extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(BaseSkill_Lupa.class.getSimpleName());
    //从.json文件中提取键名为Strike_Lupa的信息

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MagicNumber = 3;
    private static final int UPGRADE_MagicNumber = 2;
    private static final int DRAWCard = 1;


    public BaseSkill_Lupa() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.tags.add(CardTags.STARTER_DEFEND);
        this.setupMagicNumber(MagicNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.drawCards(DRAWCard);
        this.gainPowerToPlayer(new SexualHeat(player, MagicNumber));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
