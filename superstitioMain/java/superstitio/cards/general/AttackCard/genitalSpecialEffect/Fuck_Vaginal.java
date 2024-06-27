package superstitio.cards.general.AttackCard.genitalSpecialEffect;


import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.FuckJob_Card;
import superstitio.cards.general.GeneralCard;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.hangUpCard.*;

public class Fuck_Vaginal extends GeneralCard implements FuckJob_Card, Card_TriggerHangCardManually {
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
        FuckJob_Card.initFuckJobCard(this);
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
        this.setupMagicNumber(MAGIC);
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
        addToBot_dealDamage(monster, DamageActionMaker.DamageEffect.HeartMultiInOne);
        for (int i = 0; i < this.magicNumber; i++) {
            HangUpCardGroup.forEachHangUpCard(orb -> {
                if (orb instanceof ICardOrb_EachTime) {
                    orb.OrbCounter++;
                    orb.forceAcceptAction(this);
                }
                if (orb instanceof ICardOrb_WaitTime)
                    orb.forceAcceptAction(this);
            }).addToBotAsAbstractAction();
        }
    }

    @Override
    public boolean forceFilterCardOrbToHoveredMode(CardOrb orb) {
        if (orb instanceof ICardOrb_EachTime) return true;
        if (orb instanceof ICardOrb_WaitTime && orb instanceof CardOrb_CardTrigger)
            return ((CardOrb_CardTrigger) orb).cardMatcher.test(this);
        return false;
    }

    @Override
    public int forceChangeOrbCounterShown(CardOrb orb) {
        if (orb instanceof ICardOrb_WaitTime && orb instanceof CardOrb_CardTrigger) {
            if (((CardOrb_CardTrigger) orb).cardMatcher.test(this)) {
                return orb.OrbCounter - 1;
            }
        }
        return orb.OrbCounter;
    }
}
