package superstitio.cards.lupa.PowerCard;

import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.utils.ActionUtility;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;


public class Samsara extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Samsara.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;

    private static final int MAGICNumber = 8;
    private static final int UPGRADE_MagicNumber = -2;

    public Samsara() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
//        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), 8, true, true));
        ActionUtility.addToBot_makeTempCardInBattle(new Wound(), BattleCardPlace.DrawPile, this.magicNumber);
        addToBot_applyPower(new EquilibriumPower(player, 99));
        addToBot_applyPower(new superstitio.powers.Samsara(player));
    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MagicNumber);
    }
}

