package superstitio.cards.lupa.SkillCard;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.actions.ChoseCardFromHandCardSelectScreen;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.cards.modifiers.block.SexBlock;

public class ZenState extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(ZenState.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int COST_UPGRADE_NEW = 0;
    private static final int BLOCK = 4;
    private static final int UPGRADE_BLOCK = 2;
    private static final int MAGIC = 1;

    public ZenState() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new SexBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.addToBot_gainBlock();
        this.addToBot(new ChoseCardFromHandCardSelectScreen(
                this::letSpecificCardExhaust)
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0], this.magicNumber))
                .setChoiceAmount(this.magicNumber)
                .setRetainFilter(card -> !card.exhaust, card -> !CardModifierManager.hasModifier(card, ExhaustMod.ID))
        );
    }

    public AbstractGameAction letSpecificCardExhaust(AbstractCard card) {
        return AutoDoneInstantAction.newAutoDone(() -> {
            card.superFlash();
            CardModifierManager.addModifier(card, new ExhaustMod());
        });
    }

    @Override
    public void upgradeAuto() {
        this.upgradeBaseCost(COST_UPGRADE_NEW);
    }
}
