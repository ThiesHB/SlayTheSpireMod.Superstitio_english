package superstitio.cards.general.SkillCard.drawCard;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.hangUpCard.CardOrb;
import superstitio.hangUpCard.CardOrb_CardTrigger;
import superstitio.orbs.Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static superstitio.InBattleDataManager.getHangUpCardOrbGroup;
import static superstitio.actions.AutoDoneInstantAction.addToBotAbstract;

public class CalmDown extends GeneralCard implements Card_AvoidAllCardUsedCheckOfCardOrb_ManuallyTriggerCardOrb {
    public static final String ID = DataManager.MakeTextID(CalmDown.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 0;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;
    private static final int ExtraDrawNum = 1;


    private static final int HeatReduce = 6;


    public CalmDown() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        getHangUpCardOrbGroup().ifPresent(cardGroup -> {
            cardGroup.cards.forEach(cardOrb -> {
                        addToBotAbstract(() -> cardGroup.removeCard(cardOrb));
                        addToBot_drawCards();
                    });
            addToBot_drawCards(cardGroup.cards.size());
        });
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
        return 0;
    }
}

