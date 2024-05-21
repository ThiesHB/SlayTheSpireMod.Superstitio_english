package superstitio.cards.maso.PowerCard;

import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.AbstractTempCard;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.masoOnly.*;

import java.util.ArrayList;

public class BodyModification_CutOff extends MasoCard {
    public static final String ID = DataManager.MakeTextID(BodyModification_CutOff.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 15;
    private static final int UPGRADE_MAGIC = 5;
    private static final int BLOCK = 18;
    private static final int UPGRADE_BLOCK = 5;

    public BodyModification_CutOff() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.cardStrings.getEXTENDED_DESCRIPTION()[0],this.cardStrings.getEXTENDED_DESCRIPTION()[1]);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        addToBot_applyPower(new BodyModification_CutOffPower(this.magicNumber));
        final ArrayList<AbstractCard> bodyParts = new ArrayList<>();
        bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Arm()));
        bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Breast()));
        bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Castration()));
        bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Head()));
        bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Leg()));
        this.addToBot(new ChooseOneAction(bodyParts));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class BodyModification_CutOffPower extends EasyBuildAbstractPowerForPowerCard {

        public static final int PERCENTAGE = 100;

        public BodyModification_CutOffPower(int amount) {
            super(amount);
        }

        @Override
        public int onAttackToChangeDamage(DamageInfo info, int damageAmount) {
            return damageAmount * this.amount / PERCENTAGE;
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return null;
        }
    }

    public static class BodyModification_CutOff_Chose extends AbstractTempCard {
        public static final String ID = DataManager.MakeTextID(BodyModification_CutOff_Chose.class);

        public static final CardType CARD_TYPE = CardType.POWER;

        public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

        public static final CardTarget CARD_TARGET = CardTarget.SELF;

        private static final int COST = -2;
        private final LostBodyPart lostBodyPart;

        public BodyModification_CutOff_Chose() {
            this(null);
        }

        public BodyModification_CutOff_Chose(LostBodyPart lostBodyPart) {
            super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
            this.lostBodyPart = lostBodyPart;
            if (lostBodyPart == null) return;
            this.name = lostBodyPart.name;
            this.rawDescription = lostBodyPart.description;
            initializeDescription();
        }

        @Override
        public void use(final AbstractPlayer p, final AbstractMonster m) {
            this.onChoseThisOption();
        }

        @Override
        public void onChoseThisOption() {
            if (lostBodyPart != null)
                addToBot_applyPower(lostBodyPart);
        }

        @Override
        public void upgradeAuto() {
        }
    }
}
