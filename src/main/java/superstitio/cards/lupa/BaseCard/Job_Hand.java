package superstitio.cards.lupa.BaseCard;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;

public class Job_Hand extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Hand.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int ATTACK_DMG = 6;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Job_Hand() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        //添加基础攻击标签和将伤害设为6
        this.tags.add(CardTags.STARTER_STRIKE);
        this.setupDamage(ATTACK_DMG);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
    }

    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
    }
}
