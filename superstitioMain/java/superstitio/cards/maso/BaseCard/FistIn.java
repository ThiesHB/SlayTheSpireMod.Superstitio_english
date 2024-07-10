package superstitio.cards.maso.BaseCard;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import superstitio.DataManager;
import superstitio.cards.general.TempCard.SelfReference;
import superstitio.cards.maso.MasoCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.actions.ChoseCardFromHandCardSelectScreen;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.PowerUtility;

import static com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting.SELF_OR_ENEMY;
import static superstitioapi.utils.ActionUtility.addToBot_makeTempCardInBattle;
import static superstitioapi.utils.CardUtility.getSelfOrEnemyTarget;

public class FistIn extends MasoCard implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(FistIn.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = SELF_OR_ENEMY;

    private static final int COST = 0;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = -1;
    private static final int DRAWCard = 1;

    private static final int WAIT = 3;

    public FistIn() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(WAIT,DRAWCard);
    }

    private void tryToUseCard(AbstractCard card, AbstractCreature target) {
        if (card.target == CardTarget.ENEMY && target instanceof AbstractMonster)
            addToBot(new NewQueueCardAction(card, target, false, true));
        else if (card.target == SELF_OR_ENEMY) {
            addToBot(new NewQueueCardAction(card, target instanceof AbstractMonster ? target : null, false, true));
        } else
            addToBot(new NewQueueCardAction(card, true, false, true));
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = getSelfOrEnemyTarget(this, monster);
        new ChoseCardFromHandCardSelectScreen(card -> {
            if (card instanceof FistIn) {
                addToBot_makeTempCardInBattle(new SelfReference(), ActionUtility.BattleCardPlace.Hand, card.upgraded);
                addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                return;
            }
            AutoDoneInstantAction.addToBotAbstract(() -> {
                int costSave;
                if (card.costForTurn >= 0)
                    costSave = card.costForTurn;
                else if (card.costForTurn == -1)
                    costSave = EnergyPanel.getCurrentEnergy();
                else
                    costSave = 0;
                addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, costSave * magicNumber));

                tryToUseCard(card, target);
            });
        })
                .setRetainFilter()
                .setWindowText(cardStrings.getEXTENDED_DESCRIPTION()[0])
                .addToBot();
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_WaitCardTrigger(this, cardGroup, WAIT, (orb, playedCard) -> {
            orb.StartHitCreature(AbstractDungeon.player);
            addToBot_drawCards(DRAWCard);
            PowerUtility.BubbleMessage(orb.getOriginCard().hb, false, this.cardStrings.getEXTENDED_DESCRIPTION()[0]);
        })
//                .setDiscardOnEndOfTurn()
                .addToBot_HangCard();
    }

    @Override
    public void upgradeAuto() {
    }
}

