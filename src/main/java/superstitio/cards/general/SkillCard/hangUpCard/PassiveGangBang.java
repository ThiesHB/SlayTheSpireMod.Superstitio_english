package superstitio.cards.general.SkillCard.hangUpCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitio.orbs.CardOrb_CardTrigger;
import superstitio.orbs.Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb;

import static superstitio.InBattleDataManager.getHangUpCardOrbGroup;
import static superstitio.actions.AutoDoneInstantAction.addToBotAbstract;

public class PassiveGangBang extends GeneralCard implements Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb {
    public static final String ID = DataManager.MakeTextID(PassiveGangBang.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int BLOCK = 16;
    private static final int UPGRADE_BLOCK = 5;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 2;

    public PassiveGangBang() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        AbstractCard self = this;
        for (int i = 0; i < this.magicNumber; i++) {
            addToBotAbstract(() -> getHangUpCardOrbGroup()
                    .ifPresent(cardGroup -> cardGroup.orbs.stream()
                            .filter(orb -> orb instanceof CardOrb_CardTrigger)
                            .forEach(orb -> ((CardOrb_CardTrigger) orb).forceAcceptAction(self))));
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
        return Math.max(orb.OrbCounter - this.magicNumber, 0);
    }
}
