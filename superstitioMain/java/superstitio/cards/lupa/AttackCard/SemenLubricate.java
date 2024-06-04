package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.utils.CardUtility;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class SemenLubricate extends LupaCard {
    public static final String ID = DataManager.MakeTextID(SemenLubricate.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 3;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 4;

    private static final int MAGIC = 3;

    public SemenLubricate() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_dealDamage(monster);
        AutoDoneInstantAction.addToBotAbstract(this::continuePlayCard);
    }

    public void continuePlayCard() {
        if (!hasEnoughSemen(this.magicNumber)) return;
        if (CardUtility.isNotInBattle()) return;
        Optional<AbstractCard> attackCard = AbstractDungeon.player.drawPile.group.stream()
                .filter(card -> card.type == CardType.ATTACK)
                .filter(card -> !(card instanceof SemenLubricate))
                .findFirst();
        if (!attackCard.isPresent())return;
        addToBot_useSemenAndAutoRemove(this.magicNumber);
        addToBot(new NewQueueCardAction(attackCard.get(), true, false, true));
        AutoDoneInstantAction.addToBotAbstract(this::continuePlayCard);
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber);
    }

    @Override
    public void upgradeAuto() {
    }

}
