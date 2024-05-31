package superstitio.cards.general.SkillCard.gainEnergy;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.SexualHeat;
import superstitioapi.actions.XCostAction;

public class ForceOrgasm extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(ForceOrgasm.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = -1;
    private static final int MAGIC = 15;
    private static final int UPGRADE_MAGIC = 5;
    private static final int ExhaustNum = 1;

    public ForceOrgasm() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.exhaust = true;
//        ExhaustiveVariable.setBaseValue(this, 2);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        int magicNumber = this.magicNumber;
        addToBot(new XCostAction(this, AbstractGameAction.ActionType.ENERGY, effect -> {
            SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, effect * magicNumber);
            addToBot_applyPower(new superstitio.powers.Overdraft(AbstractDungeon.player, effect * ExhaustNum));
        }));
    }

    @Override
    public void upgradeAuto() {
    }
}
