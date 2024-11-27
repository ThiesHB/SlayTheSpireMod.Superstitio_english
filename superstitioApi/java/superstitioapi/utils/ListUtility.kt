package superstitioapi.utils

import com.megacrit.cardcrawl.random.Random
import java.util.stream.Collectors
import java.util.stream.Stream

object ListUtility {
    fun <T> getRandomFromList(list: List<T>, random: Random): T {
        return list[random.random(list.size - 1)]
    }

    fun <T> getRandomFromList(list: com.badlogic.gdx.utils.Array<T>, random: Random): T {
        return list[random.random(list.size - 1)]
    }

    fun <T> getRandomFromList(stream: Stream<T>, random: Random): T {
        val list = stream.collect(Collectors.toList())
        return getRandomFromList(list, random)
    }

    fun <T> getRandomFromList(list: Array<T>, random: Random): T {
        return list[random.random(list.size - 1)]
    }
}
