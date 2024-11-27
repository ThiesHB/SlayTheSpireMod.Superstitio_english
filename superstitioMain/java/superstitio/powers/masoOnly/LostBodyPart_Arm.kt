package superstitio.powers.masoOnly

import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card.BodyPart

class LostBodyPart_Arm : LostBodyPart(POWER_ID) {
    override fun banedBodyPart(): Array<BodyPart?> {
        return arrayOf(BodyPart.hand)
    }

    companion object {
        val POWER_ID: String = DataManager.MakeTextID(LostBodyPart_Arm::class.java)
    }
}
