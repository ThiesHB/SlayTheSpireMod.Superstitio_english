package superstitioapi.utils

import org.apache.logging.log4j.util.BiConsumer
import org.apache.logging.log4j.util.TriConsumer
import java.util.function.Consumer

object ToolBox {
    @JvmStatic
    fun <T> doIfIsInstance(test: Any, tClass: Class<T>, consumer: Consumer<T>) {
        if (tClass.isInstance(test)) {
            consumer.accept(tClass.cast(test))
        }
    }
    @JvmStatic
    fun <T, TArg> doIfIsInstance(test: Any, tClass: Class<T>, consumer: BiConsumer<T, TArg>, arg: TArg) {
        if (tClass.isInstance(test)) {
            consumer.accept(tClass.cast(test), arg)
        }
    }
    @JvmStatic
    fun <T, TArg1, TArg2> doIfIsInstance(
        test: Any,
        tClass: Class<T>,
        consumer: TriConsumer<T, TArg1, TArg2>,
        arg1: TArg1,
        arg2: TArg2
    ) {
        if (tClass.isInstance(test)) {
            consumer.accept(tClass.cast(test), arg1, arg2)
        }
    }
}
