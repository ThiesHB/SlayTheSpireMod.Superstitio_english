package superstitioapi

import com.evacipated.cardcrawl.modthespire.lib.SpireConfig
import com.google.gson.Gson
import java.io.IOException
import java.io.Serializable
import java.util.*

abstract class OkiWillSave : Serializable
{
    @Transient
    var config: SpireConfig? = null

    @Transient
    val theDefaultSettings: Properties = Properties()

    abstract fun onSave()

    abstract fun onLoad(save: OkiWillSave)

    companion object
    {
        @Transient
        private var saveFileGson: Gson = Gson()

        fun loadConfig(saveInstance: OkiWillSave, configClass: Class<out OkiWillSave>)
        {
            val configName = configClass.simpleName
            saveInstance.theDefaultSettings.setProperty(configName, "")
            try
            {
                saveInstance.config = SpireConfig(
                    DataUtility.getModID() + configName,
                    DataUtility.getModID() + configName + "Config",
                    saveInstance.theDefaultSettings
                )
                saveInstance.config!!.load()
                val totalDataString = saveInstance.config!!.getString(configName)
                saveInstance.onLoad(saveFileGson.fromJson(totalDataString, configClass))
            }
            catch (e: Exception)
            {
                Logger.error(e)
            }
        }

        fun saveConfig(saveInstance: OkiWillSave, configClass: Class<out OkiWillSave>)
        {
            val configName = configClass.simpleName
            val totalDataString =
                saveFileGson.toJsonTree(saveInstance.apply { saveInstance.onSave() }, configClass).toString()
            saveInstance.config!!.setString(configName, totalDataString)
            try
            {
                saveInstance.config!!.save()
            }
            catch (e: IOException)
            {
                Logger.error(e)
            }
        }
    }
}