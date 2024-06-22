package superstitio.cards.general.BaseCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitioapi.cards.DamageActionMaker;

public class Kiss extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(Kiss.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 6;
    private static final int UPGRADE_DAMAGE = 3;

    public Kiss() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        //添加基础攻击标签和将伤害设为6
        this.tags.add(CardTags.STARTER_STRIKE);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, DamageActionMaker.DamageEffect.HeartMultiInOne);
    }

    @Override
    public void upgradeAuto() {
    }
}
