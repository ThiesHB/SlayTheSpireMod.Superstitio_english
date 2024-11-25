package superstitio.cardModifier.modifiers.card

import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.DataManager
import superstitio.DataManager.CardTagsType
import superstitio.customStrings.stringsSet.UIStringsSet

class CruelTortureTag : AbstractCardTagModifier() {
    override val uiStrings: UIStringsSet
        get() = Companion.uiStrings!!

    override fun getID(): String {
        return ID
    }

    override val tag: AbstractCard.CardTags
        get() = CardTagsType.CruelTorture

    companion object {
        protected val ID: String = DataManager.MakeTextID(CruelTortureTag::class.java)
        private val uiStrings: UIStringsSet = getUIStringsWithSFW(ID)
    }
}
