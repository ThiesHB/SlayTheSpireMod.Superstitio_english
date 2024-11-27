package superstitio.cards.general.PowerCard.monsterGirl

import com.badlogic.gdx.math.MathUtils
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.exordium.Cultist
import com.megacrit.cardcrawl.powers.RitualPower
import com.megacrit.cardcrawl.vfx.SpeechBubble
import superstitio.DataManager
import superstitio.cards.general.AbstractTempCard
import superstitioapi.actions.AutoDoneInstantAction

class KakaaGirlMode : AbstractTempCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET)
{
    init
    {
        this.setupMagicNumber(MAGIC)
    }

    private fun KakaaSound()
    {
        val roll = MathUtils.random(2)
        if (roll == 0)
        {
            CardCrawlGame.sound.play("VO_CULTIST_1A")
        }
        else if (roll == 1)
        {
            CardCrawlGame.sound.play("VO_CULTIST_1B")
        }
        else
        {
            CardCrawlGame.sound.play("VO_CULTIST_1C")
        }
        AbstractDungeon.effectList.add(
            SpeechBubble(
                AbstractDungeon.player.hb.cX + AbstractDungeon.player.dialogX,
                AbstractDungeon.player.hb.cY + AbstractDungeon.player.dialogY,
                2.5f,
                Cultist.DIALOG[2],
                false
            )
        )
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        this.onChoseThisOption()
    }

    override fun onChoseThisOption()
    {
        addToBot_applyPower(RitualPower(AbstractDungeon.player, this.magicNumber, true))
        AutoDoneInstantAction.addToBotAbstract(this::KakaaSound)
    }

    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(KakaaGirlMode::class.java)

        val CARD_TYPE: CardType = CardType.POWER

        val CARD_RARITY: CardRarity = CardRarity.UNCOMMON

        val CARD_TARGET: CardTarget = CardTarget.SELF

        private const val COST = -2
        private const val MAGIC = 3
    }
}
