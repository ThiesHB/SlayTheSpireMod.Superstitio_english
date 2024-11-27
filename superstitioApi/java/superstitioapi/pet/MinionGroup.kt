package superstitioapi.pet

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.core.AbstractCreature
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.MonsterGroup
import com.megacrit.cardcrawl.ui.buttons.PeekButton
import java.util.stream.Collectors

class MinionGroup(input: Array<AbstractMonster?>?) : MonsterGroup(input)
{
    var hoveredCreature: Minion? = null

    val petCores: List<AbstractCreature>
        get() = monsters
            .filterIsInstance<Minion>()
            .map { monster: AbstractMonster -> (monster as Minion).petCore }
            .toList()

    val allMinion: List<Minion>
        get() = monsters.stream()
            .filter { monster: AbstractMonster? -> monster is Minion }
            .map { monster: AbstractMonster -> monster as Minion }
            .collect(Collectors.toList())

    override fun render(sb: SpriteBatch)
    {
        if (this.hoveredCreature != null && !hoveredCreature!!.isDead && !hoveredCreature!!.escaped && (AbstractDungeon.player.hoverEnemyWaitTimer < 0.0f) && (!AbstractDungeon.isScreenUp || PeekButton.isPeeking))
        {
            hoveredCreature!!.renderTip(sb)
        }

        for (m in this.monsters)
        {
            m.render(sb)
        }
    }

    override fun update()
    {
        for (monster in this.monsters)
        {
            monster.update()
        }
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH)
        {
            this.hoveredCreature = null
            return
        }
        this.hoveredCreature = null
        for (monster in this.allMinion)
        {
            if (monster.isDying || monster.isEscaping) continue
            monster.updateHitBox()
            if (!monster.isHovered) continue
            if (!AbstractDungeon.player.isDraggingCard)
            {
                this.hoveredCreature = monster
                break
            }
        }
        if (this.hoveredCreature == null)
        {
            AbstractDungeon.player.hoverEnemyWaitTimer = -1.0f
        }
    }

    override fun applyEndOfTurnPowers()
    {
        for (m in this.petCores)
        {
            if (!m.isDying && !m.isEscaping)
            {
                m.applyEndOfTurnTriggers()
            }
        }
        for (m in this.petCores)
        {
            if (!m.isDying && !m.isEscaping)
            {
                for (p2 in m.powers)
                {
                    p2.atEndOfRound()
                }
            }
        }
    }

    override fun applyPreTurnLogic()
    {
        for (m in this.petCores)
        {
            if (!m.isDying && !m.isEscaping)
            {
                if (!m.hasPower("Barricade"))
                {
                    m.loseBlock()
                }
                m.applyStartOfTurnPowers()
            }
        }
    }
}
