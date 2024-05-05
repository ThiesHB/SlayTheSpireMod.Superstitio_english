package superstitio.cards.lupa.SkillCard;

import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.powers.SexualHeat;
import basemod.AutoAdd;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

@AutoAdd.Ignore
public class ForceOrgasm extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(ForceOrgasm.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGICNumber = 10;

    public ForceOrgasm() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new SexualHeat(AbstractDungeon.player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }
}
