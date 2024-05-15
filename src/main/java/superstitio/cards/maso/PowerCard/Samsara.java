package superstitio.cards.maso.PowerCard;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import superstitio.DataManager;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.utils.ActionUtility;


public class Samsara extends MasoCard {
    public static final String ID = DataManager.MakeTextID(Samsara.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;

    private static final int MAGIC = 6;
    private static final int UPGRADE_MAGIC = -2;

    public Samsara() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
//        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Wound(), 8, true, true));
        ActionUtility.addToBot_makeTempCardInBattle(new Wound(), BattleCardPlace.DrawPile, this.magicNumber);
        addToBot_applyPower(new EquilibriumPower(player, 99));
        addToBot_applyPower(new SamsaraPower(player));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class SamsaraPower extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(SamsaraPower.class);

        public SamsaraPower(final AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }


        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {
            this.flash();
            this.addToBot(new DrawCardAction(1));
        }

        @Override
        public void updateDescriptionArgs() {

        }
    }
}

