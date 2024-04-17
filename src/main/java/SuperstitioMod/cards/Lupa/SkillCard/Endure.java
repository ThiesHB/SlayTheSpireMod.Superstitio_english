package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.powers.DelaySexualHeat;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Endure extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(Endure.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int MagicNum = 10;
    private static final int MagicNum_Update = 5;


    public Endure() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MagicNum);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot_gainPowerToPlayer(new DelaySexualHeat(player,this.magicNumber));
        this.addToBot_drawCards(2);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(MagicNum_Update);
        }
    }
}
