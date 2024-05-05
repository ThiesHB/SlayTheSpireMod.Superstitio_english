package superstitio.cards.lupa.SkillCard;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;

@AutoAdd.Ignore
public class Sensitive3000 extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Sensitive3000.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGICNumber = 10;

    public Sensitive3000() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new superstitio.powers.Sensitive3000(AbstractDungeon.player));
    }

    @Override
    public void upgradeAuto() {
    }
}
