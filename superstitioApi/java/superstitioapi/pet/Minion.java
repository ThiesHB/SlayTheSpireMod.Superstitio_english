package superstitioapi.pet;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import superstitioapi.Logger;
import superstitioapi.pet.animationSize.AnimationSize;
import superstitioapi.utils.CardUtility;

import java.util.List;
import java.util.stream.Collectors;

import static basemod.ReflectionHacks.getPrivate;
import static basemod.ReflectionHacks.privateMethod;

public class Minion extends CustomMonster {
    public static final int HEALTH_DIV = 10;
    public static final float SCALE = 3f;
    public AbstractMonster monster;
    private EnemyMoveInfo moveInfo;

    public Minion(AbstractMonster monster) {
        super(monster.name, monster.id, monster.maxHealth,
                monster.hb_x, monster.hb_y, monster.hb_w, monster.hb_h, null, 0, 0, true);
        this.monster = monster;
        AnimationSize.reloadAnimation(monster, SCALE);
        for (DamageInfo d : this.damage) {
            d.base /= (int) SCALE;
        }
        this.dialogX = this.monster.dialogX;
        this.dialogY = this.monster.dialogY;

        privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(this.monster);
//        this.monster.refreshHitboxLocation();
        this.monster.refreshIntentHbLocation();

        this.tips = getPrivate(monster, AbstractCreature.class, "tips");
        this.img = getPrivate(monster, AbstractMonster.class, "img");
        this.state = getPrivate(monster, AbstractCreature.class, "state");
        this.skeleton = getPrivate(monster, AbstractCreature.class, "skeleton");
        this.tint = getPrivate(monster, AbstractCreature.class, "tint");
        this.atlas = getPrivate(monster, AbstractCreature.class, "atlas");
        this.maxHealth = monster.maxHealth / HEALTH_DIV;
        this.currentHealth = maxHealth;
        this.flipHorizontal = true;
        this.monster.maxHealth = monster.maxHealth / HEALTH_DIV;
        this.monster.currentHealth = maxHealth;
        this.monster.flipHorizontal = true;

    }

    @Override
    public void createIntent() {
//        super.createIntent();
        this.monster.createIntent();
    }

    @Override
    public void init() {
        monster.drawX = drawX;
        monster.drawY = drawY;
        this.monster.hb_x = this.hb_x;
        this.monster.hb_y = this.hb_y;
        this.monster.hb = new Hitbox(this.hb_w / SCALE, this.hb_h / SCALE);
        this.monster.hb_w = this.monster.hb.width;
        this.monster.hb_h = this.monster.hb.height;
        monster.hb.move(monster.drawX, monster.drawY);
        this.monster.healthHb = new Hitbox(this.hb_w / SCALE, 72.0F * Settings.scale);
//        hb.move(monster.drawX, monster.drawY);


        privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(this.monster);
        this.monster.refreshIntentHbLocation();
//        this.refreshHitboxLocation();
//        this.refreshIntentHbLocation();


//        super.init();
        this.monster.init();
    }

    @Override
    public void showHealthBar() {
//        super.showHealthBar();
        this.monster.showHealthBar();
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        this.monster.damage(info);
//        this.die();
    }

    @Override
    public void render(SpriteBatch sb) {
//        stopAnim();
        this.monster.render(sb);

//        if (this.animation != null && this.animation.type() == AbstractAnimation.Type.SPRITE) {
//            this.animation.renderSprite(sb, this.drawX + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY);
//        }
//        else if (this.atlas == null) {
//            sb.setColor(this.tint.color);
//            sb.draw(this.img, this.drawX - (float) this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
//        }
//        else {
//            this.state.update(Gdx.graphics.getDeltaTime());
//            this.state.apply(this.skeleton);
//            this.skeleton.updateWorldTransform();
//            this.skeleton.setPosition(this.drawX + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY);
//            this.skeleton.setColor(this.tint.color);
//            this.skeleton.setFlip(this.flipHorizontal, this.flipVertical);
//            sb.end();
//            CardCrawlGame.psb.begin();
//            AbstractMonster.sr.draw(CardCrawlGame.psb, this.skeleton);
//            CardCrawlGame.psb.end();
//            sb.begin();
//            sb.setBlendFunction(770, 771);
//        }
//
//        if (this == AbstractDungeon.getCurrRoom().monsters.hoveredMonster && this.atlas == null && this.animation == null) {
//            sb.setBlendFunction(770, 1);
//            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
//            sb.draw(this.img, this.drawX - (float) this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY + AbstractDungeon.sceneOffsetY, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
//            sb.setBlendFunction(770, 771);
//        }
//
//        if (!this.isDying && !this.isEscaping && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && !AbstractDungeon.player.isDead && !AbstractDungeon.player.hasRelic("Runic Dome") && this.intent != Intent.NONE && !Settings.hideCombatElements) {
//            this.renderIntentVfxBehind(sb);
//            this.renderIntent(sb);
//            this.renderIntentVfxAfter(sb);
//            this.renderDamageRange(sb);
//        }
//
//        this.hb.render(sb);
//        this.intentHb.render(sb);
//        this.healthHb.render(sb);
//
//        if (!AbstractDungeon.player.isDead) {
//            this.renderHealth(sb);
//            this.renderName(sb);
//        }
    }

