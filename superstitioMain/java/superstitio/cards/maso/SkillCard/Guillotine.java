package superstitio.cards.maso.SkillCard;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.maso.MasoCard;
import superstitio.orbs.CardOrb_OnOrgasm_WaitTime;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitio.powers.sexualHeatNeedModifier.SexualHeatNeedModifier;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.cards.patch.GoSomewhereElseAfterUse;

import static superstitio.cards.CardOwnerPlayerManager.IsNotLupaCard;

public class Guillotine extends MasoCard implements FuckJob_Card, GoSomewhereElseAfterUse, IsNotLupaCard{
    public static final String ID = DataManager.MakeTextID(Guillotine.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    //    private static final int DAMAGE = 4;
//    private static final int UPGRADE_DAMAGE = 1;
    //    private static final int BLOCK = 8;
//    private static final int UPGRADE_BLOCK = 2;
    private static final int DAMAGE_PERCENT = 50;
    private static final int HeatNeedAdd = 10;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;

    public Guillotine() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        FuckJob_Card.initFuckJobCard(this);
//        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
//        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new GuillotinePower());
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(HeatNeedAdd, DAMAGE_PERCENT);
    }

    @Override
    public void afterInterruptMoveToCardGroup(CardGroup cardGroup) {
        new CardOrb_OnOrgasm_WaitTime(this, cardGroup, this.magicNumber, (orb) -> {
            orb.StartHitCreature(AbstractDungeon.player);
            DamageActionMaker.maker(99999, AbstractDungeon.player)
                    .setDamageType(DataManager.CanOnlyDamageDamageType.NoTriggerLupaAndMasoRelicHpLose)
                    .setEffect(AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                    .addToBot();
        })
                .setDiscardOnEndOfTurn()
                .setTargetType(CardTarget.SELF)
                .addToBot_HangCard();
    }

    public static class GuillotinePower extends EasyBuildAbstractPowerForPowerCard implements SexualHeatNeedModifier, NonStackablePower  {

        public GuillotinePower() {
            super(-1);
        }

        @Override
        public String getDescriptionStrings() {
            return powerCard.getEXTENDED_DESCRIPTION()[0];
        }

        @Override
        public int reduceSexualHeatNeeded() {
            return -HeatNeedAdd;
        }

        @Override
        public float atDamageGive(float damage, DamageInfo.DamageType type) {
            return super.atDamageGive(damage, type) * (1 + (float) DAMAGE_PERCENT / 100);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer) {
            addToBot_removeSpecificPower(this);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(HeatNeedAdd, DAMAGE_PERCENT);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new Guillotine();
        }
    }
}
