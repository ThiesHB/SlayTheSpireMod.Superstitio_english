package superstitio.cards.maso.SkillCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.SexBlock;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock;
import superstitio.orbs.CardOrb_CardTrigger;
import superstitio.orbs.Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb;

import static superstitio.InBattleDataManager.getHangUpCardOrbGroup;
import static superstitio.actions.AutoDoneInstantAction.addToBotAbstract;

public class OneMoreHour extends MasoCard implements Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb {
    public static final String ID = DataManager.MakeTextID(OneMoreHour.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 6;
    private static final int UPGRADE_BLOCK = 2;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;

    public OneMoreHour() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new DelayRemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        for (int i = 0; i < this.magicNumber; i++) {
            addToBotAbstract(() -> getHangUpCardOrbGroup()
                    .ifPresent(cardGroup -> cardGroup.orbs.stream()
                            .filter(orb -> orb instanceof CardOrb_CardTrigger)
                            .forEach(orb -> ((CardOrb_CardTrigger) orb).OrbCounter += this.magicNumber)));
        }
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public boolean forceFilterCardOrbToHoveredMode(CardOrb_CardTrigger orb) {
        return true;
    }

    @Override
    public int forceChangeOrbCounterShown(CardOrb_CardTrigger orb) {
        return orb.OrbCounter + this.magicNumber;
    }
}
