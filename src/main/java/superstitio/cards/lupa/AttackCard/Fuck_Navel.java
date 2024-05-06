package superstitio.cards.lupa.AttackCard;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;
import superstitio.powers.SexualHeat;
import superstitio.powers.interfaces.orgasm.OnOrgasm_onOrgasm;

@AutoAdd.Ignore
public class Fuck_Navel extends AbstractLupaCard_FuckJob implements OnOrgasm_onOrgasm {
    public static final String ID = DataManager.MakeTextID(Fuck_Navel.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2;

    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;

    public Fuck_Navel() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Inside(this.name);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void onOrgasm(SexualHeat SexualHeatPower) {
        if (AbstractDungeon.player.hand.contains(this)) {
            this.flash();
            this.setupDamage(this.damage + this.magicNumber);
        }
    }
}
