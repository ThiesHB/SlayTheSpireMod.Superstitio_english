package SuperstitioMod.cards.Lupa.SkillCard;

import SuperstitioMod.DataManager;
import SuperstitioMod.actions.ChoseCardFromGridSelectWindowAction;
import SuperstitioMod.cards.Lupa.AbstractLupaCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Objects;

//荣耀洞
public class GloryHole extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(GloryHole.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;

    private static final int COST_UPDATE = 1;


    public GloryHole() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.exhaust = true;
    }

    public static AbstractGameAction makeCopy(AbstractCard card) {
        return new MakeTempCardInHandAction(card.makeCopy());
    }

    public static CardGroup mainCardGroupExceptSelf() {
        CardGroup temp = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        AbstractDungeon.player.masterDeck.group.stream().filter(abstractCard -> !Objects.equals(abstractCard.cardID, GloryHole.ID)).forEach(temp::addToTop);
        return temp;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new ChoseCardFromGridSelectWindowAction(mainCardGroupExceptSelf(), GloryHole::makeCopy)
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0]))
                .setAnyNumber(true));
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPDATE);
    }
}
