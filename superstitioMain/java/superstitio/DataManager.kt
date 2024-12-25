package superstitio

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.internal.`$Gson$Types`
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType
import com.megacrit.cardcrawl.localization.*
import superstitio.cards.CardOwnerPlayerManager
import superstitio.cards.general.SkillCard.gainEnergy.TimeStop.TimeStopPower
import superstitio.cards.lupa.LupaCard
import superstitio.cards.lupa.PowerCard.defend.DrinkSemenBeer.DrinkSemenBeerPower
import superstitio.cards.lupa.SkillCard.block.Philter.SexPlateArmorPower
import superstitio.cards.maso.MasoCard
import superstitio.characters.cardpool.poolCover.GeneralPool
import superstitio.characters.cardpool.poolCover.LupaPool
import superstitio.characters.cardpool.poolCover.MasoPool
import superstitio.customStrings.StringsSetManager
import superstitio.customStrings.SuperstitioKeyWord
import superstitio.customStrings.interFace.HasDifferentVersionStringSet
import superstitio.customStrings.interFace.HasSFWVersion
import superstitio.customStrings.interFace.HasTextID
import superstitio.customStrings.interFace.WordReplace
import superstitio.customStrings.stringsSet.*
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnAttacked
import superstitio.delayHpLose.DelayHpLosePower_ApplyOnlyOnVictory
import superstitio.delayHpLose.DelayHpLosePower_HealOnVictory
import superstitio.delayHpLose.DelayRemoveDelayHpLosePower
import superstitio.powers.SexualHeat
import superstitio.powers.lupaOnly.FloorSemen
import superstitio.powers.lupaOnly.InsideSemen
import superstitio.powers.lupaOnly.OutsideSemen
import superstitio.powers.sexualHeatNeedModifier.ChokeChokerPower
import superstitioapi.DataUtility
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*
import java.util.function.*
import java.util.regex.Pattern
import java.util.stream.Collectors


class DataManager
{
    var spttData: SPTT_DATA = SPTT_DATA()

    @SpireInitializer
    class SPTT_DATA
    {
        // 在卡牌和遗物描述中的能量图标
        var SMALL_ORB: String = makeImgFilesPath_Character("small_orb")

        // 在卡牌预览界面的能量图标
        var BIG_ORB: String = makeImgFilesPath_Character("card_orb")

        // 小尺寸的能量图标（战斗中，牌堆预览）
        var ENERGY_ORB: String = makeImgFilesPath_Character("cost_orb")

        // 攻击牌的背景（小尺寸）
        var BG_ATTACK_512: String = makeImgFilesPath("bg_attack_512", "512")

        // 能力牌的背景（小尺寸）
        var BG_POWER_512: String = makeImgFilesPath("bg_power_512", "512")

        // 技能牌的背景（小尺寸）
        var BG_SKILL_512: String = makeImgFilesPath("bg_skill_512", "512")

        // 攻击牌的背景（大尺寸）
        var BG_ATTACK_1024: String = makeImgFilesPath("bg_attack", "1024")

        // 能力牌的背景（大尺寸）
        var BG_POWER_1024: String = makeImgFilesPath("bg_power", "1024")

        // 技能牌的背景（大尺寸）
        var BG_SKILL_1024: String = makeImgFilesPath("bg_skill", "1024")

        //选英雄界面的角色图标、选英雄时的背景图片
        var LUPA_CHARACTER_BUTTON: String = makeImgFilesPath_Character("Character_Button")

        // 人物选择界面的立绘
        var LUPA_CHARACTER_PORTRAIT: String = makeImgFilesPath_Character("Character_Portrait")
        var MASO_CHARACTER_PORTRAIT: String = makeImgFilesPath_Character("Character_Maso_Portrait")

        // 为原版人物枚举、卡牌颜色枚举扩展的枚举
        object LupaEnums
        {
            @SpireEnum
            lateinit var LUPA_Character: PlayerClass

            @SpireEnum(name = "SPTT_LUPA_PINK")
            lateinit var LUPA_CARD: CardColor

            @SpireEnum(name = "SPTT_LUPA_PINK")
            lateinit var LUPA_LIBRARY: LibraryType
        }

        object TzeentchEnums
        {
            @SpireEnum
            lateinit var TZEENTCH_Character: PlayerClass
        }

        object MasoEnums
        {
            @SpireEnum
            lateinit var MASO_Character: PlayerClass

            @SpireEnum(name = "SPTT_MASO_PINK")
            lateinit var MASO_CARD: CardColor

