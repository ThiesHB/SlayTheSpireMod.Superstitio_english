package superstitio.cards.lupa.AttackCard;

import basemod.AutoAdd;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard_FuckJob;

@AutoAdd.Ignore
public class Job_Blow extends AbstractLupaCard_FuckJob {
    public static final String ID = DataManager.MakeTextID(Job_Blow.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 0;
    private static final int ATTACK_DMG = 5;
    private static final int UPGRADE_PLUS_DMG = 3;

    public Job_Blow() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(ATTACK_DMG);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        AbstractLupaCard_FuckJob.addToTop_gainSexMark_Outside(this.getEXTENDED_DESCRIPTION()[0]);
    }

    @Override
    public void upgradeAuto() {
        upgradeDamage(UPGRADE_PLUS_DMG);
    }
}
