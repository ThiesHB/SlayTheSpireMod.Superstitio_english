package superstitio.cards.general.SkillCard.cardManipulation;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.ChoseCardFromGridSelectWindowAction;
import superstitio.cards.general.GeneralCard;

public class BecomeTrash extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(BecomeTrash.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = 2;

    public BecomeTrash() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        new ChoseCardFromGridSelectWindowAction(
                AbstractDungeon.player.drawPile, card -> new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile))
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0], this.magicNumber))
                .setAnyNumber(true)
                .setChoseAmount(this.magicNumber)
                .addToBot();
    }

    @Override
    public void upgradeAuto() {
    }
}
