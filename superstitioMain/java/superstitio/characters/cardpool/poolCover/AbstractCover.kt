package superstitio.characters.cardpool.poolCover

import basemod.AutoAdd
import basemod.abstracts.CustomCard
import com.megacrit.cardcrawl.cards.AbstractCard
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.GeneralCard
import superstitioapi.cardPool.BaseCardPool.IsCardPoolCover

@AutoAdd.Ignore
abstract class AbstractCover(id: String, symbol: CustomCard) :
    GeneralCard(id, symbol.type, -2, symbol.rarity, symbol.target), IsCardPoolCover
{
    init
    {
        this.textureImg = symbol.textureImg
        if (textureImg != null)
        {
            this.loadCardImage(textureImg)
        }
    }

    override val self: AbstractCard
        get()
        {
            return this
        }

    override fun upgradeAuto()
    {
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(AbstractCover::class.java)
    }
}
