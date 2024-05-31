package superstitio.cards.general.BaseCard;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;

public abstract class Invite extends SuperstitioCard {

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public Invite(String id) {
        super(id, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, DataManager.SPTT_DATA.GeneralEnums.GENERAL_CARD, "base");
        this.tags.add(CardTags.STARTER_DEFEND);
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK, makeNewBlockType());
    }

    protected abstract AbstractBlockModifier makeNewBlockType();

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
//        addToBot_reducePowerToPlayer(SexualDamage_ByEnemy.POWER_ID, this.block);
    }

    @Override
    public void upgradeAuto() {
    }
}
