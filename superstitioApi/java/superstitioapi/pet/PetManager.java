package superstitioapi.pet;

import basemod.interfaces.PostPowerApplySubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.TipTracker;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitioapi.InBattleDataManager;
import superstitioapi.SuperstitioApiSubscriber;
import superstitioapi.utils.RenderInBattle;

import static superstitioapi.actions.AutoDoneInstantAction.addToBotAbstract;

public class PetManager implements RenderInBattle, SuperstitioApiSubscriber.AtEndOfPlayerTurnPreCardSubscriber, PostPowerApplySubscriber {
    public final MinionGroup monsterGroup = new MinionGroup(new AbstractMonster[]{});

    public PetManager() {
        RenderInBattle.Register(RenderType.Normal, this);
    }

    public static float calculateSmartDistance(final AbstractCreature m1, final AbstractCreature m2) {
        return (m1.hb_w + m2.hb_w) / 2.0f;
    }

    public static AbstractMonster spawnMonster(final AbstractMonster monsterInstance) {
        final AbstractMonster monster = monsterInstance;
        if (!InBattleDataManager.getPetManager().isPresent()) return monster;
        final MonsterGroup roomMonsters =
                InBattleDataManager.getPetManager().get().monsterGroup;
//        AbstractDungeon.getMonsters();
        float monsterDX = Settings.WIDTH / 2.0f;
        float monsterDY = AbstractDungeon.player.hb.y;
        AbstractMonster lastMonster = null;
        if (!roomMonsters.monsters.isEmpty()) {
            lastMonster = roomMonsters.monsters.get(roomMonsters.monsters.size() - 1);
            monsterDX = lastMonster.drawX;
            monsterDY = lastMonster.drawY;
        }
        if (lastMonster != null)
            monster.drawX = monsterDX - calculateSmartDistance(lastMonster, monster) * Settings.scale;
        else
            monster.drawX = monsterDX - 200.0f * Settings.scale;
        monster.drawY = monsterDY;
        if (monster.drawX < 0.0f || monster.drawX > Gdx.graphics.getWidth()
                || monster.drawY < 0.0f || monster.drawY > Gdx.graphics.getHeight()) {
            monster.drawX = MathUtils.random(0.0f, Gdx.graphics.getWidth());
            monster.drawY = MathUtils.random(Gdx.graphics.getHeight() * 0.15f, Gdx.graphics.getHeight() * 0.85f);
        }
        monster.hb.move(monster.drawX, monster.drawY);
        monster.init();
        monster.applyPowers();
        monster.useUniversalPreBattleAction();
        monster.showHealthBar();
        monster.createIntent();
        monster.usePreBattleAction();
        roomMonsters.add(monster);
        return monster;
    }

    public static AbstractMonster spawnMinion(final Class<? extends AbstractMonster> monsterClass) {
        MinionMonster minionMonster = new MinionMonster(CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster(monsterClass));
        return spawnMonster(minionMonster);
    }

    public static AbstractMonster spawnMonster(final Class<? extends AbstractMonster> monsterClass) {
        final AbstractMonster monster =
                CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster(monsterClass);
        return spawnMonster(monster);
    }

    public void addPet(AbstractMonster monster) {
        monsterGroup.add(monster);
    }

    @Override
    public void receiveAtEndOfPlayerTurnPreCard() {
        addToBotAbstract(monsterGroup::applyPreTurnLogic);
        monsterGroup.monsters.forEach(this::monsterTurn);
        addToBotAbstract(monsterGroup::applyEndOfTurnPowers);
        addToBotAbstract(monsterGroup::showIntent, 2);
    }

    private void monsterTurn(AbstractMonster monster) {
        if (!monster.isDeadOrEscaped() || monster.halfDead) {
            if (monster.intent != AbstractMonster.Intent.NONE) {
                AbstractDungeon.actionManager.addToBottom(new ShowMoveNameAction(monster));
                AbstractDungeon.actionManager.addToBottom(new IntentFlashAction(monster));
            }

            if (!(Boolean) TipTracker.tips.get("INTENT_TIP") && AbstractDungeon.player.currentBlock == 0 && (monster.intent == AbstractMonster.Intent.ATTACK || monster.intent == AbstractMonster.Intent.ATTACK_DEBUFF || monster.intent == AbstractMonster.Intent.ATTACK_BUFF || monster.intent == AbstractMonster.Intent.ATTACK_DEFEND)) {
                if (AbstractDungeon.floorNum <= 5) {
                    ++TipTracker.blockCounter;
                } else {
                    TipTracker.neverShowAgain("INTENT_TIP");
                }
            }

            monster.takeTurn();
            monster.applyTurnPowers();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        monsterGroup.render(sb);
    }

    @Override
    public void update() {
        monsterGroup.update();
    }

    @Override
    public void updateAnimation() {
        monsterGroup.updateAnimations();
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        monsterGroup.monsters.forEach(AbstractMonster::applyPowers);
    }
}
