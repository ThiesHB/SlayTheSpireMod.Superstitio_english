package superstitioapi.utils;

import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.function.Consumer;

public class ToolBox {
    public static <T> void doIfIsInstance(Object test, Class<T> tClass, Consumer<T> consumer) {
        if (tClass.isInstance(test)) {
            consumer.accept((T) test);
        }
    }

    public static <T, TArg> void doIfIsInstance(Object test, Class<T> tClass, BiConsumer<T, TArg> consumer, TArg arg) {
        if (tClass.isInstance(test)) {
            consumer.accept((T) test, arg);
        }
    }

    public static <T, TArg1, TArg2> void doIfIsInstance(Object test, Class<T> tClass, TriConsumer<T, TArg1, TArg2> consumer, TArg1 arg1, TArg2 arg2) {
        if (tClass.isInstance(test)) {
            consumer.accept((T) test, arg1, arg2);
        }
    }
}
