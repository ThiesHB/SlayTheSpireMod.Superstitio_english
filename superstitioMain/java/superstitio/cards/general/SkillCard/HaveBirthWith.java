package superstitio.cards.general.SkillCard;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.PregnantBlock_newMonster;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.general.TempCard.GiveBirth;
import superstitio.monster.ChibiKindMonster;
import superstitioapi.utils.ActionUtility;

import static superstitioapi.utils.ActionUtility.addToBot_makeTempCardInBattle;
import static superstitioapi.utils.CardUtility.getSelfOrEnemyTarget;

//@AutoAdd.Ignore
public class HaveBirthWith extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(HaveBirthWith.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 1;
    private static final int BLOCK = 10;
    private static final int UPGRADE_BLOCK = 3;

    public HaveBirthWith() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new PregnantBlock_newMonster().removeAutoBind());
        this.cardsToPreview = new GiveBirth();
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = getSelfOrEnemyTarget(this, monster);
        if (target instanceof AbstractPlayer)
            ForPlayer(AbstractDungeon.player);
        else
            ForMonster((AbstractMonster) target);
        addToBot_makeTempCardInBattle(new GiveBirth(), ActionUtility.BattleCardPlace.Discard, upgraded);
    }

    private void ForPlayer(AbstractPlayer player) {
        addToBot_gainCustomBlock(new PregnantBlock_newMonster(
                new ChibiKindMonster(), new ChibiKindMonster.MinionChibi(new ChibiKindMonster())));
    }

    private void ForMonster(AbstractMonster monster) {
        addToBot_gainCustomBlock(new PregnantBlock_newMonster(monster));
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }
}
