package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CoitalVocal extends AbstractLupaCard {
    public static final String ID =
            SuperstitioModSetup.MakeTextID(CoitalVocal.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 4;
    private static final int UPGRADE_BLOCK = 2;

    public CoitalVocal() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_BLOCK);
        }
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
