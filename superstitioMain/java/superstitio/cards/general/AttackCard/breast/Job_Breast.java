package superstitio.cards.general.AttackCard.breast;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.Milk;
import superstitioapi.cards.DamageActionMaker;

public class Job_Breast extends GeneralCard implements FuckJob_Card {
    public static final String ID = DataManager.MakeTextID(Job_Breast.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_DAMAGE = 7;
    private static final int MAGIC = 6;

    public Job_Breast() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, DamageActionMaker.DamageEffect.HeartMultiInOne);
        addToBot_applyPower(new Milk(AbstractDungeon.player, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }
}
