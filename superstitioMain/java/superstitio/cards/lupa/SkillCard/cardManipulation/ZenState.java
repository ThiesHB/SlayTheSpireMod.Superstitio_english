package superstitio.cards.lupa.SkillCard.cardManipulation;

import basemod.cardmods.ExhaustMod;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen;
import superstitioapi.utils.CardUtility;

public class ZenState extends LupaCard {
    public static final String ID = DataManager.MakeTextID(ZenState.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int COST_UPGRADED_NEW = 0;
    private static final int BLOCK = 4;
    private static final int UPGRADE_BLOCK = 2;
    private static final int MAGIC = 1;

    public ZenState() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        addToBot(new ChoseCardFromHandCardSelectScreen(
                card -> {
                    addToBot_letSpecificCardExhaust(card);
                    if (!CardUtility.canUseWithoutEnvironment(card))
                        addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                })
                .setWindowText(String.format(getEXTENDED_DESCRIPTION()[0], this.magicNumber))
                .setChoiceAmount(this.magicNumber)
                .setRetainFilter(card -> !card.exhaust, card -> !CardModifierManager.hasModifier(card, ExhaustMod.ID))
        );
    }

    public void addToBot_letSpecificCardExhaust(AbstractCard card) {
        AutoDoneInstantAction.addToBotAbstract(() -> {
            card.superFlash();
            CardModifierManager.addModifier(card, new ExhaustMod());
        });
    }

    @Override
    public void upgradeAuto() {
        this.upgradeBaseCost(COST_UPGRADED_NEW);
    }
}
