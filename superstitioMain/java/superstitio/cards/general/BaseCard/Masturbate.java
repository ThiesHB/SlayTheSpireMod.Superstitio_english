package superstitio.cards.general.BaseCard;

import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.SexualHeat;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;
import superstitioapi.hangUpCard.CardOrb_WaitCardTrigger;
import superstitioapi.utils.PowerUtility;

public class Masturbate extends GeneralCard implements GoSomewhereElseAfterUse {
    public static final String ID = DataManager.MakeTextID(Masturbate.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = 2;
    private static final int DRAWCard = 1;

    private static final int WAIT = 2;


    public Masturbate() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        SexualHeat.addToBot_addSexualHeat(player, this.magicNumber);

    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_WaitCardTrigger(this, cardGroup, WAIT, (orb, playedCard) -> {
            orb.StartHitCreature(AbstractDungeon.player);
            addToBot_drawCards(DRAWCard);
            PowerUtility.BubbleMessage(orb.getOriginCard().hb, false, this.cardStrings.getEXTENDED_DESCRIPTION()[0]);
        })
                .setDiscardOnEndOfTurn()
                .addToBot_HangCard();
    }

    @Override
    public void upgradeAuto() {
    }
}
