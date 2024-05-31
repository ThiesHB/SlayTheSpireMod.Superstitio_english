package superstitio.cards.general.SkillCard.drawCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;

//透支
public class Overdraft extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(Overdraft.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;
    private static final int ExhaustNum = 1;


    public Overdraft() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_drawCards(MAGIC);
        addToBot_applyPower(new superstitio.powers.Overdraft(AbstractDungeon.player, ExhaustNum));
    }

    @Override
    public void upgradeAuto() {
    }
}
