package superstitioapi

import com.evacipated.cardcrawl.modthespire.Loader
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

object Logger {
    private val logger: Logger = LogManager.getLogger(
        SuperstitioApiSetup::class.qualifiedName
    )
    @JvmStatic
    fun error(exception: Exception) {
        logger.error("[ERROR] " + exception.message)
    }
    @JvmStatic
    fun error(string: String?) {
        logger.error(string)
    }
    @JvmStatic
    fun info(string: String?) {
        logger.info(string)
    }

    @JvmStatic
    fun run(string: String?) {
        if (Loader.DEBUG) logger.info(string)
    }
    @JvmStatic
    fun temp(string: String?) {
        logger.info(string)
    }
    @JvmStatic
    fun debug(string: String?) {
        if (Loader.DEBUG) logger.info(string)
    }
    @JvmStatic
    fun warning(string: String) {
//        if (Loader.DEBUG)
        logger.warn("[WARNING] $string")
    }
}
