package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.red.Reaper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.SexualHeat;

import java.util.Arrays;


public class Whipping extends LupaCard {
    public static final String ID = DataManager.MakeTextID(Whipping.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 2;

    private static final int MAGIC = 3;
    private static final int HEAT_GET = 1;

    public Whipping() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
        this.isMultiDamage = true;
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(HEAT_GET);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamageToAllEnemies(AbstractGameAction.AttackEffect.LIGHTNING);
        SexualHeat.addToBot_addSexualHeat(AbstractDungeon.player, Arrays.stream(this.multiDamage).sum() / this.magicNumber);
    }

    @Override
    public void upgradeAuto() {
    }

}
