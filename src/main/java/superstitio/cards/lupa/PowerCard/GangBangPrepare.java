package superstitio.cards.lupa.PowerCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.InBattleDataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.TempCard.GangBang;
import superstitio.cards.lupa.LupaCard;
import superstitio.orbs.orbgroup.SexMarkOrbGroup;
import superstitio.powers.AbstractSuperstitioPower;

import static superstitio.cards.general.FuckJob_Card.addToTop_gainSexMark_Inside;
import static superstitio.cards.general.FuckJob_Card.addToTop_gainSexMark_Outside;


public class GangBangPrepare extends LupaCard {
    public static final String ID = DataManager.MakeTextID(GangBangPrepare.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 3;

    private static final int MAGIC = 6;
    private static final int UPGRADE_MAGIC = -2;

    public GangBangPrepare() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        this.cardsToPreview = new GangBang();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new GangBangPreparePower(AbstractDungeon.player));
        SexMarkOrbGroup sexMarkOrbGroup = InBattleDataManager.getSexMarkOrbGroup().orElse(null);
        if (sexMarkOrbGroup != null) return;

        InBattleDataManager.Subscribe(new SexMarkOrbGroup(AbstractDungeon.player.hb));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class GangBangPreparePower extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(GangBangPreparePower.class);

        public GangBangPreparePower(final AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }


        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {
            if (card instanceof FuckJob_Card && card instanceof SuperstitioCard) {
                if (card.cardID.contains("Fuck_")) {
                    addToTop_gainSexMark_Inside(card.name);
                    return;
                }
                if (card.cardID.contains("Job_")) {
                    addToTop_gainSexMark_Outside(((SuperstitioCard) card).getEXTENDED_DESCRIPTION()[0]);
                }
            }
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}

