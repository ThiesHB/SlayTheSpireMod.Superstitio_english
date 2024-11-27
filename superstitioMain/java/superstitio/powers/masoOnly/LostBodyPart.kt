package superstitio.powers.masoOnly

import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.general.FuckJob_Card.BodyPart
import superstitio.powers.AbstractSuperstitioPower
import java.util.*
import java.util.stream.Collectors

abstract class LostBodyPart protected constructor(id: String) :
    AbstractSuperstitioPower(id, AbstractDungeon.player, -1, PowerType.BUFF, true)
{
    private val banedBodyPart: List<BodyPart?>

    init
    {
        banedBodyPart = Arrays.stream(banedBodyPart()).collect(Collectors.toList())
    }

    abstract fun banedBodyPart(): Array<BodyPart?>

    private fun canUseBody(card: FuckJob_Card): Boolean
    {
        return !banedBodyPart.contains(FuckJob_Card.getBodyPartType(card))
    }

    override fun updateDescriptionArgs()
    {
    }

    override fun canPlayCard(card: AbstractCard): Boolean
    {
        if (card !is FuckJob_Card) return true
        return canUseBody(card as FuckJob_Card)
    }
}
