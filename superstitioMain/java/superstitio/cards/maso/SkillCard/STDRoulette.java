package superstitio.cards.maso.SkillCard;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.SexualDamage;
import superstitioapi.utils.CreatureUtility;
import superstitioapi.utils.PowerUtility;

public class STDRoulette extends MasoCard {
    public static final String ID = DataManager.MakeTextID(STDRoulette.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int BLOCK = 16;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 2;

    public STDRoulette() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, new DelayRemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        addToBot_applyPower(new STDRoulettePower(this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class STDRoulettePower extends EasyBuildAbstractPowerForPowerCard {

        public STDRoulettePower(int amount) {
            super(amount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        @Override
        public String getDescriptionStrings() {
            return powerCard.cardStrings.getEXTENDED_DESCRIPTION(0);
        }

        @Override
        public void atEndOfRound() {
            addToBot_removeSpecificPower(this);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount) {
            if (info.type == DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType) return;
            for (AbstractMonster monster : CreatureUtility.getAllAliveMonsters()) {
                addToBot_applyPower(new SexualDamage(monster, this.amount, this.owner));
            }
            if (AbstractDungeon.cardRandomRng.randomBoolean()) {
                PowerUtility.BubbleMessage(this.owner.hb, false, this.powerCard.cardStrings.getEXTENDED_DESCRIPTION(1));
            } else {
                PowerUtility.BubbleMessage(this.owner.hb, false, this.powerCard.cardStrings.getEXTENDED_DESCRIPTION(2));
            }
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new STDRoulette();
        }
    }
}
