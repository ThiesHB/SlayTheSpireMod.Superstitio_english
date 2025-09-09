package superstitio

import basemod.ReflectionHacks
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.core.Settings.GameLanguage
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType
import com.megacrit.cardcrawl.localization.LocalizedStrings
import com.megacrit.cardcrawl.localization.RelicStrings
import superstitio.DataManager.*
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
import superstitioapi.DataUtility.GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse
import superstitioapi.DataUtility.ImgSubPath
import superstitioapi.utils.pathBuilder.*
import java.io.File
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.regex.Pattern
import kotlin.io.path.Path


class DataManager
{
    //    @SpireInitializer
    object SPTT_DATA
    {
        // 在卡牌和遗物描述中的能量图标
        val SMALL_ORB: String = ImgPath.characterPath.resolveFile("small_orb")

        // 在卡牌预览界面的能量图标
        val BIG_ORB: String = ImgPath.characterPath.resolveFile("card_orb")

        // 小尺寸的能量图标（战斗中，牌堆预览）
        val ENERGY_ORB: String = ImgPath.characterPath.resolveFile("cost_orb")

        private val BG_512: AbsoluteScopeWithFormat = ImgRootFolder.getImgFolder().createScope("512")

        // 攻击牌的背景（小尺寸）
        val BG_ATTACK_512: String = BG_512.resolveFile("bg_attack_512")

        // 能力牌的背景（小尺寸）
        val BG_POWER_512: String = BG_512.resolveFile("bg_power_512")

        // 技能牌的背景（小尺寸）
        val BG_SKILL_512: String = BG_512.resolveFile("bg_skill_512")

        private val BG_1024: AbsoluteScopeWithFormat = ImgRootFolder.getImgFolder().createScope("1024")

        // 攻击牌的背景（大尺寸）
        val BG_ATTACK_1024: String = BG_1024.resolveFile("bg_attack")

        // 能力牌的背景（大尺寸）
        val BG_POWER_1024: String = BG_1024.resolveFile("bg_power")

        // 技能牌的背景（大尺寸）
        val BG_SKILL_1024: String = BG_1024.resolveFile("bg_skill")

        //选英雄界面的角色图标、选英雄时的背景图片
        val LUPA_CHARACTER_BUTTON: String = ImgPath.characterPath.resolveFile("Character_Button")

        // 人物选择界面的立绘
        val LUPA_CHARACTER_PORTRAIT: String = ImgPath.characterPath.resolveFile("Character_Portrait")
        val MASO_CHARACTER_PORTRAIT: String = ImgPath.characterPath.resolveFile("Character_Maso_Portrait")

        val SEX_COLOR: Color = Color(250.0f / 255.0f, 20.0f / 255.0f, 147.0f / 255.0f, 1.0f)
        val BG_ATTACK_SEMEN: String = ImgPath.uiPath.resolveFile("bg_attack_semen")
        val BG_ATTACK_512_SEMEN: String = ImgPath.uiPath.resolveFile("bg_attack_512_semen")

//        @JvmStatic
//        fun initialize()
//        {
//        }

    }

    // 为原版人物枚举、卡牌颜色枚举扩展的枚举
    object LupaEnums
    {
        @SpireEnum lateinit var LUPA_Character: PlayerClass

        @SpireEnum(name = "SPTT_LUPA_PINK") lateinit var LUPA_CARD: CardColor

        @SpireEnum(name = "SPTT_LUPA_PINK") lateinit var LUPA_LIBRARY: LibraryType
    }

    object TzeentchEnums
    {
        @SpireEnum lateinit var TZEENTCH_Character: PlayerClass
    }

    object MasoEnums
    {
        @SpireEnum lateinit var MASO_Character: PlayerClass

        @SpireEnum(name = "SPTT_MASO_PINK") lateinit var MASO_CARD: CardColor

        @SpireEnum(name = "SPTT_MASO_PINK") lateinit var MASO_LIBRARY: LibraryType
    }

    object GeneralEnums
    {
        @SpireEnum lateinit var GENERAL_Virtual_Character: PlayerClass

        @SpireEnum(name = "SPTT_GENERAL_PINK") lateinit var GENERAL_CARD: CardColor

        @SpireEnum(name = "SPTT_GENERAL_PINK") lateinit var GENERAL_LIBRARY: LibraryType
    }

    object TempCardEnums
    {
        @SpireEnum lateinit var TempCard_Virtual_Character: PlayerClass

