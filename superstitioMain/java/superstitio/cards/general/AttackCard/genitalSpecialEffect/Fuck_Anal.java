package superstitio.cards.general.AttackCard.genitalSpecialEffect;


import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.SexualDamage;

import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;

public class Fuck_Anal extends GeneralCard implements FuckJob_Card {
    public static final String ID = DataManager.MakeTextID(Fuck_Anal.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;
    private static final float DAMAGE_RATE = 0.1f;
    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 2;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = 2;

    public Fuck_Anal() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        addToBot_dealDamage(monster);
        addToBotAbstract(() ->
                monster.powers.stream().filter(power -> power instanceof SexualDamage).map(power -> (SexualDamage) power)
                        .findAny()
                        .ifPresent(sexualDamage -> addToBot_applyPower(new SexualDamage(
                                monster, (int) (sexualDamage.amount * DAMAGE_RATE * this.magicNumber), AbstractDungeon.player))));

    }
}
