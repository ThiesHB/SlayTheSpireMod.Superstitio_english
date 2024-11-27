package superstitio.powers.masoOnly

import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card.BodyPart

class LostBodyPart_Leg : LostBodyPart(POWER_ID)
{
    override fun banedBodyPart(): Array<BodyPart?>
    {
        return arrayOf(BodyPart.leg)
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(LostBodyPart_Leg::class.java)
    }
}
