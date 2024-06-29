package superstitio.cards.maso.PowerCard;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.card.BodyModificationTag;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.Milk;

public class BodyModification_SuperHugeBreast extends MasoCard {
    public static final String ID = DataManager.MakeTextID(BodyModification_SuperHugeBreast.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = 2;

    public BodyModification_SuperHugeBreast() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        CardModifierManager.addModifier(this, new BodyModificationTag());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new BodyModification_SuperHugeBreastPower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class BodyModification_SuperHugeBreastPower extends EasyBuildAbstractPowerForPowerCard {

        public BodyModification_SuperHugeBreastPower(int amount) {
            super(amount);
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (info.type != DamageInfo.DamageType.NORMAL) return damageAmount;
            addToBot_applyPower(new Milk(owner, this.amount));
            return damageAmount;
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new BodyModification_SuperHugeBreast();
        }
    }
}
