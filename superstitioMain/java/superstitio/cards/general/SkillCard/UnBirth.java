package superstitio.cards.general.SkillCard;

import com.evacipated.cardcrawl.mod.stslib.blockmods.BlockModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.PregnantBlock_sealPower;
import superstitio.cards.general.GeneralCard;
import superstitio.cards.general.TempCard.GiveBirth;
import superstitio.cards.general.TempCard.SelfReference;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.utils.ActionUtility;

import java.util.ArrayList;

import static superstitioapi.utils.ActionUtility.addToBot_makeTempCardInBattle;


//TODO 增加一个按照怪物体型获得格挡的效果
public class UnBirth extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(UnBirth.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 1;
    private static final int BLOCK = 11;
    private static final int UPGRADE_BLOCK = 3;

    public UnBirth() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new PregnantBlock_sealPower(new ArrayList<>(), null).removeAutoBind());
        this.cardsToPreview = new GiveBirth();
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null || target instanceof AbstractPlayer)
            ForPlayer(AbstractDungeon.player);
        else if (BlockModifierManager.blockInstances(target).stream()
                .anyMatch(blockInstance -> blockInstance.getBlockTypes().stream()
                        .filter(blockModifier -> blockModifier instanceof PregnantBlock_sealPower)
                        .anyMatch(blockModifier -> ((PregnantBlock_sealPower) blockModifier).sealCreature == monster)))
            ForMonsterBrokenSpaceStructure((AbstractMonster) target);
        else
            ForMonster((AbstractMonster) target);
    }

    private void ForPlayer(AbstractPlayer player) {
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
        player.powers.forEach(power -> {
            if ((power.type == AbstractPower.PowerType.DEBUFF || power instanceof ArtifactPower)
                    && power instanceof InvisiblePower) {
                power.owner = AbstractDungeon.player;
                power.amount = power.amount * 2;
                sealPower.add(power);
                AutoDoneInstantAction.addToBotAbstract(() -> player.powers.remove(power));
            }
        });
        addToBot_gainCustomBlock(new PregnantBlock_sealPower(sealPower, player));
        this.exhaust = true;
        addToBot_makeTempCardInBattle(new SelfReference(), ActionUtility.BattleCardPlace.Hand, upgraded);

    }

    private void ForMonsterBrokenSpaceStructure(AbstractMonster monster) {
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
        monster.powers.forEach(power -> {
            if ((power.type == AbstractPower.PowerType.DEBUFF || power instanceof ArtifactPower)
                    && power instanceof InvisiblePower) {
                power.owner = AbstractDungeon.player;
                power.amount = power.amount * 2;
                sealPower.add(power);
                AutoDoneInstantAction.addToBotAbstract(() -> monster.powers.remove(power));
            }
        });
        addToBot_gainCustomBlock(new PregnantBlock_sealPower(sealPower, monster));
        this.exhaust = true;
        addToBot_makeTempCardInBattle(new SelfReference(), ActionUtility.BattleCardPlace.Hand, upgraded);
    }

    private void ForMonster(AbstractMonster monster) {
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
        monster.powers.forEach(power -> {
            if ((power.type == AbstractPower.PowerType.DEBUFF || power instanceof ArtifactPower)
                    && power instanceof InvisiblePower) {
                power.owner = AbstractDungeon.player;
                power.amount = power.amount * 2;
                sealPower.add(power);
                AutoDoneInstantAction.addToBotAbstract(() -> monster.powers.remove(power));
            }
        });
        addToBot_gainCustomBlock(new PregnantBlock_sealPower(sealPower, monster));
        addToBot_makeTempCardInBattle(new GiveBirth(), ActionUtility.BattleCardPlace.Discard, upgraded);
    }

    @Override
    public void upgradeAuto() {
        upgradeCardsToPreview();
    }

    enum MonsterBodyType {
        Tiny,
        Small,
        Middle,
        Big,
        VeryBig
    }

//    MonsterBodyType getMonsterBodyType(AbstractCreature creature){
//        if (creature.maxHealth < 10)
//            return MonsterBodyType.Tiny;
//        if (creature.maxHealth < 20)
//            return MonsterBodyType.Middle
//
//    }
}
