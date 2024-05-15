package superstitio.cards.maso.AttackCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.SexualHeat;
import superstitio.powers.patchAndInterface.interfaces.orgasm.OnOrgasm_onOrgasm;

import static superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard;


public class Fuck_Navel extends MasoCard implements FuckJob_Card, OnOrgasm_onOrgasm, IsNotLupaCard {
    public static final String ID = DataManager.MakeTextID(Fuck_Navel.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ALL;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_DAMAGE = 6;

    private static final int MAGIC = 5;
    private static final int UPGRADE_MAGIC = 2;

    public Fuck_Navel() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
//        this.retain = true;
        this.selfRetain = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamageToAllEnemies(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot_dealDamage(AbstractDungeon.player, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

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
