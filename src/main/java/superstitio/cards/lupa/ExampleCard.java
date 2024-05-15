package superstitio.cards.lupa;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.SexBlock;

@AutoAdd.Ignore
public class ExampleCard extends LupaCard {
    public static final String ID = DataManager.MakeTextID(ExampleCard.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int COST_UPGRADED_NEW = 10;
    private static final int DAMAGE = 2;
    private static final int UPGRADE_DAMAGE = 10;
    private static final int BLOCK = 24;
    private static final int UPGRADE_BLOCK = 10;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = 10;

    public ExampleCard() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE,UPGRADE_DAMAGE);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new SexBlock());
        this.setupMagicNumber(MAGIC,UPGRADE_BLOCK);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
    }

    @Override
    public void upgradeAuto() {
    }
}
