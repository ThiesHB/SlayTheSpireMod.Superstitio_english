package superstitio.cards.general.TempCard;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.AbstractTempCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;

public class SelfReference extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(SelfReference.class);

    public static final CardType CARD_TYPE = CardType.CURSE;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = -2;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;

    public SelfReference() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK, new RemoveDelayHpLoseBlock());
        AutoplayField.autoplay.set(this, true);
        this.shuffleBackIntoDrawPile = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        this.dontTriggerOnUseCard = true;
        this.addToBot_gainBlock();
    }

    @Override
    public void triggerOnExhaust() {
        this.addToBot(new MakeTempCardInHandAction(this.makeCopy()));
    }

    @Override
    public void upgradeAuto() {
    }
}
