package superstitio.cards.lupa.AttackCard;

import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitio.DataManager;
import superstitio.cards.lupa.LupaCard;
import superstitio.cards.lupa.OnAddSemenPower;
import superstitio.powers.AbstractSuperstitioPower;
import superstitio.powers.AllCardCostModifier_PerEnergy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        List<AbstractCard> abstractCardStream =
                AbstractDungeon.player.drawPile.group.stream().filter(card -> card.type == CardType.ATTACK)
                        .collect(Collectors.toList());
        int attackCardCount = abstractCardStream.size();
        int semenCount = getTotalSemenValue() / this.magicNumber;
        for (int i = 0; i < Math.min(semenCount, attackCardCount); i++) {
            continuePlayCard(abstractCardStream.get(i));
        }
    }

    public void continuePlayCard(AbstractCard card) {
        if (!hasEnoughSemen(this.magicNumber)) return;
        useSemen(this.magicNumber);
        addToBot(new NewQueueCardAction(card, true, false, true));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return super.canUse(p, m) && hasEnoughSemen(this.magicNumber);
    }

    @Override
    public void upgradeAuto() {
    }

}
