package superstitio.cards.lupa.PowerCard.defend;

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.SexBlock;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.lupa.LupaCard;
import superstitio.cards.lupa.OnAddSemenPower;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.SexualHeatNeededModifier;
import superstitio.powers.lupaOnly.SemenPower;


public class SemenTattoo extends LupaCard {
    public static final String ID = DataManager.MakeTextID(SemenTattoo.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;

    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;


    public SemenTattoo() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        BlockModifierManager.addModifier(this, new SexBlock());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new SemenTattooPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class SemenTattooPower extends EasyBuildAbstractPowerForPowerCard implements OnAddSemenPower {

        public SemenTattooPower(int amount) {
            super(amount);
        }

        @Override
        public void updateDescriptionArgs() {
            this.setDescriptionArgs(amount);
        }

        @Override
        public boolean onAddSemen_shouldApply(AbstractPower power) {
            if (!(power instanceof SemenPower)) return true;
            int blockAdd = this.amount * power.amount * ((SemenPower) power).getSemenValue();
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this.owner, blockAdd));
            return true;
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new SemenTattoo();
        }
    }
}
