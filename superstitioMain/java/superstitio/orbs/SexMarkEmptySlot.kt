package superstitio.orbs

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.orbs.AbstractOrb
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot
import superstitio.DataManager
import superstitio.customStrings.stringsSet.OrbStringsSet

class SexMarkEmptySlot @JvmOverloads constructor(
    x: Float = AbstractDungeon.player.drawX + AbstractDungeon.player.hb_x,
    y: Float = AbstractDungeon.player.drawY + AbstractDungeon.player.hb_y + AbstractDungeon.player.hb_h / 2.0f
) : EmptyOrbSlot(x, y) {
    protected val orbStringsSet: OrbStringsSet = AbstractLupaOrb.getPowerStringsWithSFW(ORB_ID)

    init {
        this.ID = ORB_ID
        this.name = orbStringsSet!!.getNAME()
        this.updateDescription()
    }

    override fun render(sb: SpriteBatch) {
    }

    override fun update() {
    }

    override fun updateDescription() {
        this.description = ""
    }

    override fun makeCopy(): AbstractOrb {
        return SexMarkEmptySlot()
    }

    companion object {
        val ORB_ID: String = DataManager.MakeTextID(SexMarkEmptySlot::class.java)
    }
}