    private void stopAnim() {
//        this.monster.deathTimer = 0.0f;
//        this.monster.escapeTimer = 0.0f;
//        this.monster.isDead = false;
//        this.monster.isDying = false;
//        this.monster.isEscaping = false;
//        this.monster.escaped = false;
//        this.deathTimer = 0.0f;
//        this.escapeTimer = 0.0f;
//        this.isDead = false;
//        this.isDying = false;
//        this.isEscaping = false;
//        this.escaped = false;
    }

    @Override
    public void applyPowers() {
//        super.applyPowers();
        this.monster.applyPowers();
    }

    @Override
    public void update() {
//        stopAnim();
//        super.update();
        this.monster.update();
//        stopAnim();
    }

    @Override
    public void renderTip(SpriteBatch sb) {
//        super.renderTip(sb);
        this.monster.renderTip(sb);
    }

    @Override
    public void takeTurn() {
        if (monster == null) {
            Logger.warning("no symbol monster for minion " + this.name);
            return;
        }
        Integer intentMultiAmt = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "intentMultiAmt");
        if (intentMultiAmt == null)
            intentMultiAmt = 1;
        switch (this.monster.intent) {
            case ATTACK:
            case ATTACK_BUFF:
            case ATTACK_DEBUFF:
            case ATTACK_DEFEND:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                List<AbstractMonster> monsters =
                        AbstractDungeon.getMonsters().monsters.stream()
                                .filter(monster1 -> monster1 != this).filter(monster1 -> !monster1.isDeadOrEscaped())
                                .collect(Collectors.toList());
                for (int i = 0; i < Math.max(1, intentMultiAmt); i++) {
                    AbstractDungeon.actionManager.addToBottom(
                            new DamageAction(
                                    CardUtility.getRandomFromList(monsters, AbstractDungeon.cardRandomRng),
                                    new DamageInfo(this, this.getIntentDmg()),
                                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));
                this.rollMove();
                this.createIntent();
                break;
            case DEBUFF:
            case DEFEND_DEBUFF:
            case STRONG_DEBUFF:
                this.rollMove();
                this.createIntent();
                break;
            case DEBUG:
            case DEFEND_BUFF:
            case DEFEND:
            case BUFF:
            case ESCAPE:
            case MAGIC:
            case NONE:
            case SLEEP:
            case STUN:
            case UNKNOWN:
            default:
                this.monster.takeTurn();
                this.createIntent();
                break;
        }
    }

    @Override
    public void rollMove() {
        this.getMove(AbstractDungeon.aiRng.random(99));
    }

    @Override
    public void usePreBattleAction() {
        super.usePreBattleAction();
        this.monster.usePreBattleAction();
    }

    @Override
    public void useUniversalPreBattleAction() {
        super.useUniversalPreBattleAction();
        this.monster.useUniversalPreBattleAction();
    }

    @Override
    protected void getMove(int i) {
        privateMethod(AbstractMonster.class, "getMove", int.class).invoke(this.monster, i);
        EnemyMoveInfo moveInfo = getPrivate(monster, AbstractMonster.class, "move");
        this.setMove(monster.moveName, moveInfo.nextMove, moveInfo.intent, moveInfo.baseDamage, moveInfo.multiplier, moveInfo.isMultiDamage);
        this.moveInfo = moveInfo;
        //        this.monster.getMove(i);
    }
}
