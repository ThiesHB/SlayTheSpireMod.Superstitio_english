package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.SuperstitioModSetup;
import SuperstitioMod.actions.OpenAWindowAndChoseFromCardGroupAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Omniscience_TempName extends AbstractLupaCard {
    public static final String ID = SuperstitioModSetup.MakeTextID(Omniscience_TempName.class.getSimpleName());

    public static final AbstractCard.CardType CARD_TYPE = AbstractCard.CardType.SKILL;

    public static final AbstractCard.CardRarity CARD_RARITY = AbstractCard.CardRarity.UNCOMMON;

    public static final AbstractCard.CardTarget CARD_TARGET = AbstractCard.CardTarget.SELF;

    private static final int COST = 3;
    private static final int MAGICNumber = 4;
    private static final int UPGRADE_MagicNumber = 2;

    public Omniscience_TempName() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGICNumber);
        this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new OpenAWindowAndChoseFromCardGroupAction(
                this.magicNumber,
                AbstractDungeon.player.drawPile,
                String.format(cardStrings.EXTENDED_DESCRIPTION[0], this.magicNumber),
                card -> new ExhaustSpecificCardAction(card, AbstractDungeon.player.drawPile),
                true));
    }

//    @Override
//    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
//        return super.canUse(p, m) && !AbstractDungeon.player.drawPile.isEmpty();
//    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_MagicNumber);
        }
    }
}
