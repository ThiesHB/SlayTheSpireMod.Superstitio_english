package superstitio.cards.general.SkillCard.cardManipulation;

import basemod.cardmods.RetainMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.ChoseCardFromGridSelectWindowAction;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.general.TempCard.SelfReference;

import java.util.Objects;

//荣耀洞
public class GloryHole extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(GloryHole.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
//
//    private static final int COST_UPGRADED_NEW = 2;


    public GloryHole() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.exhaust = true;
    }

    private static AbstractGameAction makeChoseCardCopy(AbstractCard card) {
        if (Objects.equals(card.cardID, GloryHole.ID))
            return new MakeTempCardInHandAction(new SelfReference());
        return new MakeTempCardInHandAction(card.makeStatEquivalentCopy());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot(new ChoseCardFromGridSelectWindowAction(AbstractDungeon.player.masterDeck, card -> addToBot(makeChoseCardCopy(card)))
                        .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0]))
                        .setAnyNumber(true)
//                .setRetainFilter(card -> !Objects.equals(card.cardID, GloryHole.ID))
        );
    }

    @Override
    public void upgradeAuto() {
//        upgradeBaseCost(COST_UPGRADED_NEW);
        CardModifierManager.addModifier(this, new RetainMod());
    }
}
