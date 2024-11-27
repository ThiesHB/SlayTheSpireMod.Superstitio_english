package superstitio.cards.lupa.BaseCard

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier
import superstitio.DataManager
import superstitio.cardModifier.modifiers.block.SexBlock
import superstitio.cards.CardOwnerPlayerManager.IsLupaCard
import superstitio.cards.general.BaseCard.Invite

class Invite_Lupa : Invite(ID), IsLupaCard {
    override fun makeNewBlockType(): AbstractBlockModifier {
        return SexBlock()
    }

    companion object {
        val ID: String = DataManager.MakeTextID(Invite::class.java.simpleName, Invite_Lupa::class.java)
    }
}
