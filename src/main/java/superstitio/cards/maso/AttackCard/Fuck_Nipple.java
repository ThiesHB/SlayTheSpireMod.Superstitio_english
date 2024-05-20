package superstitio.cards.maso.AttackCard;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageRandomEnemyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.maso.MasoCard;

@AutoAdd.Ignore
public class Fuck_Nipple extends MasoCard implements FuckJob_Card {
    public static final String ID = DataManager.MakeTextID(Fuck_Nipple.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ALL_ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 5;
    private static final int UPGRADE_DAMAGE = 2;

    public Fuck_Nipple() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.damage),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        this.addToBot(new DamageRandomEnemyAction(new DamageInfo(AbstractDungeon.player, this.damage),
                AbstractGameAction.AttackEffect.BLUNT_LIGHT));

    }

    @Override
    public void upgradeAuto() {
    }
}
