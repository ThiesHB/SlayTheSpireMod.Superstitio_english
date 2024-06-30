package superstitioapi.utils;

import java.util.function.Consumer;

public class ToolBox {
    public static <T> void doIfIsInstance(Object test, Consumer<T> consumer, Class<T> tClass) {
        if (tClass.isInstance(test)) {
            consumer.accept((T) test);
        }
    }
}
