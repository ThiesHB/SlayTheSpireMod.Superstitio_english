package superstitio.cards.lupa.SkillCard;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;

public class TimeStop extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(TimeStop.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;

    private static final int COST_Update = 2;


    public TimeStop() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new superstitio.powers.TimeStop(player, 1)));
        addToBot_applyPower(new NoDrawPower(player));
    }

    @Override
    public void upgradeAuto() {
        this.upgradeBaseCost(COST_Update);
    }
}
