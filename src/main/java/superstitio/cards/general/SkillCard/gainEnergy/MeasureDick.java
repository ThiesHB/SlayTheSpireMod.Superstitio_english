package superstitio.cards.general.SkillCard.gainEnergy;

import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.actions.ChoseCardFromHandCardSelectScreen;
import superstitio.cards.general.GeneralCard;

//TODO 打算用小腹刻度的图片（还没搞，有空再说）
public class MeasureDick extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(MeasureDick.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;

    public MeasureDick() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot(new ChoseCardFromHandCardSelectScreen(targetCard -> AutoDoneInstantAction.addToBotAbstract(() -> {
                    addToTop(new GainEnergyAction(Math.max(targetCard.costForTurn - targetCard.makeCopy().costForTurn, 0)));
                    addToTop(new DiscardSpecificCardAction(targetCard));
                }))
                        .setAnyNumber(true)
//                        .setRetainFilter(card -> card.isCostModifiedForTurn || card.isCostModified)
                        .setChoiceAmount(this.magicNumber)
                        .setWindowText(String.format(this.cardStrings.getEXTENDED_DESCRIPTION()[0], this.magicNumber))
        );
    }

    @Override
    public void upgradeAuto() {
    }
}
