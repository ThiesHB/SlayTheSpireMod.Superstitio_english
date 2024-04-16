package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NoDrawPower;

public class TimeStop extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(TimeStop.class.getSimpleName());

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
        this.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SuperstitioMod.powers.TimeStop(player,1)));
        this.gainPowerToPlayer(new NoDrawPower(player));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeBaseCost(COST_Update);
        }
    }
}
