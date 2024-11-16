package superstitio.cards.lupa.PowerCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.cardModifier.modifiers.card.InsideEjaculationTag;
import superstitio.cardModifier.modifiers.card.OutsideEjaculationTag;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.TempCard.GangBang;
import superstitio.cards.lupa.LupaCard;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;

import static superstitio.orbs.orbgroup.SexMarkOrbGroup.addToBot_GiveMarkToOrbGroup;


public class GangBangPrepare extends LupaCard {
    public static final String ID = DataManager.MakeTextID(GangBangPrepare.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
//    private static final int COST_UPGRADED_NEW = 2;

    private static final int MAGIC = 15;
    private static final int UPGRADE_MAGIC = 5;

    public GangBangPrepare() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.cardsToPreview = new GangBang();
    }

    public static void addToBot_gainSexMark_Inside(String sexName) {
        addToBot_GiveMarkToOrbGroup(sexName, SexMarkOrbGroup.SexMarkType.Inside);
    }

    public static void addToBot_gainSexMark_Outside(String sexName) {
        addToBot_GiveMarkToOrbGroup(sexName, SexMarkOrbGroup.SexMarkType.OutSide);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new GangBangPreparePower(this.magicNumber));
        SexMarkOrbGroup sexMarkOrbGroup = InBattleDataManager.getSexMarkOrbGroup().orElse(null);
        if (sexMarkOrbGroup != null) {
            sexMarkOrbGroup.setScoreRate(sexMarkOrbGroup.scoreRate + (double) this.magicNumber / 100);
            return;
        }

        superstitioapi.renderManager.inBattleManager.InBattleDataManager.Subscribe(new SexMarkOrbGroup(AbstractDungeon.player.hb,
                (double) this.magicNumber / 100));
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }

    public static class GangBangPreparePower extends EasyBuildAbstractPowerForPowerCard {

        public GangBangPreparePower(int scoreRateForShow) {
            super(scoreRateForShow);
        }


        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {
            if (card instanceof FuckJob_Card && card instanceof SuperstitioCard) {
                if (card.hasTag(InsideEjaculationTag.getInsideEjaculationTag())) {
                    addToBot_gainSexMark_Inside(card.name);
                    return;
                }
                if (card.hasTag(OutsideEjaculationTag.getOutsideEjaculationTag())) {
                    addToBot_gainSexMark_Outside(((SuperstitioCard) card).cardStrings.getEXTENDED_DESCRIPTION(0));
                }
            }
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(amount);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new GangBangPrepare();
        }
    }
}

