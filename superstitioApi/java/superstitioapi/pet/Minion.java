package superstitioapi.pet;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomMonster;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import superstitioapi.Logger;
import superstitioapi.pet.animationSize.AnimationSize;
import superstitioapi.utils.ActionUtility;
import superstitioapi.utils.CardUtility;

import java.util.List;
import java.util.stream.Collectors;

import static basemod.ReflectionHacks.*;

public class Minion extends CustomMonster {
    public static final int HEALTH_DIV = 10;
    public static final float SCALE = 3f;
    public AbstractMonster monster;
    protected Hitbox monsterHitbox;
    private float oldMX;
    private float oldMY;

    public Minion(AbstractMonster monster) {
        super(monster.name, monster.id, monster.maxHealth,
                monster.hb_x, monster.hb_y, monster.hb_w / SCALE, monster.hb_h / SCALE, null, 0, 0, true);
        this.monster = monster;
        AnimationSize.reloadAnimation(monster, SCALE);
        for (DamageInfo d : this.monster.damage) {
            d.base /= (int) SCALE;
            d.output = d.base;
        }
        this.dialogX = this.monster.dialogX;
        this.dialogY = this.monster.dialogY;

        privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(this.monster);

        this.monster.refreshIntentHbLocation();

        this.tips = getPrivate(monster, AbstractCreature.class, "tips");
        this.img = getPrivate(monster, AbstractMonster.class, "img");
        setPrivate(monster, AbstractMonster.class, "img", null);
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

    public static boolean isMonsterHovered(AbstractMonster monster) {
        return monster.hb.hovered || monster.intentHb.hovered || monster.healthHb.hovered;
    }

    @Override
    public void createIntent() {
        this.monster.createIntent();
    }

    @Override
    public void init() {
        monster.drawX = drawX;
        monster.drawY = drawY;
        refreshHitBox();
        this.monster.healthHb = new Hitbox(this.hb_w, 72.0F * Settings.scale);

        privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(this.monster);
        this.monster.refreshIntentHbLocation();

        this.monster.init();
        this.createIntent();
    }

    private void refreshHitBox() {
        this.monster.hb_x = this.hb_x;
        this.monster.hb_y = this.hb_y;
        this.monster.hb = new Hitbox(this.hb_w, this.hb_h);
        this.monster.hb_w = this.monster.hb.width;
        this.monster.hb_h = this.monster.hb.height;
        this.monster.hb.move(monster.drawX, monster.drawY);
        this.monsterHitbox = this.monster.hb;
    }

    @Override
    public void showHealthBar() {
        this.monster.showHealthBar();
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        this.monster.damage(info);
    }

    @Override
    public void render(SpriteBatch sb) {
        this.monster.render(sb);
        if (!this.isDead && !this.escaped) {
            if (this.atlas == null) {
                sb.setColor(this.tint.color);
                if (this.img != null) {
                    drawImg(sb);
                }
                if (this == AbstractDungeon.getCurrRoom().monsters.hoveredMonster) {
                    sb.setBlendFunction(770, 1);
                    sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.1F));
                    if (this.img != null) {
                        drawImg(sb);
                        sb.setBlendFunction(770, 771);
                    }
                }
            }
        }
    }

    private void drawImg(SpriteBatch sb) {
        sb.draw(this.img, this.drawX - (float) this.img.getWidth() * Settings.scale / 2.0F + this.animX, this.drawY + this.animY, (float) this.img.getWidth() * Settings.scale, (float) this.img.getHeight() * Settings.scale, 0, 0, this.img.getWidth(), this.img.getHeight(), this.flipHorizontal, this.flipVertical);
    }

    @Override
    public void applyPowers() {
        for (DamageInfo dmg : this.monster.damage) {
            dmg.applyPowers(this, ActionUtility.getRandomMonsterWithoutRngSafe());
        }

        EnemyMoveInfo monsterMove = ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");

        if (monsterMove.baseDamage > -1) {
            ReflectionHacks.privateMethod(AbstractMonster.class, "calculateDamage", int.class)
                    .invoke(monster, monsterMove.baseDamage);
        }
        Texture intentImg = ReflectionHacks.privateMethod(AbstractMonster.class, "getIntentImg").invoke(monster);
        ReflectionHacks.setPrivate(monster, AbstractMonster.class, "intentImg", intentImg);
        ReflectionHacks.privateMethod(AbstractMonster.class, "updateIntentTip").invoke(monster);
    }

    @Override
    public void update() {
        if (monster.hb != monsterHitbox)
            refreshHitBox();
        super.update();
        this.monster.update();

        if (isMonsterHovered(this) || isMonsterHovered(this.monster)) {
            if (InputHelper.justClickedLeft) {
                this.Drag_Press();
            }
            else if (InputHelper.isMouseDown) {
                this.Drag_Hold();
            }
            else if (InputHelper.justReleasedClickLeft) {
                this.Drag_Release();
            }
        }
    }

    protected void Drag_Press() {
        this.oldMX = InputHelper.mX;
        this.oldMY = InputHelper.mY;
    }

    protected void Drag_Release() {
        this.refreshHitboxLocation();
        ReflectionHacks.privateMethod(AbstractCreature.class, "refreshHitboxLocation").invoke(monster);
        this.oldMX = 0.0f;
        this.oldMY = 0.0f;
    }

    protected void Drag_Hold() {
        if (this.oldMX != 0.0f && this.oldMY != 0.0f) {
            final float xDiff = InputHelper.mX - this.oldMX;
            final float yDiff = InputHelper.mY - this.oldMY;
            this.drawX += xDiff;
            this.monster.drawX += xDiff;
            this.drawY += yDiff;
            this.monster.drawY += yDiff;
        }
        this.oldMX = InputHelper.mX;
        this.oldMY = InputHelper.mY;
    }

    @Override
    public void renderTip(SpriteBatch sb) {
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
                                    new DamageInfo(this, this.monster.getIntentDmg()),
                                    AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                }
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.8F));
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
                break;
            case DEBUFF:
            case DEFEND_DEBUFF:
            case STRONG_DEBUFF:
                AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
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
                break;
        }
    }

    @Override
    public void rollMove() {
        this.getMove(AbstractDungeon.aiRng.random(99));
    }

    @Override
    public void updatePowers() {
        super.updatePowers();
        this.monster.updatePowers();
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
        //        this.monster.getMove(i);
    }
}
