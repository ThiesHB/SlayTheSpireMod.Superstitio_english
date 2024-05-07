package superstitio.cards.lupa.BaseCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.orbs.CardOrb_WaitCardTrigger;
import superstitio.orbs.orbgroup.HangUpCardGroup;
import superstitio.powers.SexualHeat;

public class Masturbate extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(Masturbate.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.BASIC;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 4;
    private static final int UPGRADE_MAGIC = 2;
    private static final int DRAWCard = 1;

    private static final int WAIT = 2;


    public Masturbate() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET, "base");
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_applyPower(new SexualHeat(player, this.magicNumber));
        HangUpCardGroup.addToBot_AddCardOrbToOrbGroup(
                new CardOrb_WaitCardTrigger(this.makeCopy(), (orb, playedCard) -> {
                    orb.card.flash();
                    orb.StartHitCreature(AbstractDungeon.player);
                    addToBot_drawCards(DRAWCard);
                }, WAIT));
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void initializeDescription() {
        super.initializeDescription();
    }
}
