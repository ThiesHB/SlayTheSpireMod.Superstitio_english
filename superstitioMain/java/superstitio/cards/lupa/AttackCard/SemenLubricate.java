package superstitio.cards.lupa.AttackCard;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.SuperstitioImg;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.lupa.LupaCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.utils.CardUtility;

import java.util.Optional;

import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;


public class SemenLubricate extends LupaCard {
    public static final String ID = DataManager.MakeTextID(SemenLubricate.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 3;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 4;

    private static final int MAGIC = 3;
    private boolean inPlayingCard = false;

    public SemenLubricate() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster);
        inPlayingCard = true;
        addToBotAbstract(this::continuePlayCard);
    }

    public void continuePlayCard() {
        if (!inPlayingCard) return;
        if (!hasEnoughSemen(this.magicNumber)) {
            inPlayingCard = false;
            return;
        }
        if (CardUtility.isNotInBattle()) {
            inPlayingCard = false;
            return;
        }
        Optional<AbstractCard> attackCard = AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card.type == CardType.ATTACK)
                .filter(card -> !(card instanceof SemenLubricate))
//                .filter(card -> card != notCard)
                .filter(card -> card.canUse(AbstractDungeon.player, null))
                .findFirst();
        if (!attackCard.isPresent()) {
            inPlayingCard = false;
            return;
        }
        addToBot_useSemenAndAutoRemove(this.magicNumber);
        AutoDoneInstantAction.addToBotAbstract(() -> AbstractDungeon.player.drawPile.group.remove(attackCard.get()));
        addToBot_applyPower(new ContinuePlayCardPower(this));
        addToBot(new NewQueueCardAction(attackCard.get(), true, false, true));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber);
    }

    @Override
    public void upgradeAuto() {
    }

    @SuperstitioImg.NoNeedImg
    private static class ContinuePlayCardPower extends EasyBuildAbstractPowerForPowerCard implements InvisiblePower {
        private final SemenLubricate semenLubricate;

        public ContinuePlayCardPower(AbstractCard card) {
            super(-1);
            if (card instanceof SemenLubricate)
                semenLubricate = (SemenLubricate) card;
            else
                semenLubricate = null;
        }

        @Override
        public void onAfterCardPlayed(AbstractCard usedCard) {
            if (!(this.owner instanceof AbstractPlayer)) return;
            if (semenLubricate == null || !semenLubricate.inPlayingCard) {
                addToBot_removeSpecificPower(this);
                return;
            }
            addToBotAbstract(semenLubricate::continuePlayCard);
        }

        @Override
        public void updateDescriptionArgs() {
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new SemenLubricate();
        }
    }

}
