package superstitio.cards.general.TempCard;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.AutoplayField;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.general.AbstractTempCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;

import java.util.ArrayList;

public class FeelPhantomBody extends AbstractTempCard {
    public static final String ID = DataManager.MakeTextID(FeelPhantomBody.class);

    public static final CardType CARD_TYPE = CardType.CURSE;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ALL;

    private static final int COST = -2;
    private static final int BLOCK = 5;
    private static final int UPGRADE_PLUS_BLOCK = 3;
    public ArrayList<AbstractPower> sealPower = new ArrayList<>();
    public AbstractCreature sealMonster = null;

    public FeelPhantomBody() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_PLUS_BLOCK, new RemoveDelayHpLoseBlock());
        AutoplayField.autoplay.set(this, true);
        this.dontTriggerOnUseCard = true;
        this.shuffleBackIntoDrawPile = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
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
