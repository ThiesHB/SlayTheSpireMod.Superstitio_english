package superstitio.cards.lupa.PowerCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.CardUtility;

//咕杀/くっころ
public class Ku_Koro extends LupaCard {
    public static final String ID = DataManager.MakeTextID(Ku_Koro.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    private static final int COST_UPGRADED_NEW = 0;

    public Ku_Koro() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new Ku_KoroPower());
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW);
    }

    public static class Ku_KoroPower extends EasyBuildAbstractPowerForPowerCard {

        public Ku_KoroPower() {
            super(-1);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new Ku_Koro();
        }

        @Override
        public void onExhaust(AbstractCard card) {
            ActionUtility.addToBot_makeTempCardInBattle(card, ActionUtility.BattleCardPlace.Discard);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount) {
            if (damageAmount > 0 && info.owner != AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
                this.flash();
                final AbstractCard card = CardUtility.getRandomStatusCard(true, true);
                ActionUtility.addToBot_makeTempCardInBattle(card, ActionUtility.BattleCardPlace.Hand);
            }
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}
