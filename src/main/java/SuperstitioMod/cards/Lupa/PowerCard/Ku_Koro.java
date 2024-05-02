package SuperstitioMod.cards.Lupa.PowerCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

//咕杀/くっころ
public class Ku_Koro extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Ku_Koro.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int COST_UPGRADE = 0;

    public Ku_Koro() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPowerToPlayer(new SuperstitioMod.powers.Ku_Koro(player));
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADE);
    }

}