        @SpireEnum(name = "SPTT_TEMP_PINK") lateinit var TempCard_CARD: CardColor

        @SpireEnum(name = "SPTT_TEMP_PINK") lateinit var TempCard_LIBRARY: LibraryType
    }

    object CanOnlyDamageDamageType
    {
        @SpireEnum lateinit var UnBlockAbleDamageType: DamageType

        @SpireEnum lateinit var NoTriggerLupaAndMasoRelicHpLose: DamageType
    }

    object CardTagsType
    {
        @SpireEnum lateinit var CruelTorture: AbstractCard.CardTags

        @SpireEnum lateinit var BodyModification: AbstractCard.CardTags

        @SpireEnum lateinit var InsideEjaculation: AbstractCard.CardTags

        @SpireEnum lateinit var OutsideEjaculation: AbstractCard.CardTags
    }

    companion object
    {
        /**
         * 作为随机数种子之类的
         */
        const val MAGIC_NUMBER_0: Int = 19260817

        const val CODER_COMPUTER_NAME: String = "DESKTOP-VK8L63C"
        const val COMPUTERNAME: String = "COMPUTERNAME"
        const val GURO_VERSION_TIPS: String = "#GuroVersion#"
        var cards: MutableMap<String, CardStringsWillMakeFlavorSet> = HashMap()
        var powers: MutableMap<String, PowerStringsSet> = HashMap()
        var modifiers: MutableMap<String, ModifierStringsSet> = HashMap()
        var orbs: MutableMap<String, OrbStringsSet> = HashMap()
        var uiStrings: MutableMap<String, UIStringsSet> = HashMap()

        //    public static Object[] allData = Arrays.stream(new Map[]{cards, powers, modifiers, orbs, uiStrings}).toArray();
        fun forEachData(consumer: (Map<String, HasDifferentVersionStringSet<*>>) -> Unit)
        {
            consumer(cards)
            consumer(powers)
            consumer(modifiers)
            consumer(orbs)
            consumer(uiStrings)
        }

        fun forEachDataEachValue(consumer: (HasDifferentVersionStringSet<*>) -> Unit)
        {
            forEachData { it.values.forEach(consumer) }
        }

        fun getModID(): String = SuperstitioModSetup.MOD_NAME + "Mod"


        private val resourcesFilesPath = AbsoluteScope.create(getModID() + "Resources")
        private val localizationPath = resourcesFilesPath.createScope("localization").withFormat("json")

        fun makeLocalizationPath(language: GameLanguage?, filename: String): String
        {
            val ret = when (language)
            {
                GameLanguage.ZHS -> "zhs"
                GameLanguage.ZHT -> "zhs"
                GameLanguage.ENG -> "eng"
                else             -> "eng"
            }
            return localizationPath.createScope(ret).resolveFile(filename)
        }

//
//        fun makeFolderTotalString(vararg strings: String): String
//        {
//            if (strings.isEmpty()) return ""
//            val totalString = StringBuilder()
//            for (string in strings) totalString.append("/").append(string)
//            return totalString.toString()
//        }

        object ImgRootFolder
        {
            private val pngResourcesFilesPath = resourcesFilesPath.withFormat("png")
            val imgNSFWFolder = pngResourcesFilesPath.createScope("img")
            val imgSFWFolder = pngResourcesFilesPath.createScope("imgSFW")
            // TODO GURO向卡牌还没有成功屏蔽，所以没办法上线（当然，现在本来也只是重构，上线不上线无所谓）
            fun getImgFolder() = if (SuperstitioConfig.isEnableSFW())
            {
                imgSFWFolder
            }
            else
            {
                imgNSFWFolder
            }
        }

        object ImgPath
        {
            val cardsPath get() = ImgSubPath.cardsPath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val uiPath get() = ImgSubPath.uiPath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val characterPath get() = ImgSubPath.characterPath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val relicsPath get() = ImgSubPath.relicsPath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val relicsOutlinePath get() = ImgSubPath.relicsOutlinePath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val relicsLargePath get() = ImgSubPath.relicsLargePath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val orbsPath get() = ImgSubPath.orbsPath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val powersPath get() = ImgSubPath.powersPath.pinToAbsolute(ImgRootFolder.getImgFolder())
            val eventsPath get() = ImgSubPath.eventsPath.pinToAbsolute(ImgRootFolder.getImgFolder())
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

        fun tryGetImgPath(
            relativeScope: RelativeScope,
            fileNameMaybeDirty: String,
            defaultFileName: String,
        ): String
        {
            return tryGetImgPath(relativeScope, fileNameMaybeDirty, relativeScope.resolveFile(defaultFileName))
        }

        fun tryGetImgPath(
            relativeScope: RelativeScope,
            fileNameMaybeDirty: String,
            defaultPath: RelativeFilePath,
        ): String
        {
            val imgAbsoluteScope = ImgRootFolder.getImgFolder()
            return DataUtility.tryGetImgPath(
                relativeScope.resolveFile(DataUtility.getIdOnly(fileNameMaybeDirty)), defaultPath, imgAbsoluteScope,
            )
            {
                    relativeFilePath: RelativeFilePath,
                    defaultPath: RelativeFilePath,
                    absoluteScope: AbsoluteScopeWithFormat,
                ->
                if (!isRunningInCoderComputer)
                    return@tryGetImgPath

                try
                {
                    makeNeedDrawPicture(relativeFilePath, defaultPath, absoluteScope)
                }
                catch (e: IOException)
                {
                    Logger.error(e)
                }
            }
        }

        //生成所有的需要绘制的图片，方便检查
        @Throws(IOException::class)
        private fun makeNeedDrawPicture(
            relativeFilePath: RelativeFilePath,
            defaultFilePath: RelativeFilePath,
            absoluteScope: AbsoluteScopeWithFormat,
        )
        {
            val relativePath = Path(relativeFilePath.pinToAbsolute(absoluteScope)).normalize()
            val defaultPath = Path(defaultFilePath.pinToAbsolute(absoluteScope)).normalize()
            val needDrawPath = Path("needDraw").resolve(relativePath).normalize()

            val fileName = relativePath.fileName.toString()
            val defaultName = defaultPath.fileName.toString()

            val filePathString = relativePath.parent.toString()
            val defaultPathString = defaultPath.parent.toString()


            if (fileName.contains("32"))
                return
            if (fileName.contains("84") && noNeedDrawPower84(fileName))
                return
            if (noNeedImgName(fileName))
                return

            if (defaultPathString.contains("outline"))
                return
            if (defaultPathString.contains("large"))
                return
            if (filePathString.contains("orb"))
                return

            val isCard = filePathString.contains("card")
            val defaultFileHandle = Gdx.files.internal(
                if (isCard)
                    relativeFilePath.withNewFileName(fileName + "_p").pinToAbsolute(absoluteScope)
                else
                    defaultFilePath.pinToAbsolute(absoluteScope)
            )

            val needDrawFilePath = relativeFilePath.withNewFileName(
                if (isCard)
                    fileName + "_p"
                else
                    fileName
            ).pinToAbsolute(absoluteScope.createScope("needDraw"))

            val defaultFileCopyTo = File(needDrawFilePath)
            val pattern = Pattern.compile("^(.+/)[^/]+$")
            val matcher = pattern.matcher(needDrawFilePath)
            val totalFolderPath = if (matcher.find())
                matcher.group(1)
            else
                needDrawFilePath
            File(totalFolderPath).mkdirs()
            if (!defaultFileCopyTo.exists())
                Files.copy(defaultFileHandle.read(), defaultFileCopyTo.toPath())
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

            fun replaceOneField(
                field: Field, obj: Any, wordReplace: WordReplace,
            )
            {
                val fieldObj = field[obj]
                if (fieldObj is String)
                {
                    field[obj] = fieldObj.replace(wordReplace.WordOrigin, wordReplace.WordReplace)
                    return
                }
                else if (fieldObj is Array<*> && fieldObj.isArrayOf<String>())
                {
                    @Suppress("UNCHECKED_CAST")
                    val values = fieldObj as? Array<String?>
                    if (values.isNullOrEmpty())
                        return
                    val list = arrayOfNulls<String>(values.size)
                    for (i in values.indices)
                    {
                        list[i] = values[i]?.replace(wordReplace.WordOrigin, wordReplace.WordReplace)
                    }

                    field[obj] = list
                    return
                }
            }

            for (field in obj.javaClass.declaredFields)
            {
                field.isAccessible = true
                try
                {
                    replaceOneField(field, obj, wordReplace)
                }
                catch (e: IllegalAccessException)
                {
                    Logger.error(e)
                }
            }

        }

        fun <T : HasDifferentVersionStringSet<*>> loadCustomStringsFile(
            fileName: String, target: MutableMap<String, T>, tSetClass: Class<T>,
        )
        {
            superstitioapi.Logger.debug("loadJsonStrings: " + tSetClass.typeName)
            val jsonString = Gdx.files.internal(makeLocalizationPath(Settings.language, fileName))
                .readString(StandardCharsets.UTF_8.toString())
            val typeToken =
                GetTypeOfMapByAComplexFunctionBecauseTheMotherfuckerGenericProgrammingWayTheFuckingJavaUse(
                    tSetClass,
                    DataManager::class.java.declaredFields
                )
            val gson = Gson()
            val map = gson.fromJson<Map<String, T>>(jsonString, typeToken)
            map.forEach { (id: String, strings: T) ->
                strings.initial()
                if (strings is HasTextID) strings.textID = id
            }
            target.putAll(map)
        }

        fun makeAllSFWLocalizationForCoder()
        {
            //生成所有的SFW本地化
            fun <T> makeSFWLocalization(data: Map<String, T>, fileName: String)
            {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gson.toJson(data)
                Gdx.files.local(makeLocalizationPath(Settings.language, fileName + "_sfw"))
                    .writeString(jsonString, false, "UTF-8")
            }

            //生成所有的SFW本地化
            fun <T> makeSFWLocalization(data: List<T>, fileName: String)
            {
                val gson = GsonBuilder().setPrettyPrinting().create()
                val jsonString = gson.toJson(data)
                Gdx.files.local(makeLocalizationPath(Settings.language, fileName + "_sfw"))
                    .writeString(jsonString, false, "UTF-8")
            }

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
                if (s.contains(getModID())) copyRelicsStrings[s] = t
            }
            makeSFWLocalization(copyRelicsStrings, "relics")
            val allKeywords = StringsSetManager.allKeywords.map(SuperstitioKeyWord::makeCopy)
            for (keyWord in allKeywords)
            {
                keyWord.NAMES = arrayOf("")
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
        ) fun <T : HasSFWVersion<*>?> leaveOnlySFW(stringSet: T, tClass: Class<T>): T
        {
            val tryCopyStringSet = stringSet!!.makeSFWCopy()
            val copyStringSet = if (tClass.isInstance(stringSet)) tClass.cast(tryCopyStringSet)
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

        private val isRunningInCoderComputer: Boolean
            get() = System.getenv()[COMPUTERNAME] == CODER_COMPUTER_NAME


//        private fun getImgFolderPath(path: String, file: String): String
//        {
//            var allLevelPath = resourcesFilesPath.createScope("img") + "img" + path + "/" + file
//            var sfwLevelPath = resourcesFilesPath.createScope("imgSFW") + "imgSFW" + path + "/" + file
//
//            if (path.contains(GURO_VERSION_TIPS) || file.contains(GURO_VERSION_TIPS))
//            {
//                sfwLevelPath = sfwLevelPath.replace(GURO_VERSION_TIPS.toRegex(), "")
//                allLevelPath = allLevelPath.replace(GURO_VERSION_TIPS.toRegex(), "")
//                if (!SuperstitioConfig.isEnableGuroCharacter())
//                {
//                    return sfwLevelPath
//                }
//            }
//
//
//            return if (!SuperstitioConfig.isEnableSFW())
//            {
//                allLevelPath
//            }
//            else sfwLevelPath
//        }

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
            val powerIds = setOf(
                SexPlateArmorPower.POWER_ID,
                TimeStopPower.POWER_ID,
                ChokeChokerPower.POWER_ID,
                SexualHeat.POWER_ID,
                DelayHpLosePower_ApplyOnlyOnVictory.POWER_ID,
                DelayHpLosePower_ApplyOnAttacked.POWER_ID,
                DelayHpLosePower_HealOnVictory.POWER_ID,
                DelayRemoveDelayHpLosePower.POWER_ID,
                FloorSemen.POWER_ID,
                InsideSemen.POWER_ID,
                OutsideSemen.POWER_ID,
                DrinkSemenBeerPower.POWER_ID
            )

            return powerIds.any { DataUtility.getIdOnly(it) == checkName }
        }
    }
}

fun SPTT_Color.ToColorEnums(): CardColor
{
    return when (this)
    {
        SPTT_Color.TempColor    -> TempCardEnums.TempCard_CARD
        SPTT_Color.GeneralColor -> GeneralEnums.GENERAL_CARD
        SPTT_Color.LupaColor    -> LupaEnums.LUPA_CARD
        SPTT_Color.MasoColor    -> MasoEnums.MASO_CARD
    }
}
