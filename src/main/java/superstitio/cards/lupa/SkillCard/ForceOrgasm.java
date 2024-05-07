package superstitio.cards.lupa.SkillCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.XCostAction;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.powers.SexualHeat;

public class ForceOrgasm extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(ForceOrgasm.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = -1;
    private static final int MAGIC = 10;
    private static final int UPGRADE_MAGIC = 5;

    public ForceOrgasm() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int magicNumber = this.magicNumber;
        addToBot(new XCostAction(this, AbstractGameAction.ActionType.ENERGY, effect ->
                addToBot_applyPower(new SexualHeat(AbstractDungeon.player, effect * magicNumber))));
    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MAGIC);
    }
}
