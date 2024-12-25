package superstitio.delayHpLose

import com.evacipated.cardcrawl.modthespire.lib.SpireField
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.characters.AbstractPlayer
import superstitioapi.powers.interfaces.TriPredicate

@SpirePatch2(clz = AbstractPlayer::class, method = "<class>")
object IsImmunityFields
{
    @JvmField
    var checkShouldImmunity: SpireField<TriPredicate<AbstractPlayer, DamageInfo?, Int>?> =
        SpireField<TriPredicate<AbstractPlayer, DamageInfo?, Int>?> { null }
}