package superstitio.cards.lupa.SkillCard;

import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import superstitio.DataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.lupa.AbstractLupaCard;
import superstitio.cards.lupa.TempCard.GiveBirth;
import superstitio.cards.lupa.TempCard.SelfReference;
import superstitio.cards.modifiers.block.PregnantBlock;
import superstitio.cards.modifiers.block.PregnantBlock_loop;
import superstitio.cards.modifiers.block.PregnantBlock_sealPower;

import java.util.ArrayList;

import static superstitio.utils.ActionUtility.addToBot_makeTempCardInBattle;


//TODO 增加一个按照怪物体型获得格挡的效果
public class UnBirth extends AbstractLupaCard {
    public static final String ID = DataManager.MakeTextID(UnBirth.class.getSimpleName());

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 1;
    private static final int BLOCK = 8;
    private static final int UPGRADE_BLOCK = 3;

    public UnBirth() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupBlock(BLOCK, UPGRADE_BLOCK, new PregnantBlock().removeAutoBind());
        this.cardsToPreview = new GiveBirth();
        //this.exhaust = true;
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = SelfOrEnemyTargeting.getTarget(this);
        if (target == null)
            ForPlayer(AbstractDungeon.player);
        else
            ForMonster((AbstractMonster) target);


    }

    private void ForPlayer(AbstractPlayer player) {
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
//        monster.powers.forEach(power -> {
//            if (power.type == AbstractPower.PowerType.DEBUFF || power instanceof ArtifactPower) {
//                power.owner = AbstractDungeon.player;
//                sealPower.add(power);
//                AutoDoneInstantAction.addToBotAbstract(() -> monster.powers.remove(power));
//            }
//        });
        addToBot_gainBlock(new PregnantBlock_loop(sealPower, player));
        addToBot_makeTempCardInBattle(new SelfReference(), BattleCardPlace.Discard, upgraded);
    }

    private void ForMonster(AbstractMonster monster) {
        ArrayList<AbstractPower> sealPower = new ArrayList<>();
        monster.powers.forEach(power -> {
            if (power.type == AbstractPower.PowerType.DEBUFF || power instanceof ArtifactPower) {
                power.owner = AbstractDungeon.player;
                sealPower.add(power);
                AutoDoneInstantAction.addToBotAbstract(() -> monster.powers.remove(power));
            }
        });
        addToBot_gainBlock(new PregnantBlock_sealPower(sealPower, monster));
        addToBot_makeTempCardInBattle(new GiveBirth(), BattleCardPlace.Discard, upgraded);
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
}
