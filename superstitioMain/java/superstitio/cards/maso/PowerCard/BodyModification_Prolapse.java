package superstitio.cards.maso.PowerCard;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.card.BodyModificationTag;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitioapi.hangUpCard.CardOrb;
import superstitioapi.hangUpCard.HangUpCardGroup;
import superstitioapi.utils.ListUtility;
import superstitioapi.utils.PowerUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class BodyModification_Prolapse extends MasoCard {
    public static final String ID = DataManager.MakeTextID(BodyModification_Prolapse.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;
    //    private static final int UPGRADE_MAGIC = 1;
    public static final String[] ProlapseNames = getCardStringsWithSFWAndFlavor(BodyModification_Prolapse.ID).getEXTENDED_DESCRIPTION(2).split(",");
    private static final int COST = 1;
    //    private static final int COST_UPGRADED_NEW = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public BodyModification_Prolapse() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new BodyModificationTag());
        initializeDescription();
    }

    private static String getRandomName() {
        return ListUtility.getRandomFromList(ProlapseNames, AbstractDungeon.cardRandomRng);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs((int) Math.pow(2, this.magicNumber));
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        IntStream.range(0, this.magicNumber).forEach(i ->
                addToBot_applyPower(new BodyModification_ProlapsePower(1, getRandomName())));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class BodyModification_ProlapsePower extends EasyBuildAbstractPowerForPowerCard {

        public Map<String, Integer> prolapseNames;

        public BodyModification_ProlapsePower(int amount, String name) {
            super(amount, false);
            this.prolapseNames = new HashMap<>();
            this.prolapseNames.put(name, amount);
            updateDescription();
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (info.type != DamageInfo.DamageType.NORMAL) return damageAmount;
            if (!AbstractDungeon.player.hand.isEmpty()) {
                AbstractCard card = AbstractDungeon.player.hand.getRandomCard(AbstractDungeon.cardRandomRng);
                AbstractDungeon.player.hand.moveToDiscardPile(card);
                card.triggerOnManualDiscard();
                GameActionManager.incrementDiscard(false);
                return (int) ((double) damageAmount / Math.pow(2, this.amount));
            } else {
                ArrayList<CardOrb> cards = new ArrayList<>();
                HangUpCardGroup.forEachHangUpCard(cardOrb -> {
                    if (!cardOrb.ifShouldRemove())
                        cards.add(cardOrb);
                }).get();
                if (cards.isEmpty()) return damageAmount;
                CardOrb cardOrb = ListUtility.getRandomFromList(cards, AbstractDungeon.cardRandomRng);
                cardOrb.cardGroupReturnAfterEvoke = AbstractDungeon.player.discardPile;
                cardOrb.setTriggerDiscardIfMoveToDiscard();
                cardOrb.setShouldRemove();
                return (int) ((double) damageAmount / Math.pow(2, this.amount));
            }
        }

        @Override
        public void updateDescriptionArgs() {
            final String[] strings = {""};
            this.prolapseNames.forEach((name, num) -> strings[0] += String.format(powerCard.cardStrings.getEXTENDED_DESCRIPTION(1), name, num));
            setDescriptionArgs((int) Math.pow(2, this.amount), strings[0]);
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (!(power instanceof BodyModification_ProlapsePower)) {
                return;
            }
            this.prolapseNames = PowerUtility.mergeAndSumMaps(this.prolapseNames, ((BodyModification_ProlapsePower) power).prolapseNames);
            updateDescription();
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new BodyModification_Prolapse();
        }

        @Override
        protected String getDesc() {
            return super.getDesc() + powerCard.cardStrings.getEXTENDED_DESCRIPTION(0);
        }
    }
}
