package superstitio.cardModifier.modifiers.card

import basemod.abstracts.AbstractCardModifier
import basemod.helpers.CardTags
import com.megacrit.cardcrawl.cards.AbstractCard
import superstitio.DataManager
import superstitio.customStrings.interFace.StringSetUtility
import superstitio.customStrings.stringsSet.UIStringsSet

abstract class AbstractCardTagModifier : AbstractCardModifier()
{
    protected abstract val uiStrings: UIStringsSet?

    protected abstract fun getID(): String

    protected abstract val tag: AbstractCard.CardTags?

    override fun shouldApply(card: AbstractCard): Boolean
    {
        return !card.hasTag(tag)
    }

    override fun onInitialApplication(card: AbstractCard)
    {
        CardTags.addTags(card, tag)
    }

    override fun onRemove(card: AbstractCard)
    {
        CardTags.removeTags(card, tag)
    }

    override fun identifier(card: AbstractCard): String
    {
        return getID()
    }

    override fun modifyName(cardName: String, card: AbstractCard): String
    {
        return String.format(uiStrings!!.getTEXT()[0], super.modifyName(cardName, card))
    }

    override fun makeCopy(): AbstractCardModifier
    {
        try
        {
            return this.javaClass.newInstance()
        }
        catch (var2: IllegalAccessException)
        {
            throw RuntimeException("SuperstitioApi failed to auto-generate makeCopy for cardModifier: " + this.getID())
        }
        catch (var2: InstantiationException)
        {
            throw RuntimeException("SuperstitioApi failed to auto-generate makeCopy for cardModifier: " + this.getID())
        }
    }

    companion object
    {
        fun getUIStringsWithSFW(uiID: String): UIStringsSet
        {
            return StringSetUtility.getCustomStringsWithSFW(
                uiID,
                DataManager.uiStrings,
                UIStringsSet::class.java
            )
        }
    }
}
