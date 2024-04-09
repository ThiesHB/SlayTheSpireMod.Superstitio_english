package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.powers.SexualHeat;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CalmDown extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(CalmDown.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MagicNumber = 2;
    private static final int UPGRADE_MagicNumber = 1;

    private static final int HeatReduce = 4;

    public CalmDown() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MagicNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.drawCards(MagicNumber);
        this.gainPowerToPlayer(new SexualHeat(player, -HeatReduce));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