            @SpireEnum(name = "SPTT_MASO_PINK")
            lateinit var MASO_LIBRARY: LibraryType
        }

        object GeneralEnums
        {
            @SpireEnum
            lateinit var GENERAL_Virtual_Character: PlayerClass

            @SpireEnum(name = "SPTT_GENERAL_PINK")
            lateinit var GENERAL_CARD: CardColor

            @SpireEnum(name = "SPTT_GENERAL_PINK")
            lateinit var GENERAL_LIBRARY: LibraryType
        }

        object TempCardEnums
        {
            @SpireEnum
            lateinit var TempCard_Virtual_Character: PlayerClass

            @SpireEnum(name = "SPTT_TEMP_PINK")
            lateinit var TempCard_CARD: CardColor

            @SpireEnum(name = "SPTT_TEMP_PINK")
            lateinit var TempCard_LIBRARY: LibraryType
        }

        companion object
        {
            val SEX_COLOR: Color = Color(250.0f / 255.0f, 20.0f / 255.0f, 147.0f / 255.0f, 1.0f)
            val BG_ATTACK_SEMEN: String = makeImgFilesPath_UI("bg_attack_semen")
            val BG_ATTACK_512_SEMEN: String = makeImgFilesPath_UI("bg_attack_512_semen")

            @JvmStatic
            fun initialize()
            {
                SPTT_DATA()
            }
        }
    }

    object CanOnlyDamageDamageType
    {
        @SpireEnum
        lateinit var UnBlockAbleDamageType: DamageType

        @SpireEnum
        lateinit var NoTriggerLupaAndMasoRelicHpLose: DamageType
    }

    object CardTagsType
    {
        @SpireEnum
        lateinit var CruelTorture: AbstractCard.CardTags

        @SpireEnum
        lateinit var BodyModification: AbstractCard.CardTags

        @SpireEnum
        lateinit var InsideEjaculation: AbstractCard.CardTags

        @SpireEnum
        lateinit var OutsideEjaculation: AbstractCard.CardTags
    }

