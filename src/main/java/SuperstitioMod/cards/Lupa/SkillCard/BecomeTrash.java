package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.actions.ChoseCardFromGridSelectWindowAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class BecomeTrash extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(BecomeTrash.class.getSimpleName());

    public static final AbstractCard.CardType CARD_TYPE = AbstractCard.CardType.SKILL;

    public static final AbstractCard.CardRarity CARD_RARITY = AbstractCard.CardRarity.UNCOMMON;

    public static final AbstractCard.CardTarget CARD_TARGET = AbstractCard.CardTarget.SELF;

    private static final int COST = 3;
    private static final int MAGICNumber = 4;
    private static final int UPGRADE_MagicNumber = 2;

    public BecomeTrash() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new ChoseCardFromGridSelectWindowAction(
                AbstractDungeon.player.drawPile, card -> new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile))
                .setupWindowText(String.format(getEXTENDED_DESCRIPTION()[0], this.magicNumber))
                .setupAnyNumber(true)
                .setupChoseAmount(this.magicNumber)
        );
    }

    @Override
    public void upgradeAuto() {
        upgradeMagicNumber(UPGRADE_MagicNumber);
    }
}
