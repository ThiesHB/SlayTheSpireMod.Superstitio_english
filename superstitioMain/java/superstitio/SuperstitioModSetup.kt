package superstitio

import basemod.AutoAdd
import basemod.BaseMod
import basemod.ReflectionHacks
import basemod.abstracts.CustomRelic
import basemod.helpers.RelicType
import basemod.interfaces.*
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.core.Settings
import com.megacrit.cardcrawl.localization.*
import com.megacrit.cardcrawl.unlock.UnlockTracker
import superstitio.DataManager.SPTT_DATA
import superstitio.DataManager.SPTT_DATA.*
import superstitio.cards.SuperstitioCard
import superstitio.characters.Lupa
import superstitio.characters.Maso
import superstitio.characters.Tzeentch
import superstitio.customStrings.StringsSetManager
import superstitio.customStrings.SuperstitioKeyWord
import superstitio.customStrings.SuperstitioKeyWord.WillMakeSuperstitioKeyWords
import superstitio.customStrings.interFace.HasDifferentVersionStringSet
import superstitio.customStrings.interFace.WordReplace
import superstitio.customStrings.stringsSet.*
import superstitio.relics.SuperstitioRelic
import superstitio.relics.interFace.SelfRelic
import superstitioapi.relicToBlight.InfoBlight
import superstitioapi.relicToBlight.InfoBlight.BecomeInfoBlight
import superstitioapi.shader.ShaderUtility
import java.util.*
import java.util.function.Consumer