    companion object
    {
        /**
         * 作为随机数种子之类的
         */
        const val MAGIC_NUMBER_0: Int = 19260817

        const val CODER_COMPUTERNAME: String = "DESKTOP-VK8L63C"
        const val COMPUTERNAME: String = "COMPUTERNAME"
        const val GURO_VERSION_TIPS: String = "#GuroVersion#"
        var cards: MutableMap<String, CardStringsWillMakeFlavorSet> = HashMap()
        var powers: MutableMap<String, PowerStringsSet> = HashMap()
        var modifiers: MutableMap<String, ModifierStringsSet> = HashMap()
        var orbs: MutableMap<String, OrbStringsSet> = HashMap()
        var uiStrings: MutableMap<String, UIStringsSet> = HashMap()

        //    public static Object[] allData = Arrays.stream(new Map[]{cards, powers, modifiers, orbs, uiStrings}).toArray();
        fun forEachData(consumer: Consumer<Map<String, HasDifferentVersionStringSet<*>>>)
        {
            consumer.accept(cards)
            consumer.accept(powers)
            consumer.accept(modifiers)
            consumer.accept(orbs)
            consumer.accept(uiStrings)
        }

        fun getModID(): String = SuperstitioModSetup.MOD_NAME + "Mod"

        fun makeImgFilesPath(fileName: String, vararg folderPaths: String): String
        {
            return getImgFolderPath(makeFolderTotalString(*folderPaths), "$fileName.png")
        }

        fun makeFolderTotalString(vararg strings: String): String
        {
            if (strings.size == 0) return ""
            val totalString = StringBuilder()
            for (string in strings) totalString.append("/").append(string)
            return totalString.toString()
        }

        fun makeImgFilesPath_Card(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "cards", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_Relic(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "relics", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_UI(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "ui", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_Character(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "character", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_RelicOutline(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "relics/outline", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_RelicLarge(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "relics/large", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_Orb(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "orbs", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_Power(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "powers", makeFolderTotalString(*subFolder))
        }

        fun makeImgFilesPath_Event(fileName: String, vararg subFolder: String): String
        {
            return makeImgFilesPath(fileName, "events", makeFolderTotalString(*subFolder))
        }

        fun MakeTextID(idText: String): String
        {
            return getModID() + ":" + idText
        }

        fun MakeTextID(idClass: Class<*>): String
        {
            if (CardOwnerPlayerManager.isGeneralCard(idClass)) return getModID() + ":" + idClass.simpleName
            if (CardOwnerPlayerManager.isLupaCard(idClass)) return getModID() + ":" + LupaCard::class.java.simpleName + ":" + idClass.simpleName
            if (CardOwnerPlayerManager.isMasoCard(idClass)) return getModID() + ":" + MasoCard::class.java.simpleName + ":" + idClass.simpleName
            return getModID() + ":" + idClass.simpleName
        }

        fun MakeTextID(idText: String, idClass: Class<*>): String
        {
            if (CardOwnerPlayerManager.isGeneralCard(idClass)) return getModID() + ":" + idText
            if (CardOwnerPlayerManager.isLupaCard(idClass)) return getModID() + ":" + LupaCard::class.java.simpleName + ":" + idText
            if (CardOwnerPlayerManager.isMasoCard(idClass)) return getModID() + ":" + MasoCard::class.java.simpleName + ":" + idText
            return getModID() + ":" + idText
        }

        fun makeImgPath(
            defaultFileName: String,
            PathFinder: BiFunction<String, Array<String>, String>,
            fileName: String,
            vararg subFolder: String
        ): String
        {
            val name = if (fileName.contains(MasoCard::class.java.simpleName))
            {
                fileName + GURO_VERSION_TIPS
            }
            else
            {
                fileName
            }

            return DataUtility.makeImgPath({
                try
                {
                    if (isRunningInCoderComputer) makeNeedDrawPicture(
                        defaultFileName,
                        PathFinder,
                        DataUtility.getIdOnly(name),
                        DataUtility.makeDefaultPath(defaultFileName, PathFinder),
                        *subFolder
                    )
                }
                catch (e: IOException)
                {
                    Logger.error(e)
                }
            }, defaultFileName, PathFinder, name, *subFolder)
        }

        fun <T> makeJsonStringFromFile(fileName: String, objectClass: Class<T>?): T
        {
            val gson = Gson()
            val json = Gdx.files.internal(makeLocalizationPath(Settings.language, fileName))
                .readString(StandardCharsets.UTF_8.toString())
            return gson.fromJson(json, objectClass)
        }

        fun replaceStringsInObj(obj: Any, wordReplace: WordReplace)
        {
            for (field in obj.javaClass.declaredFields)
            {
                field.isAccessible = true
                try
                {
                    if (field[obj] is String)
                    {
                        var value = field[obj] as String?
                        if (value == null || !value.contains(wordReplace.WordOrigin)) continue
                        value = value.replace(wordReplace.WordOrigin, wordReplace.WordReplace)
                        field[obj] = value
                    }
                    else if (field[obj] is Array<*> && (field[obj] as Array<*>).isArrayOf<String>())
                    {
                        val values = field[obj] as Array<String?>?
                        if (values.isNullOrEmpty()) continue
                        val list = arrayOfNulls<String>(values.size)
                        var i = 0
                        val valuesLength = values.size
                        while (i < valuesLength)
                        {
                            var string = values[i]
                            if (string != null && string.contains(wordReplace.WordOrigin))
                            {
                                string = string.replace(wordReplace.WordOrigin, wordReplace.WordReplace)
                            }
                            val apply = string
                            list[i] = apply
                            i++
                        }

                        field[obj] = list
                    }
                }
                catch (e: IllegalAccessException)
                {
                    Logger.error(e)
                }
            }
        }

        fun <T : HasDifferentVersionStringSet<*>> loadCustomStringsFile(
            fileName: String,
            target: MutableMap<String, T>,
            tSetClass: Class<T>
        )
        {
            superstitioapi.Logger.debug("loadJsonStrings: " + tSetClass.typeName)
            val jsonString = Gdx.files.internal(makeLocalizationPath(Settings.language, fileName))
                .readString(StandardCharsets.UTF_8.toString())
            val typeToken =
                GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(tSetClass)
            val gson = Gson()
            val map = gson.fromJson<Map<String, T>>(jsonString, typeToken)
            map.forEach { (id: String, strings: T) ->
                strings.initial()
                if (strings is HasTextID) (strings as HasTextID).textID = id
            }
            target.putAll(map)
        }

        //生成所有的SFW本地化
        fun <T> makeSFWLocalization(data: Map<String, T>, fileName: String)
        {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(data)
            Gdx.files.local(makeLocalizationPath(Settings.language, fileName + "_sfw"))
                .writeString(jsonString, false, "UTF-8")
        }

        //生成所有的SFW本地化
        //    public static <T> void makeSFWLocalization(Map<String, RelicStrings> data, String fileName, Class<T> tClass) {
        //        Json json = new Json();
        //        String jsonString = json.toJson(data);
        //        Gdx.files.local(makeLocalizationPath(Settings.language, fileName + "_sfw")).writeString(jsonString, false);
        //    }
        //生成所有的SFW本地化
        fun <T> makeSFWLocalization(data: List<T>, fileName: String)
        {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val jsonString = gson.toJson(data)
            Gdx.files.local(makeLocalizationPath(Settings.language, fileName + "_sfw"))
                .writeString(jsonString, false, "UTF-8")
        }

        fun makeAllSFWLocalizationForCoder()
        {
            if (!isRunningInCoderComputer) return
            if (!SuperstitioConfig.isEnableSFW()) return
            makeSFWLocalization(leaveOnlySFWMap(cards, CardStringsWillMakeFlavorSet::class.java), "cards")
            makeSFWLocalization(leaveOnlySFWMap(modifiers, ModifierStringsSet::class.java), "modifiers")
            makeSFWLocalization(leaveOnlySFWMap(powers, PowerStringsSet::class.java), "powers")
            makeSFWLocalization(leaveOnlySFWMap(orbs, OrbStringsSet::class.java), "orbs")
            makeSFWLocalization(leaveOnlySFWMap(uiStrings, UIStringsSet::class.java), "ui")
            val relicsStrings = ReflectionHacks.getPrivateStatic<Map<String, RelicStrings>>(
                LocalizedStrings::class.java, "relics"
            )
            val copyRelicsStrings: MutableMap<String, RelicStrings> = HashMap()
            relicsStrings.forEach { (s: String, t: RelicStrings) ->
                if (s.contains(
                        getModID()
                    )
                ) copyRelicsStrings[s] = t
            }
            makeSFWLocalization(copyRelicsStrings, "relics")
            val allKeywords =
                StringsSetManager.allKeywords.stream().map { obj: SuperstitioKeyWord? -> obj!!.makeCopy() }
                    .collect(Collectors.toList())
            for (keyWord in allKeywords)
            {
                keyWord!!.NAMES = arrayOf("")
                keyWord.PROPER_NAME = ""
                keyWord.DESCRIPTION = ""
            }
            makeSFWLocalization(allKeywords, "keywords")
        }


        @Throws(
            NoSuchMethodException::class,
            InvocationTargetException::class,
            InstantiationException::class,
            IllegalAccessException::class
        )
        fun <T : HasSFWVersion<*>?> leaveOnlySFW(stringSet: T, tClass: Class<T>): T
        {
            val tryCopyStringSet = stringSet!!.makeSFWCopy()
            val copyStringSet = if (tClass.isInstance(stringSet))
                tClass.cast(tryCopyStringSet)
            else
            {
                tClass.getDeclaredConstructor().newInstance()
            }
            //        copyStringSet.initialSelfBlack();
            return copyStringSet
        }

        fun <T : HasSFWVersion<*>> leaveOnlySFWMap(data: Map<String, T>, tClass: Class<T>): Map<String, T>
        {
            val copyData: MutableMap<String, T> = HashMap()
            data.forEach { (s: String, t: T) ->
                try
                {
                    copyData[s] = leaveOnlySFW(t, tClass)
                }
                catch (e: NoSuchMethodException)
                {
                    Logger.error(e)
                }
                catch (e: InvocationTargetException)
                {
                    Logger.error(e)
                }
                catch (e: InstantiationException)
                {
                    Logger.error(e)
                }
                catch (e: IllegalAccessException)
                {
                    Logger.error(e)
                }
            }
            return copyData
        }

        fun makeLocalizationPath(language: GameLanguage?, filename: String): String
        {
            var ret = "localization/"
            ret = when (language)
            {
                GameLanguage.ZHS -> ret + "zhs/"
                else             -> ret + "zhs/"
            }
            return resourcesFilesPath + ret + filename + ".json"
        }

        private val isRunningInCoderComputer: Boolean
            get() = System.getenv()[COMPUTERNAME] == CODER_COMPUTERNAME

        private val resourcesFilesPath: String
            get() = getModID() + "Resources/"

        private fun getImgFolderPath(path: String, file: String): String
        {
            var allLevelPath = resourcesFilesPath + "img" + path + "/" + file
            var sfwLevelPath = resourcesFilesPath + "imgSFW" + path + "/" + file

            if (path.contains(GURO_VERSION_TIPS) || file.contains(GURO_VERSION_TIPS))
            {
                sfwLevelPath = sfwLevelPath.replace(GURO_VERSION_TIPS.toRegex(), "")
                allLevelPath = allLevelPath.replace(GURO_VERSION_TIPS.toRegex(), "")
                if (!SuperstitioConfig.isEnableGuroCharacter())
                {
                    return sfwLevelPath
                }
            }


            return if (!SuperstitioConfig.isEnableSFW())
            {
                allLevelPath
            }
            else sfwLevelPath
        }

        //生成所有的需要绘制的图片，方便检查
        @Throws(IOException::class)
        private fun makeNeedDrawPicture(
            defaultFileName: String, PathFinder: BiFunction<String, Array<String>, String>, fileName: String,
            defaultPath: String, vararg subFolder: String
        )
        {
            val needDrawFileName: MutableList<String> = ArrayList()
            needDrawFileName.add("needDraw")
            needDrawFileName.addAll(subFolder)
            if (fileName.contains("32")) return
            if (fileName.contains("84") && noNeedDrawPower84(fileName)) return
            if (noNeedImgName(fileName)) return

            if (defaultPath.contains("outline")) return
            if (defaultPath.contains("large")) return

            val PathWithSubFolder = PathFinder.apply("", subFolder.toList().toTypedArray())
            val defaultFileHandle = if (PathWithSubFolder.contains("card"))
            {
                Gdx.files.internal(PathFinder.apply(defaultFileName + "_p", arrayOf("")))
            }
            else if (PathWithSubFolder.contains("orb"))
            {
                return
            }
            else
            {
                Gdx.files.internal(defaultPath)
            }
            val needDrawFilePath = if (PathWithSubFolder.contains("card"))
            {
                PathFinder.apply(fileName + "_p", needDrawFileName.toTypedArray())
            }
            else PathFinder.apply(fileName, needDrawFileName.toTypedArray())
            val defaultFileCopyTo = File(needDrawFilePath)
            val pattern = Pattern.compile("^(.+/)[^/]+$")
            val matcher = pattern.matcher(needDrawFilePath)
            val totalFolderPath = if (matcher.find())
            {
                matcher.group(1)
            }
            else
            {
                needDrawFilePath
            }
            File(totalFolderPath).mkdirs()
            if (!defaultFileCopyTo.exists()) Files.copy(defaultFileHandle.read(), defaultFileCopyTo.toPath())
        }

        private fun noNeedImgName(fileName: String): Boolean
        {
            if (fileName == LupaPool::class.java.simpleName) return true
            if (fileName == MasoPool::class.java.simpleName) return true
            if (fileName == GeneralPool::class.java.simpleName) return true
            return false
        }

        //    public static <T> Optional<String> getTypeMapFromLocalizedStrings(Class<T> tClass) {
        //        Logger.run("initializeTypeMaps");
        //        for (Field f : LocalizedStrings.class.getDeclaredFields()) {
        //            Type type = f.getGenericType();
        //            if (type instanceof ParameterizedType) {
        //                ParameterizedType pType = (ParameterizedType) type;
        //                Type[] typeArgs = pType.getActualTypeArguments();
        //                if (typeArgs.length == 2 && typeArgs[0] == String.class && typeArgs[1] == tClass)
        //                    return Optional.of(f.getName());
        //            }
        //        }
        //        return Optional.empty();
        //    }
        //
        //    public static void setJsonStrings(Class<?> tClass, Map<String, Object> GivenMap) {
        //        String mapName = getTypeMapFromLocalizedStrings(tClass).orElse("");
        //        if (mapName.isEmpty()) return;
        //        Map<String, Object> localizationStrings = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, mapName);
        //        localizationStrings.putAll(GivenMap);
        //        ReflectionHacks.setPrivateStaticFinal(LocalizedStrings.class, mapName, localizationStrings);
        //    }
        private fun noNeedDrawPower84(fileName: String): Boolean
        {
            val checkName = fileName.replace("84", "")
            if (checkName == DataUtility.getIdOnly(SexPlateArmorPower.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(TimeStopPower.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(ChokeChokerPower.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(SexualHeat.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(DelayHpLosePower_ApplyOnlyOnVictory.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(DelayHpLosePower_ApplyOnAttacked.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(DelayHpLosePower_HealOnVictory.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(DelayRemoveDelayHpLosePower.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(FloorSemen.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(InsideSemen.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(OutsideSemen.POWER_ID)) return true
            if (checkName == DataUtility.getIdOnly(DrinkSemenBeerPower.POWER_ID)) return true
            return false
        }

        private fun <T> GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(
            tClass: Class<T>
        ): ParameterizedType?
        {
            val var0 = DataManager::class.java.declaredFields

            for (f in var0)
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
}
