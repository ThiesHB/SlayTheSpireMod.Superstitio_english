package superstitio.cards.general.PowerCard.monsterGirl;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import superstitio.DataManager;
import superstitio.cards.general.AbstractTempCard;

public class KaakaGirlMode extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(KaakaGirlMode.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = -2;
    private static final int MAGIC = 2;

    public KaakaGirlMode() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(final AbstractPlayer p, final AbstractMonster m) {
        this.onChoseThisOption();
    }

    @Override
    public void onChoseThisOption() {
        addToBot_applyPower(new RitualPower(AbstractDungeon.player, this.magicNumber, true));
    }

    @Override
    public void upgradeAuto() {
    }
}