@SpireInitializer
class SuperstitioModSetup : EditStringsSubscriber, EditRelicsSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
    EditCharactersSubscriber, AddAudioSubscriber, PostInitializeSubscriber
{
    var data: DataManager

    init
    {
        BaseMod.subscribe(this)
        SuperstitioConfig.loadConfig()

        // 这里注册颜色
        data = DataManager()
        BaseMod.addColor(
            LupaEnums.LUPA_CARD,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR,
            data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
            data.spttData.ENERGY_ORB,
            data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
            data.spttData.BIG_ORB, data.spttData.SMALL_ORB
        )

        BaseMod.addColor(
            TempCardEnums.TempCard_CARD,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR,
            data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
            data.spttData.ENERGY_ORB,
            data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
            data.spttData.BIG_ORB, data.spttData.SMALL_ORB
        )

        BaseMod.addColor(
            GeneralEnums.GENERAL_CARD,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR,
            data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
            data.spttData.ENERGY_ORB,
            data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
            data.spttData.BIG_ORB, data.spttData.SMALL_ORB
        )

        BaseMod.addColor(
            MasoEnums.MASO_CARD,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR, SPTT_DATA.SEX_COLOR,
            SPTT_DATA.SEX_COLOR,
            data.spttData.BG_ATTACK_512, data.spttData.BG_SKILL_512, data.spttData.BG_POWER_512,
            data.spttData.ENERGY_ORB,
            data.spttData.BG_ATTACK_1024, data.spttData.BG_SKILL_1024, data.spttData.BG_POWER_1024,
            data.spttData.BIG_ORB, data.spttData.SMALL_ORB
        )

        Logger.run("Done $this subscribing")
        Logger.run("Adding mod settings")
    }

    override fun receiveEditCharacters()
    {
        //添加角色到MOD中
        BaseMod.addCharacter(
            Maso(CardCrawlGame.playerName),
            data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.MASO_CHARACTER_PORTRAIT, MasoEnums.MASO_Character
        )
        BaseMod.addCharacter(
            Lupa(CardCrawlGame.playerName),
            data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.LUPA_CHARACTER_PORTRAIT, LupaEnums.LUPA_Character
        )
        BaseMod.addCharacter(
            Tzeentch(CardCrawlGame.playerName),
            data.spttData.LUPA_CHARACTER_BUTTON, data.spttData.LUPA_CHARACTER_PORTRAIT, TzeentchEnums.TZEENTCH_Character
        )
    }

    override fun receiveEditCards()
    {
        //将卡牌添加
        AutoAdd(MOD_NAME.lowercase(Locale.getDefault()))
            .packageFilter(SuperstitioCard::class.java)
            .setDefaultSeen(true)
            .cards()
    }

    override fun receiveAddAudio()
    {
    }

    override fun receiveEditRelics()
    {
        AutoAdd(MOD_NAME.lowercase(Locale.getDefault()))
            .packageFilter(SuperstitioRelic::class.java)
            .any(CustomRelic::class.java) { info: AutoAdd.Info, relic: CustomRelic ->
                if (relic is BecomeInfoBlight)
                {
                    InfoBlight.initInfoBlight(relic)
                    UnlockTracker.relicSeenPref.putInteger(relic.relicId, 1)
                    UnlockTracker.relicSeenPref.flush()
                    relic.isSeen = true
                    return@any
                }
                if (relic is SelfRelic) BaseMod.addRelicToCustomPool(relic, (relic as SelfRelic).relicOwner)
                else BaseMod.addRelic(relic, RelicType.SHARED)
                if (info.seen)
                {
                    UnlockTracker.markRelicAsSeen(relic.relicId)
                }
            }
    }

    override fun receiveEditStrings()
    {
        Logger.run("Beginning to edit strings for mod with ID: " + DataManager.Companion.getModID())
        if (!SEAL_MANUAL_SFW)
        {
            StringsSetManager.loadAllStrings()
            BaseMod.loadCustomStringsFile(
                CharacterStrings::class.java,
                DataManager.makeLocalizationPath(
                    Settings.language,
                    if (SuperstitioConfig.isEnableSFW()) "character_LupaSFW" else "character_Lupa"
                )
            )
            BaseMod.loadCustomStringsFile(
                CharacterStrings::class.java,
                DataManager.makeLocalizationPath(Settings.language, "character_General")
            )
            BaseMod.loadCustomStringsFile(
                RelicStrings::class.java,
                DataManager.makeLocalizationPath(Settings.language, "relic_General")
            )
            BaseMod.loadCustomStringsFile(
                RelicStrings::class.java,
                DataManager.makeLocalizationPath(Settings.language, "relic_Lupa")
            )
            BaseMod.loadCustomStringsFile(
                RelicStrings::class.java,
                DataManager.makeLocalizationPath(Settings.language, "relic_Maso")
            )
            BaseMod.loadCustomStringsFile(
                UIStrings::class.java,
                DataManager.makeLocalizationPath(Settings.language, "UIStrings")
            )
            BaseMod.loadCustomStringsFile(
                MonsterStrings::class.java, DataManager.makeLocalizationPath(
                    Settings.language, "monsters"
                )
            )
            if (SuperstitioConfig.isEnableSFW())
            {
                makeSFWWordForOriginStrings()
            }
        }
        else
        {
            ShaderUtility.canUseShader = false
            DataManager.loadCustomStringsFile(
                "sfw/" + "cards" + "_sfw",
                DataManager.cards,
                CardStringsWillMakeFlavorSet::class.java
            )
            DataManager.loadCustomStringsFile(
                "sfw/" + "modifiers" + "_sfw",
                DataManager.modifiers,
                ModifierStringsSet::class.java
            )
            DataManager.loadCustomStringsFile(
                "sfw/" + "powers" + "_sfw",
                DataManager.powers,
                PowerStringsSet::class.java
            )
            DataManager.loadCustomStringsFile(
                "sfw/" + "orbs" + "_sfw",
                DataManager.orbs,
                OrbStringsSet::class.java
            )
            DataManager.loadCustomStringsFile(
                "sfw/" + "ui" + "_sfw",
                DataManager.uiStrings,
                UIStringsSet::class.java
            )

            StringsSetManager.makeSFWVersion()
            BaseMod.loadCustomStringsFile(
                RelicStrings::class.java,
                DataManager.makeLocalizationPath(Settings.language, "sfw/" + "relics" + "_sfw")
            )
            BaseMod.loadCustomStringsFile(
                CharacterStrings::class.java, DataManager.makeLocalizationPath(
                    Settings.language, "character_LupaSFW"
                )
            )
            BaseMod.loadCustomStringsFile(
                CharacterStrings::class.java, DataManager.makeLocalizationPath(
                    Settings.language, "character_General"
                )
            )
            BaseMod.loadCustomStringsFile(
                UIStrings::class.java,
                DataManager.makeLocalizationPath(Settings.language, "UIStrings")
            )
            BaseMod.loadCustomStringsFile(
                MonsterStrings::class.java, DataManager.makeLocalizationPath(
                    Settings.language, "monsters"
                )
            )
        }
        Logger.run("Done editing strings")
    }

    override fun receiveEditKeywords()
    {
        if (!SEAL_MANUAL_SFW)
        {
            StringsSetManager.makeKeyWords()
            DataManager.makeAllSFWLocalizationForCoder()
        }
        else
        {
            val keywordsSFW: MutableList<SuperstitioKeyWord?> = ArrayList<SuperstitioKeyWord?>(
                Arrays.asList(
                    *DataManager.makeJsonStringFromFile(
                        "sfw/" + "keywords" + "_sfw",
                        Array<SuperstitioKeyWord>::class.java
                    )
                )
            )

            DataManager.forEachData { data: Map<String, HasDifferentVersionStringSet<*>> ->
                data.forEach { (_: String, stringSet: HasDifferentVersionStringSet<*>) ->
                    if (stringSet is WillMakeSuperstitioKeyWords)
                    {
                        stringSet.getWillMakeKEYWORDS().toList()?.let(keywordsSFW::addAll)
                    }
                }
            }

            keywordsSFW.forEach(Consumer { obj: SuperstitioKeyWord? -> obj!!.registerKeywordFormFile() })
            keywordsSFW.forEach(Consumer { obj: SuperstitioKeyWord? -> obj!!.addToGame() })
        }
    }

    override fun receivePostInitialize()
    {
        SuperstitioConfig.setUpModOptions()
    }

    companion object
    {
        const val MOD_NAME: String = "Superstitio"

        /**
         * 人工启用sfw模式
         */
        const val SEAL_MANUAL_SFW: Boolean = false

        @JvmStatic
        fun initialize()
        {
            val mod = SuperstitioModSetup()
        }

        private fun makeSFWWordForOriginStrings()
        {
            val relicsStrings = ReflectionHacks.getPrivateStatic<Map<String, RelicStrings>>(
                LocalizedStrings::class.java, "relics"
            )

            val wordReplaces = StringsSetManager.makeWordReplaceRule()
            relicsStrings.forEach { (s: String, Strings: RelicStrings) ->
                if (s.contains(DataManager.Companion.getModID().lowercase(Locale.getDefault())) || s.contains(
                        DataManager.Companion.getModID()
                    )
                )
                {
                    Strings.FLAVOR = ""
                    wordReplaces.forEach(Consumer { wordReplace: WordReplace ->
                        DataManager.replaceStringsInObj(
                            Strings,
                            wordReplace
                        )
                    })
                }
            }

            ReflectionHacks.setPrivateStaticFinal(LocalizedStrings::class.java, "relics", relicsStrings)
        }
    }
}
