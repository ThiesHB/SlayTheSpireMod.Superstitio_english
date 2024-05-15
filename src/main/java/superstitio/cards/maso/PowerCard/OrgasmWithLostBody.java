package superstitio.cards.maso.PowerCard;

import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.TempCard.FeelPhantomBody;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.AbstractSuperstitioPower;

import static superstitio.utils.ActionUtility.addToBot_makeTempCardInBattle;


public class OrgasmWithLostBody extends MasoCard {
    public static final String ID = DataManager.MakeTextID(OrgasmWithLostBody.class);

    public static final CardType CARD_TYPE = CardType.POWER;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;

    public OrgasmWithLostBody() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.cardsToPreview = new FeelPhantomBody();
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new OrgasmWithLostBodyPower(player));
    }

    @Override
    public void upgradeAuto() {
        this.upgradeCardsToPreview();
    }

    public static class OrgasmWithLostBodyPower extends AbstractSuperstitioPower {
        public static final String POWER_ID = DataManager.MakeTextID(OrgasmWithLostBodyPower.class);

        public OrgasmWithLostBodyPower(final AbstractCreature owner) {
            super(POWER_ID, owner, -1);
        }


        @Override
        public void onCardDraw(AbstractCard card) {
            super.onCardDraw(card);
            if (card.canUse(AbstractDungeon.player, null)) return;
            //不是因为能量不够或者对象不对而无法打出
            if (!(card.cardPlayable(null) && card.hasEnoughEnergy())) return;

            addToBot(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
            addToBot_makeTempCardInBattle(new FeelPhantomBody(), BattleCardPlace.Hand);
        }

        @Override
        public void updateDescriptionArgs() {
        }
    }
}

