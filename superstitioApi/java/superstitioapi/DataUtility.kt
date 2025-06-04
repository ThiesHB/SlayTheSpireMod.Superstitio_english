package superstitioapi

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.google.gson.Gson
import com.google.gson.internal.`$Gson$Types`
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.localization.LocalizedStrings
import superstitioapi.utils.pathBuilder.*
import java.lang.reflect.Field
import java.lang.reflect.ParameterizedType
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern
import java.util.stream.Collectors


@SpireInitializer
object DataUtility
{

    private val isPathExist: MutableMap<String, Boolean> = HashMap()

    @JvmStatic
    fun initialize()
    {
    }

    internal object DataRootPath
    {
        private val resourcesFilesPath = AbsoluteScope.create(getModID() + "Resources")
        val localizationPath = resourcesFilesPath.createScope("localization").withFormat("json")
        val shaderPath = resourcesFilesPath.createScope("shader").withFormat("glsl")
        val imagesPath = resourcesFilesPath.createScope("img").withFormat("png")
    }

    object ImgSubPath
    {
        val cardsPath = RelativeScope.create("cards")
        val uiPath = RelativeScope.create("ui")
        val characterPath = RelativeScope.create("character")
        val relicsPath = RelativeScope.create("relics")
        val relicsOutlinePath = relicsPath.createScope("outline")
        val relicsLargePath = relicsPath.createScope("large")
        val orbsPath = RelativeScope.create("orbs")
        val powersPath = RelativeScope.create("powers")
        val eventsPath = RelativeScope.create("events")
    }

    internal fun makeLocalizationPath(language: GameLanguage?, filename: String): String
    {
        val ret = when (language)
        {
            GameLanguage.ZHS -> "zhs"
            GameLanguage.ZHT -> "zhs"
            GameLanguage.ENG -> "eng"
            else             -> "eng"
        }
        return DataRootPath.localizationPath.createScope(ret).resolveFile(filename)
    }

    fun makeShaderPath(filename: String): String
    {
        return DataRootPath.shaderPath.resolveFile(filename)
    }

    fun getModID(): String = SuperstitioApiSetup.MOD_NAME + "Mod"

    fun makeRelativeImgFilesPath(fileName: String, vararg folderPaths: String): RelativeFilePath
    {
        return RelativeScope.create(*folderPaths).resolveFile(fileName)
    }

    fun makeImgFilesPath(fileName: String, vararg folderPaths: String): String
    {
        return DataRootPath.imagesPath.createScope(*folderPaths).resolveFile(fileName)
    }


//    fun makeFolderTotalString(vararg strings: String): String
//    {
//        if (strings.size == 0) return ""
//        val totalString = StringBuilder()
//        for (string in strings) totalString.append("/").append(string)
//        return totalString.toString()
//    }

    fun MakeTextID(idText: String): String
    {
        return getModID() + ":" + idText
    }

    fun MakeTextID(idClass: Class<*>): String
    {
        return getModID() + ":" + idClass.simpleName
    }


    fun tryGetImgPath(
        relativeScope: RelativeScope,
        fileNameMaybeDirty: String, // 含有模组信息的“脏文件名，需要清洗为ID”
        defaultFileName: String,
        absoluteScope: AbsoluteScopeWithFormat,
        actionWhenNotFound: ActionWhenNotFound? = null
    ): String
    {
        return tryGetImgPath(
            relativeScope.resolveFile(getIdOnly(fileNameMaybeDirty)),
            relativeScope.resolveFile(getIdOnly(defaultFileName)),
            absoluteScope,
            actionWhenNotFound
        )
    }

