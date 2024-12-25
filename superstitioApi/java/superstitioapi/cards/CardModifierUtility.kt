package superstitioapi.cards

import basemod.abstracts.AbstractCardModifier
import basemod.cardmods.EtherealMod
import basemod.cardmods.ExhaustMod
import basemod.cardmods.RetainMod
import basemod.helpers.CardModifierManager
import com.megacrit.cardcrawl.cards.AbstractCard

fun AbstractCard.addCardModifier(modifier: AbstractCardModifier)
{
    CardModifierManager.addModifier(this, modifier)
}

fun AbstractCard.addExhaustMod()
{
    this.addCardModifier(ExhaustMod())
}

fun AbstractCard.addRetainMod()
{
    this.addCardModifier(RetainMod())
}

fun AbstractCard.addEtherealMod()
{
    this.addCardModifier(EtherealMod())
}
