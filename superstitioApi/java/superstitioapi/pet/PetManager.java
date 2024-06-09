package superstitioapi.pet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import superstitioapi.SuperstitioApiSubscriber;
import superstitioapi.utils.RenderInBattle;

public class PetManager implements RenderInBattle, SuperstitioApiSubscriber.AtEndOfPlayerTurnSubscriber {
    public static final MonsterGroup monsterGroup = new MonsterGroup(new AbstractMonster[]{});

    public PetManager() {
        RenderInBattle.Register(RenderType.Normal, this);
    }

    public static float calculateSmartDistance(final AbstractCreature m1, final AbstractCreature m2) {
        return (m1.hb_w + m2.hb_w) / 2.0f;
    }

    public static AbstractMonster spawnMonster(final AbstractMonster monsterInstance) {
        final AbstractMonster monster = monsterInstance;
        final MonsterGroup roomMonsters = monsterGroup;//AbstractDungeon.getMonsters();
        float monsterDX = Settings.WIDTH / 2.0f;
        float monsterDY = AbstractDungeon.player.drawY;
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
//
//    public static AbstractMonster spawnMonster(final Minion monsterInstance) {
//        final Minion monster = monsterInstance;
//        final MonsterGroup roomMonsters = monsterGroup;//AbstractDungeon.getMonsters();
//        float monsterDX = Settings.WIDTH / 2.0f;
//        float monsterDY = AbstractDungeon.player.drawY;
//        AbstractMonster lastMonster = null;
//        if (!roomMonsters.monsters.isEmpty()) {
//            lastMonster = roomMonsters.monsters.get(roomMonsters.monsters.size() - 1);
//            monsterDX = lastMonster.drawX;
//            monsterDY = lastMonster.drawY;
//        }
//        if (lastMonster != null)
//            monster.drawX = monsterDX - calculateSmartDistance(lastMonster, monster) * Settings.scale;
//        else
//            monster.drawX = monsterDX - 200.0f * Settings.scale;
//        monster.drawY = monsterDY;
//        monster.monster.drawX = monster.drawX;
//        monster.monster.drawY = monster.drawY;
//        monster.hb.move(monster.drawX, monster.drawY);
//        monster.monster.hb.move(monster.drawX, monster.drawY);
//        monster.init();
//        monster.applyPowers();
//        monster.useUniversalPreBattleAction();
//        monster.showHealthBar();
//        monster.createIntent();
//        monster.usePreBattleAction();
//        roomMonsters.add(monster);
//        return monster;
//    }

    public static AbstractMonster spawnMonster(final Class<? extends AbstractMonster> monsterClass) {
        final AbstractMonster monster =
                CopyAndSpawnMonsterUtility.motherFuckerWhyIShouldUseThisToCopyMonster(monsterClass);
        spawnMonster(monster);
        return monster;
    }

    public void addPet(AbstractMonster monster) {
        monsterGroup.add(monster);
    }

    @Override
    public void receiveAtEndOfPlayerTurn() {
//        GameActionManager
//        monsterGroup.monsters.forEach(AbstractMonster::takeTurn);
        monsterGroup.applyEndOfTurnPowers();
        monsterGroup.queueMonsters();
//        monsterGroup.applyPreTurnLogic();
//        monsterGroup.applyEndOfTurnPowers();
    }

    @Override
    public void render(SpriteBatch sb) {
//        monsterGroup.monsters.forEach(m -> m.render(sb));
        monsterGroup.render(sb);
//        monsterGroup.renderReticle(sb);
    }

    @Override
    public void update() {
//        monsters.monsters.forEach(monster -> {
////            if (monster.isDeadOrEscaped())
////                addToBotAbstract(() -> monsters.remove(monster));
//            monster.update();
//        });
        monsterGroup.update();
    }

    @Override
    public void updateAnimation() {
        monsterGroup.updateAnimations();
    }
}
