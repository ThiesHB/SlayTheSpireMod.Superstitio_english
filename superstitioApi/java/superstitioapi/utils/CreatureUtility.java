package superstitioapi.utils;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import com.megacrit.cardcrawl.random.Random;
import superstitioapi.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.currMapNode;

public class CreatureUtility {

    public static void forPlayerAndEachMonsters(Consumer<AbstractCreature> foreachAction) {
        foreachAction.accept(AbstractDungeon.player);
        AbstractDungeon.getMonsters().monsters.forEach(foreachAction);
    }

    public static <T> ArrayList<T> getListMemberFromPlayerAndEachMonsters(Function<AbstractCreature, List<T>> foreachFunction) {
        ArrayList<T> returnList = new ArrayList<>(foreachFunction.apply(AbstractDungeon.player));
        AbstractDungeon.getMonsters().monsters.forEach(monster -> returnList.addAll(foreachFunction.apply(monster)));
        return returnList;
    }

    public static <T> ArrayList<T> getSingleMemberFromPlayerAndEachMonsters(Function<AbstractCreature, T> foreachFunction) {
        ArrayList<T> returnList = new ArrayList<>();
        returnList.add(foreachFunction.apply(AbstractDungeon.player));
        AbstractDungeon.getMonsters().monsters.forEach(monster -> returnList.add(foreachFunction.apply(monster)));
        return returnList;
    }

    public static AbstractMonster getRandomMonsterSafe() {
        final AbstractMonster m = AbstractDungeon.getRandomMonster();
        if (m != null && !m.isDeadOrEscaped() && !m.isDead) {
            return m;
        }
        return new ApologySlime();
    }

    public static AbstractMonster getRandomMonsterWithoutRngSafe() {
        final AbstractMonster m = currMapNode.room.monsters.getRandomMonster(null, true, new Random());
        if (m != null && !m.isDeadOrEscaped() && !m.isDead) {
            return m;
        }
        return new ApologySlime();
    }

    public static ArrayList<AbstractMonster> getMonsters() {
        return AbstractDungeon.getMonsters().monsters;
    }

    public static AbstractMonster[] getAllAliveMonsters() {
        AbstractMonster[] monsters = getMonsters().stream().filter(CreatureUtility::isAlive).toArray(AbstractMonster[]::new);
        if (monsters.length == 0) {
            Logger.warning("no monsters alive, all monsters: " + getMonsters().stream().findAny().orElse(null));
            return new AbstractMonster[]{new ApologySlime()};
        }
        return monsters;
    }

    public static boolean isAlive(final AbstractCreature c) {
        return c != null && !c.isDeadOrEscaped() && !c.isDead;
    }
}
