package superstitioapi.effect

import basemod.ReflectionHacks
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect
import com.megacrit.cardcrawl.core.CardCrawlGame
import com.megacrit.cardcrawl.vfx.AbstractGameEffect
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect

class PlayAttackMusicEffect(private val attackEffect: AttackEffect) : AbstractGameEffect()
{
    override fun update()
    {
        playSound(attackEffect)
        this.isDone = true
    }

    override fun render(spriteBatch: SpriteBatch)
    {
    }

    override fun dispose()
    {
    }

    companion object
    {
        private fun playSound(effect: AttackEffect)
        {
            when (effect)
            {
                AttackEffect.SHIELD                                                                     -> playBlockSound()
                AttackEffect.SLASH_DIAGONAL, AttackEffect.SLASH_HORIZONTAL, AttackEffect.SLASH_VERTICAL -> CardCrawlGame.sound.play(
                    "ATTACK_FAST"
                )

                AttackEffect.SLASH_HEAVY                                                                -> CardCrawlGame.sound.play(
                    "ATTACK_HEAVY"
                )

                AttackEffect.BLUNT_LIGHT                                                                -> CardCrawlGame.sound.play(
                    "BLUNT_FAST"
                )

                AttackEffect.BLUNT_HEAVY                                                                -> CardCrawlGame.sound.play(
                    "BLUNT_HEAVY"
                )

                AttackEffect.FIRE                                                                       -> CardCrawlGame.sound.play(
                    "ATTACK_FIRE"
                )

                AttackEffect.POISON                                                                     -> CardCrawlGame.sound.play(
                    "ATTACK_POISON"
                )

                AttackEffect.NONE                                                                       ->
                {
                }

                else                                                                                    -> CardCrawlGame.sound.play(
                    "ATTACK_FAST"
                )
            }
        }

        private fun playBlockSound()
        {
            val blockSound = ReflectionHacks.getPrivateStatic<Int>(FlashAtkImgEffect::class.java, "blockSound")
            if (blockSound == 0)
            {
                CardCrawlGame.sound.play("BLOCK_GAIN_1")
            }
            else if (blockSound == 1)
            {
                CardCrawlGame.sound.play("BLOCK_GAIN_2")
            }
            else
            {
                CardCrawlGame.sound.play("BLOCK_GAIN_3")
            }
            ReflectionHacks.setPrivateStatic(FlashAtkImgEffect::class.java, "blockSound", blockSound + 1)
            if (blockSound + 1 > 2)
            {
                ReflectionHacks.setPrivateStatic(FlashAtkImgEffect::class.java, "blockSound", 0)
            }
        }
    }
}
