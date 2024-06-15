//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package superstitioapi.actions;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.BindingHelper;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModContainer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import superstitioapi.effect.ChangeColorEffect;
import superstitioapi.effect.PlayAttackMusicEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.megacrit.cardcrawl.cards.DamageInfo.DamageType;

public class DamageAllEnemiesAction extends AbstractGameAction {
    private final Function<Integer, DamageInfo> newDamageInfoMaker;
    private final List<Supplier<AbstractGameEffect>> atStart_additionalEffects = new ArrayList<>();
    private final List<Function<AbstractCreature, AbstractGameEffect>> atEnd_additionalEffects = new ArrayList<>();
    public int[] damages;
    private Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker;
    private boolean firstFrame;

    public DamageAllEnemiesAction(AbstractCreature source, int[] damages, DamageType type, AttackEffect attackEffect,
                                  Function<Integer, DamageInfo> newDamageInfoMaker) {
        this.attackEffect = attackEffect;
        this.newAttackEffectMaker = creature -> new FlashAtkImgEffect(creature.hb.cX, creature.hb.cY, attackEffect);
        this.newDamageInfoMaker = newDamageInfoMaker;
        this.source = source;
        this.damages = damages;
        //createDamageMatrix(baseDamage);
        this.damageType = type;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = ActionType.DAMAGE;
        this.firstFrame = true;
    }

    public DamageAllEnemiesAction(AbstractCreature source, int[] damages, DamageType type, AttackEffect attackEffect) {
        this(source, damages, type, attackEffect,
                damage -> new DamageInfo(source, damage, type));
    }

    public DamageAllEnemiesAction(AbstractCreature source, int[] damages, DamageType type, AttackEffect attackEffect, Object instigator,
                                  AbstractDamageModifier... damageModifiers) {
        this(source, damages, type, attackEffect,
                damage -> makeNewInfoWithModifier(instigator, source, type, damageModifiers).apply(damage));
    }

    public static Function<Integer, DamageInfo> makeNewInfoWithModifier(
            Object instigator, AbstractCreature source, DamageType damageType, AbstractDamageModifier... damageModifiers) {
        return damageAmount -> BindingHelper.makeInfo(new DamageModContainer(instigator, damageModifiers), source, damageAmount, damageType);
    }

    public DamageAllEnemiesAction setNewAttackEffectMaker(Function<AbstractCreature, AbstractGameEffect> newAttackEffectMaker) {
        this.newAttackEffectMaker = newAttackEffectMaker;
        return this;
    }

    public final DamageAllEnemiesAction setFast() {
        this.duration = Settings.ACTION_DUR_XFAST;
        return this;
    }

    @SafeVarargs
    public final DamageAllEnemiesAction setAtStart_additionalEffects(Supplier<AbstractGameEffect>... atStart_additionalEffects) {
        this.atStart_additionalEffects.addAll(Arrays.asList(atStart_additionalEffects));
        return this;
    }

    public final DamageAllEnemiesAction setPlaySound() {
        this.atStart_additionalEffects.add(() -> new PlayAttackMusicEffect(attackEffect));
        return this;
    }

    public final DamageAllEnemiesAction setChangeColor() {
        if (attackEffect == AttackEffect.POISON) {
            this.atEnd_additionalEffects.add(creature -> new ChangeColorEffect(creature, Color.CHARTREUSE));
        } else if (attackEffect == AttackEffect.FIRE) {
            this.atEnd_additionalEffects.add(creature -> new ChangeColorEffect(creature, Color.RED));
        }
        return this;
    }

    public void update() {
        if (this.firstFrame) {
            for (Supplier<AbstractGameEffect> additionalEffect : atStart_additionalEffects) {
                AbstractDungeon.effectList.add(additionalEffect.get());
            }

            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            for (AbstractMonster monster : monsters) {
                if (monster.isDying || monster.currentHealth <= 0 || monster.isEscaping) continue;
                AbstractDungeon.effectList.add(newAttackEffectMaker.apply(monster));
            }
            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {

            for (AbstractPower p : AbstractDungeon.player.powers) {
                p.onDamageAllEnemies(this.damages);
            }

            ArrayList<AbstractMonster> monsters = AbstractDungeon.getCurrRoom().monsters.monsters;
            for (int i = 0; i < monsters.size(); i++) {
                if (monsters.get(i).isDeadOrEscaped()) continue;
                for (Function<AbstractCreature, AbstractGameEffect> action : atEnd_additionalEffects) {
                    AbstractDungeon.effectList.add(action.apply(monsters.get(i)));
                }
                monsters.get(i).damage(newDamageInfoMaker.apply(this.damages[i]));
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

            if (!Settings.FAST_MODE) {
                this.addToTop(new WaitAction(0.1F));
            }
        }
    }
}
