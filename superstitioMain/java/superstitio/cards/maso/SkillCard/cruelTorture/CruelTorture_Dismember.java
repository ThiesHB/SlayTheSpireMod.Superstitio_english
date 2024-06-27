package superstitio.cards.maso.SkillCard.cruelTorture;

import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.card.CruelTortureTag;
import superstitio.cards.maso.MasoCard;
import superstitio.delayHpLose.RemoveDelayHpLoseBlock;
import superstitioapi.hangUpCard.CardOrb;
import superstitioapi.hangUpCard.Card_TriggerHangCardManually;
import superstitioapi.hangUpCard.HangUpCardGroup;

//凌迟
public class CruelTorture_Dismember extends MasoCard implements Card_TriggerHangCardManually {
    public static final String ID = DataManager.MakeTextID(CruelTorture_Dismember.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 1;
    private static final int BLOCK = 3;
    private static final int UPGRADE_BLOCK = 1;


    public CruelTorture_Dismember() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new RemoveDelayHpLoseBlock());
        CardModifierManager.addModifier(this, new CruelTortureTag());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        HangUpCardGroup.forEachHangUpCard(orb -> {
            addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber));
            addToBot_gainBlock();
        }).addToBotAsAbstractAction();
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public boolean forceFilterCardOrbToHoveredMode(CardOrb orb) {
        orb.targetType = CardOrb.HangOnTarget.Self;
        orb.actionType = CardOrb.HangEffectType.Good;
        return true;
    }

    @Override
    public int forceChangeOrbCounterShown(CardOrb orb) {
        return 0;
    }
}

