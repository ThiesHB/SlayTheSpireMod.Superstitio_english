package superstitio.cards.lupa.AttackCard

import com.badlogic.gdx.graphics.Color
import com.megacrit.cardcrawl.characters.AbstractPlayer
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import superstitio.DataManager
import superstitio.cards.general.FuckJob_Card
import superstitio.cards.lupa.LupaCard
import superstitio.powers.SexualHeat
import superstitio.powers.SexualHeat.Orgasm
import superstitioapi.SuperstitioApiSetup
import superstitioapi.actions.AutoDoneInstantAction

class Job_LegPit : LupaCard(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET), FuckJob_Card
{
    init
    {
        FuckJob_Card.initFuckJobCard(this)
        this.setupDamage(DAMAGE, UPGRADE_DAMAGE)
    }

    override fun use(player: AbstractPlayer?, monster: AbstractMonster?)
    {
        for (i in 0 until Attack_Num)
        {
            addToBot_dealDamage(monster!!, SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
            AutoDoneInstantAction.addToBotAbstract {
                if (monster.lastDamageTaken > 0 && SexualHeat.isInOrgasm(AbstractDungeon.player))
                    addToBot_drawCards()
            }
        }
    }

    override fun triggerOnGlowCheck()
    {
        this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy()
        if (Orgasm.isPlayerInOrgasm())
        {
            this.glowColor = Color.PINK.cpy()
        }
    }


    override fun upgradeAuto()
    {
    }

    companion object
    {
        val ID: String = DataManager.MakeTextID(Job_LegPit::class.java)

        val CARD_TYPE: CardType = CardType.ATTACK

        val CARD_RARITY: CardRarity = CardRarity.COMMON

        val CARD_TARGET: CardTarget = CardTarget.ENEMY

        private const val COST = 1
        private const val DAMAGE = 4
        private const val UPGRADE_DAMAGE = 2

        private const val Attack_Num = 2
    }
}
