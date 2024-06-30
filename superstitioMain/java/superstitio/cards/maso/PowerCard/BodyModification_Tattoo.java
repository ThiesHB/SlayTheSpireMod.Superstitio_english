package superstitio.cards.maso.PowerCard;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
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
import superstitio.powers.SexualHeat;
import superstitioapi.SuperstitioApiSubscriber;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.utils.ListUtility;
import superstitioapi.utils.PowerUtility;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class BodyModification_Tattoo extends MasoCard {
    public static final String ID = DataManager.MakeTextID(BodyModification_Tattoo.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;
    public static final String[] TattooNames = getCardStringsWithSFWAndFlavor(BodyModification_Tattoo.ID).getEXTENDED_DESCRIPTION()[2].split(",");

    private static final int COST = 2;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;

    public BodyModification_Tattoo() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new BodyModificationTag());
    }

    private static String getRandomName() {
        return ListUtility.getRandomFromList(TattooNames, AbstractDungeon.cardRandomRng);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        IntStream.range(0, this.magicNumber).forEach(i -> addToBot_applyPower(new BodyModification_TattooPower(1, getRandomName())));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class BodyModification_TattooPower extends EasyBuildAbstractPowerForPowerCard implements SuperstitioApiSubscriber.AtManualDiscardSubscriber.AtManualDiscardPower {

        public Map<String, Integer> tattooNames;

        public BodyModification_TattooPower(int amount, String name) {
            super(amount, false);
            this.tattooNames = new HashMap<>();
            this.tattooNames.put(name, amount);
            updateDescription();
        }

        @Override
        public void onExhaust(AbstractCard card) {
            SexualHeat.useConsumer_addSexualHeat(this.owner, this.amount, AutoDoneInstantAction::addToTopAbstract);
        }

        @Override
        public void atManualDiscard() {
            SexualHeat.useConsumer_addSexualHeat(this.owner, this.amount, AutoDoneInstantAction::addToTopAbstract);
        }


        @Override
        protected String getDesc() {
            return super.getDesc() + powerCard.getEXTENDED_DESCRIPTION()[0];
        }

        @Override
        public void updateDescriptionArgs() {
            final String[] strings = {""};
            this.tattooNames.forEach((name, num) -> strings[0] += String.format(powerCard.getEXTENDED_DESCRIPTION()[1], name, num));
            setDescriptionArgs(this.amount, strings[0]);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new BodyModification_Tattoo();
        }


        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source) {
            if (!(power instanceof BodyModification_TattooPower)) {
                return;
            }
            this.tattooNames = PowerUtility.mergeAndSumMaps(this.tattooNames, ((BodyModification_TattooPower) power).tattooNames);
            updateDescription();
        }
    }
}
