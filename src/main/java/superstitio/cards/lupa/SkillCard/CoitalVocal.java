package superstitio.cards.lupa.SkillCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.cardModifier.modifiers.block.SexBlock;

public class CoitalVocal extends LupaCard {
    public static final String ID =
            DataManager.MakeTextID(CoitalVocal.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 4;
    private static final int UPGRADE_BLOCK = 2;

    public CoitalVocal() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new SexBlock());
    }

    @Override
    public void upgradeAuto() {
    }


    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        int monsterNum =
                (int) AbstractDungeon.getMonsters().monsters.stream()
                        .filter(m -> !m.isDeadOrEscaped()).count();
        for (int i = 0; i < monsterNum + 1; i++) {
            addToBot_gainBlock();
        }
    }
}
