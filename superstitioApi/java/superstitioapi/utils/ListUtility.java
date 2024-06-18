package superstitioapi.utils;

import com.badlogic.gdx.utils.Array;
import com.megacrit.cardcrawl.random.Random;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListUtility {
    public static <T> T getRandomFromList(List<T> list, Random random) {
        return list.get(random.random(list.size() - 1));
    }

    public static <T> T getRandomFromList(Array<T> list, Random random) {
        return list.get(random.random(list.size - 1));
    }

    public static <T> T getRandomFromList(Stream<T> stream, Random random) {
        List<T> list = stream.collect(Collectors.toList());
        return getRandomFromList(list, random);
    }

    public static <T> T getRandomFromList(T[] list, Random random) {
        return list[random.random(list.length - 1)];
    }
}
