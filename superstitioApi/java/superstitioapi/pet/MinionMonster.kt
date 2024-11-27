package superstitioapi.pet

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction
import com.megacrit.cardcrawl.actions.common.DamageAction
import com.megacrit.cardcrawl.actions.common.RollMoveAction
import com.megacrit.cardcrawl.actions.utility.WaitAction
import com.megacrit.cardcrawl.cards.DamageInfo
import com.megacrit.cardcrawl.dungeons.AbstractDungeon
import com.megacrit.cardcrawl.monsters.AbstractMonster
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime
import superstitioapi.Logger
import superstitioapi.utils.CreatureUtility
import superstitioapi.utils.ListUtility
import java.util.stream.Collectors
import kotlin.math.max

open class MinionMonster protected constructor(petCore: AbstractMonster, drawScale: Float) :
    Minion(petCore, drawScale)
{
    init
    {
        for (d in petCoreMonster.damage)
        {
            d.base /= DAMAGE_SCALE.toInt()
            d.output = d.base
        }
        petCoreMonster.refreshIntentHbLocation()
    }

    constructor(petCore: AbstractMonster) : this(petCore, DEFAULT_DRAW_SCALE)

    val petCoreMonster: AbstractMonster
        get() = if (petCore is AbstractMonster) petCore
        else DefaultMonster

    override fun createIntent()
    {
        petCoreMonster.createIntent()
    }

    override fun init()
    {
        super.init()
        petCoreMonster.refreshIntentHbLocation()
        petCoreMonster.init()
    }

    override fun applyPowers()
    {
        for (dmg in petCoreMonster.damage)
        {
            dmg.applyPowers(this, CreatureUtility.getRandomMonsterWithoutRngSafe())
        }

        val monsterMove = ReflectionHacks.getPrivate<EnemyMoveInfo>(petCoreMonster, AbstractMonster::class.java, "move")

        if (monsterMove.baseDamage > -1)
        {
            ReflectionHacks.privateMethod(AbstractMonster::class.java, "calculateDamage", Int::class.javaPrimitiveType)
                .invoke<Any>(petCoreMonster, monsterMove.baseDamage)
        }
        val intentImg = ReflectionHacks.privateMethod(AbstractMonster::class.java, "getIntentImg").invoke<Texture>(
            petCoreMonster
        )
        ReflectionHacks.setPrivate(petCoreMonster, AbstractMonster::class.java, "intentImg", intentImg)
        ReflectionHacks.privateMethod(AbstractMonster::class.java, "updateIntentTip").invoke<Any>(petCoreMonster)
    }

    override val isHovered: Boolean
        get() = isMonsterHovered(this) || isMonsterHovered(
            petCoreMonster
        )

    override fun renderTip(sb: SpriteBatch)
    {
        petCoreMonster.renderTip(sb)
    }

    override fun updateHitBox()
    {
        super.updateHitBox()
        petCoreMonster.intentHb.update()
    }

    override fun takeTurn()
    {
        if (petCoreMonster == null)
        {
            Logger.warning("no symbol monster for minion " + this.name)
            return
        }
        var intentMultiAmt =
            ReflectionHacks.getPrivate<Int>(petCoreMonster, AbstractMonster::class.java, "intentMultiAmt")
        if (intentMultiAmt == null) intentMultiAmt = 1
        when (petCoreMonster.intent)
        {
            Intent.ATTACK, Intent.ATTACK_BUFF, Intent.ATTACK_DEBUFF, Intent.ATTACK_DEFEND                                                                     ->
            {
                AbstractDungeon.actionManager.addToBottom(AnimateSlowAttackAction(this))
                val monsters =
                    AbstractDungeon.getMonsters().monsters.stream()
                        .filter { monster1: AbstractMonster -> monster1 !== this }
                        .filter { monster1: AbstractMonster -> !monster1.isDeadOrEscaped }
                        .collect(Collectors.toList())
                var i = 0
                while (i < max(1.0, intentMultiAmt.toDouble()))
                {
                    AbstractDungeon.actionManager.addToBottom(
                        DamageAction(
                            ListUtility.getRandomFromList(monsters, AbstractDungeon.cardRandomRng),
                            DamageInfo(this, petCoreMonster.intentDmg),
                            AttackEffect.BLUNT_HEAVY
                        )
                    )
                    i++
                }
                AbstractDungeon.actionManager.addToBottom(WaitAction(0.8f))
                AbstractDungeon.actionManager.addToBottom(RollMoveAction(this))
            }

            Intent.DEBUFF, Intent.DEFEND_DEBUFF, Intent.STRONG_DEBUFF                                                                                         -> AbstractDungeon.actionManager.addToBottom(
                RollMoveAction(
                    this
                )
            )

            Intent.DEBUG, Intent.DEFEND_BUFF, Intent.DEFEND, Intent.BUFF, Intent.ESCAPE, Intent.MAGIC, Intent.NONE, Intent.SLEEP, Intent.STUN, Intent.UNKNOWN -> petCoreMonster.takeTurn()
            else                                                                                                                                              -> petCoreMonster.takeTurn()
        }
    }

    override fun updatePowers()
    {
        petCoreMonster.updatePowers()
    }

    override fun usePreBattleAction()
    {
        petCoreMonster.usePreBattleAction()
    }

    override fun useUniversalPreBattleAction()
    {
        petCoreMonster.useUniversalPreBattleAction()
    }

    override fun setupImg(): Texture
    {
//        Texture img = ;
//        setPrivate(petCore, AbstractMonster.class, "img", null);
        return ReflectionHacks.getPrivate(petCoreMonster, AbstractMonster::class.java, "img")
    }

    override fun updatePetCore()
    {
        petCoreMonster.update()
    }

    override fun getMove(i: Int)
    {
        ReflectionHacks.privateMethod(AbstractMonster::class.java, "getMove", Int::class.javaPrimitiveType).invoke<Any>(
            petCoreMonster, i
        )
        val moveInfo = ReflectionHacks.getPrivate<EnemyMoveInfo>(petCoreMonster, AbstractMonster::class.java, "move")
        this.setMove(
            petCoreMonster.moveName, moveInfo.nextMove, moveInfo.intent, moveInfo.baseDamage, moveInfo.multiplier,
            moveInfo.isMultiDamage
        )
    }

    companion object
    {
        const val DAMAGE_SCALE: Float = 3f
        private val DefaultMonster: AbstractMonster = ApologySlime()

        fun isMonsterHovered(monster: AbstractMonster): Boolean
        {
            return monster.hb.hovered || monster.intentHb.hovered || monster.healthHb.hovered
        }
    }
}
