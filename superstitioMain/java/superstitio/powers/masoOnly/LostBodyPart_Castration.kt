package superstitio.powers.masoOnly

import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card.BodyPart

//去势
class LostBodyPart_Castration : LostBodyPart(POWER_ID)
{
    override fun banedBodyPart(): Array<BodyPart?>
    {
        return arrayOf(BodyPart.genital)
    }

    companion object
    {
        val POWER_ID: String = DataManager.MakeTextID(LostBodyPart_Castration::class.java)
    }
}
