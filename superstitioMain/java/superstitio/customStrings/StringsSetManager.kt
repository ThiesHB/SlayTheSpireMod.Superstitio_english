package superstitio.customStrings

import com.evacipated.cardcrawl.mod.stslib.Keyword
import superstitio.DataManager
import superstitio.SuperstitioModSetup
import superstitio.customStrings.interFace.WordReplace
import superstitio.customStrings.stringsSet.*
import java.util.*
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collectors

object StringsSetManager {
    private val powerFiles = arrayOf("power", "power_Lupa", "power_Maso")
    private val modifierFiles = arrayOf("modifier_damage", "modifier_block")
    private val cardFiles = arrayOf(
        "card_Lupa", "card_General", "card_Maso", "card_FuckJobAndBase", "card_TempCard",
        "card_Colorless"
    )
    private var wordReplaceRules: MutableList<WordReplace>? = null
    @JvmStatic
    fun loadModifierStringsSet() {
        for (modifierFile in modifierFiles) DataManager.loadCustomStringsFile(
            modifierFile,
            DataManager.modifiers,
            ModifierStringsSet::class.java
        )
    }
    @JvmStatic
    fun loadPowerStringsSet() {
        for (powerFile in powerFiles) DataManager.loadCustomStringsFile(
            powerFile,
            DataManager.powers,
            PowerStringsSet::class.java
        )
    }
    @JvmStatic
    fun loadCardStringsSet() {
        for (cardFile in cardFiles) DataManager.loadCustomStringsFile(
            cardFile,
            DataManager.cards,
            CardStringsWillMakeFlavorSet::class.java
        )
    }
    @JvmStatic
    fun loadAllStrings() {
        loadCardStringsSet()
        loadModifierStringsSet()
        loadPowerStringsSet()
        loadOrbStringsSet()
        loadUIStringsSet()

        makeSFWVersion()
    }
    @JvmStatic
    fun makeWordReplaceRule(): MutableList<WordReplace> {
        if (SuperstitioModSetup.SEAL_MANUAL_SFW) {
            return ArrayList()
        }
        if (!wordReplaceRules.isNullOrEmpty())
            return wordReplaceRules as MutableList<WordReplace>
        val sfwReplaces =
            Arrays.stream(
                DataManager.makeJsonStringFromFile(
                    "SFW_replace",
                    Array<WordReplace>::class.java
                )
            ).collect(Collectors.toList())
        if (DataManager.cards.isNotEmpty()) sfwReplaces.addAll(
            CardStringsWillMakeFlavorSet.makeCardNameReplaceRules(
                ArrayList(DataManager.cards.values)
            )
        )
        if (DataManager.modifiers.isNotEmpty()) sfwReplaces.addAll(
            ModifierStringsSet.makeModifierNameReplaceRules(
                ArrayList(DataManager.modifiers.values)
            )
        )
        sfwReplaces.addAll(SuperstitioKeyWord.makeKeywordNameReplaceRules(
            SuperstitioKeyWord.getAndRegisterKeywordsFormFile()))
        wordReplaceRules =
            sfwReplaces.filter { wordReplace: WordReplace -> !wordReplace.hasNullOrEmpty() }
            .toMutableList()
        return wordReplaceRules as MutableList<WordReplace>
    }
    @JvmStatic
    fun makeSFWVersion() {
//        if (SuperstitioConfig.isEnableSFW()) {
        makeSFWWordForStringsSet()
        //        }
    }
    @JvmStatic
    fun makeKeyWords() {
        val keywords = allKeywords
        keywords.forEach(Consumer { keyWord: SuperstitioKeyWord? -> keyWord!!.makeSFWVersion(makeWordReplaceRule()) })
        keywords.forEach(Consumer { obj: SuperstitioKeyWord? -> obj!!.addToGame() })
        return
    }

    val allKeywords: List<SuperstitioKeyWord?>
        get() {
            val keywordsSFW: MutableList<SuperstitioKeyWord?> = ArrayList()
            keywordsSFW.addAll(SuperstitioKeyWord.getAndRegisterKeywordsFormFile())
            keywordsSFW.addAll(Arrays.stream(ModifierStringsSet.MakeKeyWords())
                .map<SuperstitioKeyWord?>(
                    Function<Keyword, SuperstitioKeyWord?>(SuperstitioKeyWord.Companion::STSLibKeyWordToThisType)
                ).collect(Collectors.toList())
            )
            return keywordsSFW
        }

    private fun loadUIStringsSet() {
        DataManager.loadCustomStringsFile(
            "UIStringsWithSFW",
            DataManager.uiStrings,
            UIStringsSet::class.java
        )
    }

    private fun loadOrbStringsSet() {
        DataManager.loadCustomStringsFile(
            "orb",
            DataManager.orbs,
            OrbStringsSet::class.java
        )
    }

    private fun makeSFWWordForStringsSet() {
        val wordReplaces = makeWordReplaceRule()

        DataManager.cards.forEach(BiConsumer<String?, CardStringsWillMakeFlavorSet> { string: String?, card: CardStringsWillMakeFlavorSet ->
            card.setupSFWStringByWordReplace(
                wordReplaces
            )
        })
        DataManager.powers.forEach((BiConsumer<String?, PowerStringsSet> { string: String?, power: PowerStringsSet ->
            power.setupSFWStringByWordReplace(
                wordReplaces
            )
        }))
        DataManager.modifiers.forEach((BiConsumer<String?, ModifierStringsSet> { string: String?, modifier: ModifierStringsSet ->
            modifier.setupSFWStringByWordReplace(
                wordReplaces
            )
        }))
        DataManager.orbs.forEach((BiConsumer<String?, OrbStringsSet> {
            string: String?, orb: OrbStringsSet ->
            orb.setupSFWStringByWordReplace(wordReplaces)
        }))
    }
}
