package superstitio.cards.maso.PowerCard;

import basemod.AutoAdd;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.watcher.ChooseOneAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.card.BodyModificationTag;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.AbstractTempCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.masoOnly.*;
import superstitioapi.utils.CardUtility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class BodyModification_CutOff extends MasoCard {
    public static final String ID = DataManager.MakeTextID(BodyModification_CutOff.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 15;
    private static final int UPGRADE_MAGIC = 5;
    private static final int BLOCK = 14;
    private static final int UPGRADE_BLOCK = 4;

    public BodyModification_CutOff() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        CardModifierManager.addModifier(this, new BodyModificationTag());
//        this.tags.add(DataManager.CardTagsType.BodyModification);
    }

//    @Override
//    public void updateDescriptionArgs() {
//        setDescriptionArgs(this.cardStrings.getEXTENDED_DESCRIPTION()[0], this.cardStrings.getEXTENDED_DESCRIPTION()[1]);
//    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        final ArrayList<AbstractCard> bodyParts = new ArrayList<>();
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Arm.POWER_ID))
            bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Arm()));
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Breast.POWER_ID))
            bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Breast()));
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Castration.POWER_ID))
            bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Castration()));
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Head.POWER_ID))
            bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Head()));
        if (!AbstractDungeon.player.hasPower(LostBodyPart_Leg.POWER_ID))
            bodyParts.add(new BodyModification_CutOff_Chose(new LostBodyPart_Leg()));
        if (bodyParts.isEmpty())
            return;
        addToBot_gainBlock();
        addToBot_applyPower(new BodyModification_CutOffPower(this.magicNumber));
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
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type) {
            return super.atDamageFinalGive(damage + damage * this.amount / PERCENTAGE, type);
        }

        @Override
        public float atDamageFinalGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
            return super.atDamageFinalGive(damage, type, card);
        }

        @Override
        protected String getDesc() {
            return powerCard.getEXTENDED_DESCRIPTION()[0];
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new BodyModification_CutOff();
        }
    }

    @AutoAdd.Ignore
    public static class BodyModification_CutOff_Chose extends AbstractTempCard implements HasMultiCardsToPreview {
        public static final String ID = DataManager.MakeTextID(BodyModification_CutOff_Chose.class);

        public static final CardType CARD_TYPE = CardType.POWER;

        public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

        public static final CardTarget CARD_TARGET = CardTarget.SELF;

        private static final int COST = -2;
        private final LostBodyPart lostBodyPart;
        private final List<AbstractCard> multiCardsToPreview = new ArrayList<>();
        private float cardsToPreviewTimer = 0.0f;

        public BodyModification_CutOff_Chose() {
            this(null);
        }

        public BodyModification_CutOff_Chose(LostBodyPart lostBodyPart) {
            super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
            this.lostBodyPart = lostBodyPart;
            if (lostBodyPart == null) return;
            this.name = lostBodyPart.name;
            this.rawDescription = lostBodyPart.description;
            CardUtility.AllCardInBattle().forEach(card -> {
                if (!(card instanceof FuckJob_Card)) return;
                if (!Arrays.stream(lostBodyPart.banedBodyPart()).collect(Collectors.toList()).contains(FuckJob_Card.getBodyPartType((FuckJob_Card) card)))
                    return;
                if (this.multiCardsToPreview.stream().anyMatch(cardInList -> Objects.equals(cardInList.cardID, card.cardID))) return;
                this.multiCardsToPreview.add(card.makeCopy());
            });
            initializeDescription();
        }

        @Override
        public void update() {
            super.update();
            HasMultiCardsToPreview.super.update();
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

        @Override
        public List<AbstractCard> getMultiCardsToPreview() {
            return this.multiCardsToPreview;
        }

        @Override
        public float getCardsToPreviewTimer() {
            return this.cardsToPreviewTimer;
        }

        @Override
        public void setCardsToPreviewTimer(float cardsToPreviewTimer) {
            this.cardsToPreviewTimer = cardsToPreviewTimer;
        }

        @Override
        public void setCardsToPreview(AbstractCard cardsToPreview) {
            this.cardsToPreview = cardsToPreview;
        }
    }
}
