package superstitio.cards.general.SkillCard.hangUpCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitioapi.hangUpCard.CardOrb;
import superstitioapi.hangUpCard.Card_TriggerHangCardManually;

import static superstitioapi.InBattleDataManager.getHangUpCardOrbGroup;
import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;

public class OneMoreHour extends GeneralCard implements Card_TriggerHangCardManually {
    public static final String ID = DataManager.MakeTextID(OneMoreHour.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int BLOCK = 7;
    private static final int UPGRADE_BLOCK = 3;
    private static final int MAGIC = 2;
    private static final int UPGRADE_MAGIC = 1;

    public OneMoreHour() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        addToBot_gainBlock();
        for (int i = 0; i < this.magicNumber; i++) {
            addToBotAbstract(() -> getHangUpCardOrbGroup()
                    .ifPresent(cardGroup -> cardGroup.cards
                            .forEach(orb -> orb.OrbCounter *= this.magicNumber)));
        }
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public boolean forceFilterCardOrbToHoveredMode(CardOrb orb) {
        orb.targetType = CardOrb.HangOnTarget.None;
        orb.actionType = CardOrb.HangEffectType.Good;
        return true;
    }

    @Override
    public int forceChangeOrbCounterShown(CardOrb orb) {
        return orb.OrbCounter * this.magicNumber;
    }
}
