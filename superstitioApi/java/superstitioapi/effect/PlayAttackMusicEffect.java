package superstitioapi.effect;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class PlayAttackMusicEffect extends AbstractGameEffect {
    private final AbstractGameAction.AttackEffect attackEffect;

    public PlayAttackMusicEffect(AbstractGameAction.AttackEffect attackEffect) {
        this.attackEffect = attackEffect;
    }

    private static void playSound(AbstractGameAction.AttackEffect effect) {
        switch (effect) {
            case SHIELD:
                playBlockSound();
                break;
            case SLASH_DIAGONAL:
            case SLASH_HORIZONTAL:
            case SLASH_VERTICAL:
            default:
                CardCrawlGame.sound.play("ATTACK_FAST");
                break;
            case SLASH_HEAVY:
                CardCrawlGame.sound.play("ATTACK_HEAVY");
                break;
            case BLUNT_LIGHT:
                CardCrawlGame.sound.play("BLUNT_FAST");
                break;
            case BLUNT_HEAVY:
                CardCrawlGame.sound.play("BLUNT_HEAVY");
                break;
            case FIRE:
                CardCrawlGame.sound.play("ATTACK_FIRE");
                break;
            case POISON:
                CardCrawlGame.sound.play("ATTACK_POISON");
            case NONE:
        }

    }

    private static void playBlockSound() {
        int blockSound = ReflectionHacks.getPrivateStatic(FlashAtkImgEffect.class, "blockSound");
        if (blockSound == 0) {
            CardCrawlGame.sound.play("BLOCK_GAIN_1");
        } else if (blockSound == 1) {
            CardCrawlGame.sound.play("BLOCK_GAIN_2");
        } else {
            CardCrawlGame.sound.play("BLOCK_GAIN_3");
        }
        ReflectionHacks.setPrivateStatic(FlashAtkImgEffect.class, "blockSound", blockSound + 1);
        if (blockSound + 1 > 2) {
            ReflectionHacks.setPrivateStatic(FlashAtkImgEffect.class, "blockSound", 0);
        }
    }

    @Override
    public void update() {
        playSound(attackEffect);
        this.isDone = true;
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
    }

    @Override
    public void dispose() {
    }

}
