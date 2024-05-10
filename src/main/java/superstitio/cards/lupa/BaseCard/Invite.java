package superstitio.cards.lupa.BaseCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;

public class Invite extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Invite.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public Invite() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.tags.add(CardTags.STARTER_DEFEND);
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK, new RemoveDelayHpLoseBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
//        addToBot_reducePowerToPlayer(SexualDamage_ByEnemy.POWER_ID, this.block);
    }

    @Override
    public void upgradeAuto() {
    }
}
