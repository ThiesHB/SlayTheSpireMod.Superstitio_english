package SuperstitioMod.cards.Lupa.PowerCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupa;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

//咕杀/くっころ
public class Ku_Koro extends AbstractLupa {
    public static final String ID = SuperstitioModSetup.MakeTextID(Ku_Koro.class.getSimpleName());
    //从.json文件中提取键名为Strike_Lupa的信息

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int COST_UPGRADE = 0;

    public Ku_Koro() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "default");
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        gainPowerToPlayer(new SuperstitioMod.powers.Ku_Koro(player));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeBaseCost(COST_UPGRADE);
        }
    }
}
