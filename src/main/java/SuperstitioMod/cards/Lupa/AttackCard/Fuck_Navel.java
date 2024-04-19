package SuperstitioMod.cards.Lupa.AttackCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import SuperstitioMod.cards.Lupa.AbstractLupaCard_FuckJob;
import SuperstitioMod.powers.SexualHeat;
import SuperstitioMod.powers.interFace.OnOrgasm;
import SuperstitioMod.utils.CardUtility;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;


public class Fuck_Navel extends AbstractLupaCard_FuckJob implements OnOrgasm {
    public static final String ID = SuperstitioModSetup.MakeTextID(Fuck_Navel.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 2;

    private static final int MAGIC_Number = 3;
    private static final int MAGIC_Number_UPGRADE = 1;

    public Fuck_Navel() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
        this.setupMagicNumber(MAGIC_Number);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_damageToEnemy(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        CardUtility.addToTop_gainSexMark_Inside(this.name);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(MAGIC_Number_UPGRADE);
        }
    }

    @Override
    public void onCheckOrgasm(SexualHeat SexualHeatPower) {

    }

    @Override
    public void afterOrgasm(SexualHeat SexualHeatPower) {
        if (AbstractDungeon.player.hand.contains(this)) {
            this.flash();
            this.setupDamage(this.damage + this.magicNumber);
        }
    }

    @Override
    public void afterEndOrgasm(SexualHeat SexualHeatPower) {

    }

    @Override
    public boolean preventOrgasm(SexualHeat SexualHeatPower) {
        return false;
    }

    @Override
    public void beforeSquirt(SexualHeat SexualHeatPower) {

    }
}
