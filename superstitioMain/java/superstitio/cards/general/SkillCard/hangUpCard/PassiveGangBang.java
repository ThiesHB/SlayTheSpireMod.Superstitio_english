package superstitio.cards.general.SkillCard.hangUpCard;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitioapi.hangUpCard.CardOrb;
import superstitioapi.hangUpCard.CardOrb_CardTrigger;
import superstitioapi.hangUpCard.Card_TriggerHangCardManually;

import static superstitioapi.InBattleDataManager.getHangUpCardOrbGroup;
import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;

public class PassiveGangBang extends GeneralCard implements Card_TriggerHangCardManually {
    public static final String ID = DataManager.MakeTextID(PassiveGangBang.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.RARE;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 2;
    private static final int BLOCK = 14;
    private static final int UPGRADE_BLOCK = 4;
    private static final int MAGIC = 3;
    private static final int UPGRADE_MAGIC = 1;

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
                    .ifPresent(cardGroup -> cardGroup.cards.stream()
                            .filter(orb -> orb instanceof CardOrb_CardTrigger)
                            .forEach(orb -> orb.forceAcceptAction(self))));
        }
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public boolean forceFilterCardOrbToHoveredMode(CardOrb orb) {
//        return orb instanceof CardOrb_CardTrigger;
        return true;
    }

    @Override
    public int forceChangeOrbCounterShown(CardOrb orb) {
//        if (orb instanceof CardOrb_CardTrigger)
        return Math.max(orb.OrbCounter - this.magicNumber, 0);
//        return orb.OrbCounter;
    }
}
