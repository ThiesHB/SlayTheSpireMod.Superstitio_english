package superstitioapi.hangUpCard

interface Card_TriggerHangCardManually
{
    fun forceFilterCardOrbToHoveredMode(orb: CardOrb): Boolean

    fun forceChangeOrbCounterShown(orb: CardOrb): Int
}
