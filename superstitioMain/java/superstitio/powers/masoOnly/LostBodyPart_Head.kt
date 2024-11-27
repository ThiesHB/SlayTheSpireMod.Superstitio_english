package superstitio.powers.masoOnly

import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card.BodyPart

class LostBodyPart_Head : LostBodyPart(POWER_ID)
{
    override fun banedBodyPart(): Array<BodyPart?>
    {
        return arrayOf(BodyPart.head, BodyPart.mouth)
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(LostBodyPart_Head::class.java)
    }
}
