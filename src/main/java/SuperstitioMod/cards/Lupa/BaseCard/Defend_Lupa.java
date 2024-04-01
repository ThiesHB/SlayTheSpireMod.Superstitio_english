package SuperstitioMod.cards.Lupa.BaseCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Defend_Lupa extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(Defend_Lupa.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public Defend_Lupa() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "default");
        this.tags.add(CardTags.STARTER_DEFEND);
        this.setupBlock(BLOCK);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainBlock();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BLOCK);
        }
    }
}