    fun tryGetImgPath(
        relativeFilePath: RelativeFilePath,
        defaultPath: RelativeFilePath,
        absoluteScope: AbsoluteScopeWithFormat,
        actionWhenNotFound: ActionWhenNotFound? = null
    ): String
    {
        val primaryPath = relativeFilePath.pinToAbsolute(absoluteScope)

        val primaryPathExists = isPathExist.getOrPut(primaryPath) {
            val exists = Gdx.files.internal(primaryPath).exists()
            if (!exists)
            {
                Logger.warning("Can't find $primaryPath. Use default img instead.")
                actionWhenNotFound?.invoke(relativeFilePath, defaultPath, absoluteScope)
            }
            return@getOrPut exists // 返回存在性结果，存入缓存
        }

        return if (primaryPathExists)
        {
            primaryPath
        }
        else
        {
            defaultPath.pinToAbsolute(absoluteScope)
        }
    }

//    fun makeImgPath(
//        defaultFileName: String,
//        absoluteScope: AbsoluteScopeWithFormat,
//        relativeScope: RelativeScope,
//        fileName: String,
//        vararg subFolder: String
//    ): String
//    {
//        return makeImgPath({ }, defaultFileName, PathFinder, getIdOnly(fileName), *subFolder)
//    }

    /**
     * 只输出后面的id，不携带模组信息
     *
     * @param complexIds 带有本模组信息的ID，“SuperstitionMod:xxx”
     * @return 去除前面的部分
     */
    fun getIdOnly(complexIds: Array<String>): Array<String>
    {
        return complexIds.map(::getIdOnly).toTypedArray()
    }

    fun getIdOnly(complexIds: String): String
    {
        // 定义正则表达式，匹配最后一个冒号及其后面的所有字符
        val pattern = Pattern.compile("(.*?):([^:]*)$")
        val matcher = complexIds.let(pattern::matcher)

        // 如果匹配成功
        if (matcher.find())
        {
            // 返回冒号后的内容
            return matcher.group(2)
        }
        // 如果没有匹配到冒号，则返回原字符串
        return complexIds
    }

    fun <T> getTypeMapFromLocalizedStrings(tClass: Class<T>): String?
    {
        Logger.run("initializeTypeMaps")
        for (f in LocalizedStrings::class.java.declaredFields)
        {
            val type = f.genericType
            if (type is ParameterizedType)
            {
                val typeArgs = type.actualTypeArguments
                if (typeArgs.size == 2 && typeArgs[0] === String::class.java && typeArgs[1] === tClass)
                    return f.name
            }
        }
        return null
    }

    fun setJsonStrings(tClass: Class<*>, GivenMap: Map<String, Any>?)
    {
        val mapName = getTypeMapFromLocalizedStrings(tClass)
        if (mapName.isNullOrEmpty()) return
        val localizationStrings = ReflectionHacks.getPrivateStatic<MutableMap<String, Any>>(
            LocalizedStrings::class.java, mapName
        )
        localizationStrings.putAll(GivenMap!!)
        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings::class.java, mapName, localizationStrings)
    }

    fun removeMatch(listReturn: MutableList<hasUuid>, listInfo: MutableList<hasUuid>): MutableList<hasUuid>
    {
        listReturn.removeIf { a: hasUuid ->
            listInfo.stream()
                .map(hasUuid::uuid)
                .collect(Collectors.toList())
                .contains(a.uuid)
        }
        return listReturn
    }

    fun <T> makeJsonStringFromFile(fileName: String, objectClass: Class<T>?): T
    {
        val gson = Gson()
        val json = Gdx.files.internal(makeLocalizationPath(Settings.language, fileName))
            .readString(StandardCharsets.UTF_8.toString())
        return gson.fromJson(json, objectClass)
    }

    fun <T> GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(
        tClass: Class<T>, declaredFields: Array<out Field>
    ): ParameterizedType?
    {
        for (f in declaredFields)
        {
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

/**
 * 寻找文件时，如果没有找到，使用的回调函数
 * @param primaryPath 原本期望的相对地址
 * @param defaultPath 不得不采纳的默认相对地址
 * @param absoluteScope 绝对地址
 */
typealias ActionWhenNotFound = (
    primaryPath: RelativeFilePath,
    defaultPath: RelativeFilePath,
    absoluteScope: AbsoluteScopeWithFormat
) -> Unit

interface hasUuid
{
    val uuid: String
}
