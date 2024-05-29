package superstitio.cards.lupa.SkillCard.block;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.SexBlock;
import superstitio.cards.lupa.LupaCard;
import superstitio.delayHpLose.PreventHpLimit;

public class PrivatePhoto extends LupaCard {
    public static final String ID = DataManager.MakeTextID(PrivatePhoto.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;

    public PrivatePhoto() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new SexBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new PreventHpLimit(AbstractDungeon.player, this.magicNumber));
        addToBot_gainBlock();
    }

    @Override
    public void upgradeAuto() {
    }
}

