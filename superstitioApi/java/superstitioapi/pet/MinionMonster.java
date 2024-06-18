package superstitioapi.pet;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.monsters.exordium.ApologySlime;
import superstitioapi.Logger;
import superstitioapi.utils.CreatureUtility;
import superstitioapi.utils.ListUtility;

import java.util.List;
import java.util.stream.Collectors;

import static basemod.ReflectionHacks.getPrivate;
import static basemod.ReflectionHacks.privateMethod;

public class MinionMonster extends Minion {
    public static final float DAMAGE_SCALE = 3f;
    private static final AbstractMonster DefaultMonster = new ApologySlime();

    protected MinionMonster(AbstractMonster petCore, float drawScale) {
        super(petCore, drawScale);

        for (DamageInfo d : this.getPetCoreMonster().damage) {
            d.base /= (int) DAMAGE_SCALE;
            d.output = d.base;
        }
        this.getPetCoreMonster().refreshIntentHbLocation();

    }

    public MinionMonster(AbstractMonster petCore) {
        this(petCore, DEFAULT_DRAW_SCALE);
    }

    public static boolean isMonsterHovered(AbstractMonster monster) {
        return monster.hb.hovered || monster.intentHb.hovered || monster.healthHb.hovered;
    }

    @Override
    protected Texture setupImg() {
//        Texture img = ;
//        setPrivate(petCore, AbstractMonster.class, "img", null);
        return getPrivate(getPetCoreMonster(), AbstractMonster.class, "img");
    }

    @Override
    public void createIntent() {
        this.getPetCoreMonster().createIntent();
    }

    @Override
    public void init() {
        super.init();
        this.getPetCoreMonster().refreshIntentHbLocation();
        this.getPetCoreMonster().init();
    }

    @Override
    public void applyPowers() {
        for (DamageInfo dmg : this.getPetCoreMonster().damage) {
            dmg.applyPowers(this, CreatureUtility.getRandomMonsterWithoutRngSafe());
        }

        EnemyMoveInfo monsterMove = ReflectionHacks.getPrivate(getPetCoreMonster(), AbstractMonster.class, "move");

        if (monsterMove.baseDamage > -1) {
            ReflectionHacks.privateMethod(AbstractMonster.class, "calculateDamage", int.class)
                    .invoke(getPetCoreMonster(), monsterMove.baseDamage);
        }
        Texture intentImg = ReflectionHacks.privateMethod(AbstractMonster.class, "getIntentImg").invoke(getPetCoreMonster());
        ReflectionHacks.setPrivate(getPetCoreMonster(), AbstractMonster.class, "intentImg", intentImg);
        ReflectionHacks.privateMethod(AbstractMonster.class, "updateIntentTip").invoke(getPetCoreMonster());
    }

    @Override
    protected void updatePetCore() {
        this.getPetCoreMonster().update();
    }

    @Override
    public boolean isHovered() {
        return isMonsterHovered(this) || isMonsterHovered(this.getPetCoreMonster());
    }

    @Override
    public void renderTip(SpriteBatch sb) {
        this.getPetCoreMonster().renderTip(sb);
    }

    @Override
    public void updateHitBox() {
        super.updateHitBox();
        getPetCoreMonster().intentHb.update();
    }

    @Override
    public void takeTurn() {
        if (getPetCoreMonster() == null) {
            Logger.warning("no symbol monster for minion " + this.name);
            return;
        }
        Integer intentMultiAmt = ReflectionHacks.getPrivate(getPetCoreMonster(), AbstractMonster.class, "intentMultiAmt");
        if (intentMultiAmt == null)
            intentMultiAmt = 1;
        switch (this.getPetCoreMonster().intent) {
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
                                    ListUtility.getRandomFromList(monsters, AbstractDungeon.cardRandomRng),
                                    new DamageInfo(this, this.getPetCoreMonster().getIntentDmg()),
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
                this.getPetCoreMonster().takeTurn();
                break;
        }
    }

    @Override
    public void updatePowers() {
        this.getPetCoreMonster().updatePowers();
    }

    @Override
    public void usePreBattleAction() {
        this.getPetCoreMonster().usePreBattleAction();
    }

    @Override
    public void useUniversalPreBattleAction() {
        this.getPetCoreMonster().useUniversalPreBattleAction();
    }

    @Override
    protected void getMove(int i) {
        privateMethod(AbstractMonster.class, "getMove", int.class).invoke(this.getPetCoreMonster(), i);
        EnemyMoveInfo moveInfo = getPrivate(getPetCoreMonster(), AbstractMonster.class, "move");
        this.setMove(getPetCoreMonster().moveName, moveInfo.nextMove, moveInfo.intent, moveInfo.baseDamage, moveInfo.multiplier,
                moveInfo.isMultiDamage);
    }

    public AbstractMonster getPetCoreMonster() {
        if (petCore instanceof AbstractMonster)
            return (AbstractMonster) petCore;
        else
            return DefaultMonster;
    }

}
