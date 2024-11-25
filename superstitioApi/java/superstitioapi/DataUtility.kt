package superstitioapi

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.localization.LocalizedStrings
import java.lang.reflect.ParameterizedType
import java.nio.charset.StandardCharsets
import java.util.function.BiFunction
import java.util.function.Consumer
import java.util.regex.Pattern
import java.util.stream.Collectors


@SpireInitializer
object DataUtility {

    val isPathExist: MutableMap<String, Boolean> = HashMap()

    @JvmStatic
    fun initialize() {
    }

    fun makeLocalizationPath(language: GameLanguage?, filename: String): String {
        var ret = "localization/"
        ret = when (language) {
            GameLanguage.ZHS -> ret + "zhs/"
            GameLanguage.ENG -> ret + "eng/"
            else -> ret + "eng/"
        }
        return resourcesFilesPath + ret + filename + ".json"
    }

    fun makeShaderPath(filename: String): String {
        return resourcesFilesPath + "shader/" + filename
    }

    val modID: String
        get() = SuperstitioApiSetup.MOD_NAME + "Mod"

    fun makeImgFilesPath(fileName: String, vararg folderPaths: String): String {
        return getImgFolderPath(makeFolderTotalString(*folderPaths)) + "/" + fileName + ".png"
    }

    fun makeFolderTotalString(vararg strings: String): String {
        if (strings.size == 0) return ""
        val totalString = StringBuilder()
        for (string in strings) totalString.append("/").append(string)
        return totalString.toString()
    }

    fun makeImgFilesPath_Card(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "cards", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_Relic(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "relics", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_UI(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "ui", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_Character_Lupa(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "character", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_RelicOutline(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "relics/outline", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_RelicLarge(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "relics/large", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_Orb(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "orbs", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_Power(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "powers", makeFolderTotalString(*subFolder))
    }

    fun makeImgFilesPath_Event(fileName: String, vararg subFolder: String): String {
        return makeImgFilesPath(fileName, "events", makeFolderTotalString(*subFolder))
    }

    fun MakeTextID(idText: String): String {
        return modID + ":" + idText
    }

    fun MakeTextID(idClass: Class<*>): String {
        return modID + ":" + idClass.simpleName
    }

    fun makeImgPath(
        defaultFileName: String,
        PathFinder: BiFunction<String, Array<String>, String>,
        fileName: String,
        vararg subFolder: String
    ): String {
        return makeImgPath({ }, defaultFileName, PathFinder, fileName, *subFolder)
    }

    fun makeImgPath(
        actionIfNoImg: Consumer<String>,
        defaultFileName: String,
        PathFinder: BiFunction<String, Array<String>, String>,
        fileName: String,
        vararg subFolder: String
    ): String {
        val path: String?
        val idOnlyNames = getIdOnly(fileName)
        path = PathFinder.apply(idOnlyNames, subFolder.toList().toTypedArray())

        if (isPathExist.containsKey(path)) {
            return if (isPathExist[path]!!) path
            else makeDefaultPath(defaultFileName, PathFinder)
        } else if (Gdx.files.internal(path).exists()) {
            isPathExist[path] = true
            return path
        } else {
            isPathExist[path] = false
            Logger.warning("Can't find $path. Use default img instead.")
            actionIfNoImg.accept(path)

            return makeDefaultPath(defaultFileName, PathFinder)
        }
    }

    fun makeDefaultPath(
        defaultFileName: String,
        PathFinder: BiFunction<String, Array<String>, String>
    ): String {
        return PathFinder.apply(defaultFileName, arrayOf(""))
    }

    /**
     * 只输出后面的id，不携带模组信息
     *
     * @param complexIds 带有本模组信息的ID，“SuperstitionMod:xxx”
     * @return 去除前面的部分
     */
    fun getIdOnly(complexIds: Array<String>): Array<String> {
        return complexIds.map(::getIdOnly).toTypedArray()
    }

    fun getIdOnly(complexIds: String): String {
        // 定义正则表达式，匹配最后一个冒号及其后面的所有字符
        val pattern = Pattern.compile("(.*?):([^:]*)$")
        val matcher = complexIds.let(pattern::matcher)

        // 如果匹配成功
        if (matcher.find()) {
            // 返回冒号后的内容
            return matcher.group(2)
        }
        // 如果没有匹配到冒号，则返回原字符串
        return complexIds
    }

    fun <T> getTypeMapFromLocalizedStrings(tClass: Class<T>): String? {
        Logger.run("initializeTypeMaps")
        for (f in LocalizedStrings::class.java.declaredFields) {
            val type = f.genericType
            if (type is ParameterizedType) {
                val typeArgs = type.actualTypeArguments
                if (typeArgs.size == 2 && typeArgs[0] === String::class.java && typeArgs[1] === tClass)
                    return f.name
            }
        }
        return null
    }

    fun setJsonStrings(tClass: Class<*>, GivenMap: Map<String, Any>?) {
        val mapName = getTypeMapFromLocalizedStrings(tClass)
        if (mapName.isNullOrEmpty()) return
        val localizationStrings = ReflectionHacks.getPrivateStatic<MutableMap<String, Any>>(
            LocalizedStrings::class.java, mapName
        )
        localizationStrings.putAll(GivenMap!!)
        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings::class.java, mapName, localizationStrings)
    }

    fun removeMatch(listReturn: MutableList<hasUuid>, listInfo: MutableList<hasUuid>): MutableList<hasUuid> {
        listReturn.removeIf { a: hasUuid ->
            listInfo.stream()
                .map(hasUuid::uuid)
                .collect(Collectors.toList())
                .contains(a.uuid)
        }
        return listReturn
    }

    fun <T> makeJsonStringFromFile(fileName: String, objectClass: Class<T>?): T {
        val gson = Gson()
        val json = Gdx.files.internal(makeLocalizationPath(Settings.language, fileName))
            .readString(StandardCharsets.UTF_8.toString())
        return gson.fromJson(json, objectClass)
    }

    private val resourcesFilesPath: String
        get() = modID + "Resources/"

    private fun getImgFolderPath(path: String): String {
        return resourcesFilesPath + "img" + path
    }

    private fun <T> GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(
        tClass: Class<T>
    ): ParameterizedType? {
        val var0 = DataUtility::class.java.declaredFields

        for (f in var0) {
            val type = f.genericType as? ParameterizedType ?: continue
            val typeArgs = type.actualTypeArguments
            if (typeArgs.size == 2 && typeArgs[0] == String::class.java && typeArgs[1] == tClass)
                return `$Gson$Types`.newParameterizedTypeWithOwner(
                    null,
                    MutableMap::class.java,
                    String::class.java,
                    typeArgs[1]
                )
        }
        return null
    }
}

interface hasUuid {
    val uuid: String
}
