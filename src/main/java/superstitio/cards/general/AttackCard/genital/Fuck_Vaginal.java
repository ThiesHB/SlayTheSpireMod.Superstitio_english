package superstitio.cards.general.AttackCard.genital;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.general.AbstractCard_FuckJob;
import superstitio.orbs.CardOrb_CardTrigger;
import superstitio.orbs.CardOrb_EachCardTrigger;
import superstitio.orbs.CardOrb_WaitCardTrigger;
import superstitio.orbs.Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb;

import static superstitio.InBattleDataManager.getHangUpCardOrbGroup;

public class Fuck_Vaginal extends AbstractCard_FuckJob implements Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb {
    public static final String ID = DataManager.MakeTextID(Fuck_Vaginal.class);

    public static final CardType CARD_TYPE = CardType.ATTACK;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.ENEMY;

    private static final int COST = 1;
    private static final int DAMAGE = 8;
    private static final int UPGRADE_DAMAGE = 4;
    private static final int MAGIC = 1;

    public Fuck_Vaginal() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        addToBot_dealDamage(monster);
        for (int i = 0; i < this.magicNumber; i++) {
            AutoDoneInstantAction.addToBotAbstract(() -> getHangUpCardOrbGroup().ifPresent(orbGroup -> {
                orbGroup.forEachOrbInThisOrbGroup(CardOrb_EachCardTrigger.class, (orb) -> {
                    orb.OrbCounter++;
                    orb.forceAcceptAction(this);
                });
                orbGroup.forEachOrbInThisOrbGroup(CardOrb_WaitCardTrigger.class, (orb) -> orb.forceAcceptAction(this));
            }));
        }

    }

    @Override
    public boolean forceFilterCardOrbToHoveredMode(CardOrb_CardTrigger orb) {
        if (orb instanceof CardOrb_EachCardTrigger) return true;
        return orb.cardMatcher.test(this);
    }

    @Override
    public int forceChangeOrbCounterShown(CardOrb_CardTrigger orb) {
        if (orb instanceof CardOrb_EachCardTrigger) return orb.OrbCounter;
        return orb.OrbCounter - 1;
    }
}
