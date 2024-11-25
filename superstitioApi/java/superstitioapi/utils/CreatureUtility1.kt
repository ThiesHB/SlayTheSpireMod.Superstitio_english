package superstitioapi.utils

import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime
import com.megacrit.cardcrawl.random.Random
import superstitioapi.Logger
import java.util.function.Consumer
import java.util.function.Function

object CreatureUtility {
    fun forPlayerAndEachMonsters(foreachAction: Consumer<AbstractCreature>) {
        foreachAction.accept(AbstractDungeon.player)
        AbstractDungeon.getMonsters().monsters.forEach(foreachAction)
    }

    fun <T> getListMemberFromPlayerAndEachMonsters(foreachFunction: Function<AbstractCreature, List<T>>): MutableList<T> {
        val returnList = ArrayList(foreachFunction.apply(AbstractDungeon.player))
        AbstractDungeon.getMonsters().monsters.forEach { monster: AbstractMonster ->
            returnList.addAll(
                foreachFunction.apply(monster)
            )
        }
        return returnList
    }

    fun <T> getSingleMemberFromPlayerAndEachMonsters(foreachFunction: Function<AbstractCreature, T>): MutableList<T> {
        val returnList = ArrayList<T>()
        returnList.add(foreachFunction.apply(AbstractDungeon.player))
        AbstractDungeon.getMonsters().monsters.forEach { monster: AbstractMonster ->
            returnList.add(
                foreachFunction.apply(monster)
            )
        }
        return returnList
    }

    fun getRandomMonsterSafe(): AbstractMonster {
        val m = AbstractDungeon.getRandomMonster()
        if (m != null && !m.isDeadOrEscaped && !m.isDead) {
            return m
        }
        return ApologySlime()
    }

    fun getRandomMonsterWithoutRngSafe(): AbstractMonster {
        val m = AbstractDungeon.currMapNode.room.monsters.getRandomMonster(null, true, Random())
        if (m != null && !m.isDeadOrEscaped && !m.isDead) {
            return m
        }
        return ApologySlime()
    }

    fun getMonsters(): MutableList<AbstractMonster> = AbstractDungeon.getMonsters().monsters

    fun getAllAliveMonsters(): Array<AbstractMonster> {
        val monsters = getMonsters().filter(::isAlive)
        if (monsters.isEmpty()) {
            Logger.warning(
                "no monsters alive, all monsters: " + getMonsters().stream().findAny().orElse(null)
            )
            return arrayOf(ApologySlime())
        }
        return monsters.toTypedArray()
    }

    fun isAlive(c: AbstractCreature?): Boolean {
        return c != null && !c.isDeadOrEscaped && !c.isDead
    }

    fun getTargetOrRandomMonster(target: AbstractCreature): AbstractCreature {
        if (isAlive(target)) {
            return target
        }
        return getRandomMonsterSafe()
    }

    fun getMonsterOrRandomMonster(target: AbstractCreature?): AbstractMonster {
        if (target is AbstractMonster && isAlive(target)) {
            return target
        }
        return getRandomMonsterSafe()
        //        if (first != null)
//            return first;
//        Logger.warning("NoAliveMonsters");
//        return new ApologySlime();
    }
}
