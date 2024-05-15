package superstitio.cards.lupa.PowerCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.utils.ActionUtility;
import superstitio.utils.CardUtility;

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
        addToBot_applyPower(new Ku_KoroPower(player));
    }

    @Override
    public void upgradeAuto() {
        upgradeBaseCost(COST_UPGRADED_NEW);
    }

    /**
     * 每次受到攻击伤害时，获得1随机状态牌。所有消耗牌会回到抽牌堆
     */
    public static class Ku_KoroPower extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(Ku_KoroPower.class);

        public Ku_KoroPower(final AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }

        @Override
        public void onExhaust(AbstractCard card) {
            ActionUtility.addToBot_makeTempCardInBattle(card, BattleCardPlace.Discard);
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (damageAmount > 0 && info.owner != AbstractDungeon.player && info.type == DamageInfo.DamageType.NORMAL) {
                this.flash();
                final AbstractCard card = CardUtility.getRandomCurseCard(true, true);
                ActionUtility.addToBot_makeTempCardInBattle(card, BattleCardPlace.Hand);
            }

            return super.onAttacked(info, damageAmount);
        }

        @Override
        public void updateDescriptionArgs() {

        }
    }
}
