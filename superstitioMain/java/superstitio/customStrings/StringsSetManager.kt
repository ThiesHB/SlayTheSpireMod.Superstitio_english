package superstitio.customStrings

import superstitio.DataManager
import superstitio.SuperstitioModSetup
import superstitio.customStrings.interFace.WordReplace
import superstitio.customStrings.stringsSet.*
import java.util.*

object StringsSetManager
{
    private val powerFiles = arrayOf("power", "power_Lupa", "power_Maso")
    private val modifierFiles = arrayOf("modifier_damage", "modifier_block")
    private val cardFiles = arrayOf(
        "card_Lupa", "card_General", "card_Maso", "card_FuckJobAndBase", "card_TempCard",
        "card_Colorless"
    )
    private var wordReplaceRules: MutableList<WordReplace>? = null

    @JvmStatic
    fun loadModifierStringsSet()
    {
        for (modifierFile in modifierFiles) DataManager.loadCustomStringsFile(
            modifierFile,
            DataManager.modifiers,
            ModifierStringsSet::class.java
        )
    }

    @JvmStatic
    fun loadPowerStringsSet()
    {
        for (powerFile in powerFiles) DataManager.loadCustomStringsFile(
            powerFile,
            DataManager.powers,
            PowerStringsSet::class.java
        )
    }

    @JvmStatic
    fun loadCardStringsSet()
    {
        for (cardFile in cardFiles) DataManager.loadCustomStringsFile(
            cardFile,
            DataManager.cards,
            CardStringsWillMakeFlavorSet::class.java
        )
    }

    @JvmStatic
    fun loadAllStrings()
    {
        loadCardStringsSet()
        loadModifierStringsSet()
        loadPowerStringsSet()
        loadOrbStringsSet()
        loadUIStringsSet()

        makeSFWVersion()
    }

    @JvmStatic
    fun makeWordReplaceRule(): MutableList<WordReplace>
    {
        if (SuperstitioModSetup.SEAL_MANUAL_SFW)
            return ArrayList()
        if (!wordReplaceRules.isNullOrEmpty())
            return wordReplaceRules!!
        val sfwReplaces =
            DataManager.makeJsonStringFromFile("SFW_replace", Array<WordReplace>::class.java).toMutableList()
        if (DataManager.cards.isNotEmpty())
            sfwReplaces.addAll(
                CardStringsWillMakeFlavorSet.makeCardNameReplaceRules(ArrayList(DataManager.cards.values))
            )
        if (DataManager.modifiers.isNotEmpty())
            sfwReplaces.addAll(
                ModifierStringsSet.makeModifierNameReplaceRules(ArrayList(DataManager.modifiers.values))
            )
        sfwReplaces.addAll(
            SuperstitioKeyWord.makeKeywordNameReplaceRules(SuperstitioKeyWord.getAndRegisterKeywordsFormFile())
        )
        wordReplaceRules = sfwReplaces.filterNot { it.hasNullOrEmpty() }.toMutableList()
        return wordReplaceRules!!
    }

    @JvmStatic
    fun makeSFWVersion()
    {
//        if (SuperstitioConfig.isEnableSFW()) {
        makeSFWWordForStringsSet()
        //        }
    }

    @JvmStatic
    fun makeKeyWords()
    {
        val keywords = allKeywords
        keywords.forEach { keyWord -> keyWord.makeSFWVersion(makeWordReplaceRule()) }
        keywords.forEach { obj -> obj.addToGame() }
        return
    }

    val allKeywords: List<SuperstitioKeyWord>
        get()
        {
            val keywordsSFW: MutableList<SuperstitioKeyWord> = ArrayList()
            keywordsSFW.addAll(SuperstitioKeyWord.getAndRegisterKeywordsFormFile())
            keywordsSFW.addAll(ModifierStringsSet.MakeKeyWords().map(SuperstitioKeyWord::STSLibKeyWordToThisType))
            return keywordsSFW
        }

    private fun loadUIStringsSet()
    {
        DataManager.loadCustomStringsFile(
            "UIStringsWithSFW",
            DataManager.uiStrings,
            UIStringsSet::class.java
        )
    }

    private fun loadOrbStringsSet()
    {
        DataManager.loadCustomStringsFile(
            "orb",
            DataManager.orbs,
            OrbStringsSet::class.java
        )
    }

    private fun makeSFWWordForStringsSet()
    {
        val wordReplaces = makeWordReplaceRule()

        DataManager.cards.values.forEach { it.setupSFWStringByWordReplace(wordReplaces) }
        DataManager.powers.values.forEach { it.setupSFWStringByWordReplace(wordReplaces) }
        DataManager.modifiers.values.forEach { it.setupSFWStringByWordReplace(wordReplaces) }
        DataManager.orbs.values.forEach { it.setupSFWStringByWordReplace(wordReplaces) }
    }
}
