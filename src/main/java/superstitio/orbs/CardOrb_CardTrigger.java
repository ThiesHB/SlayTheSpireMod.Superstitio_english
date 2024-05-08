package superstitio.orbs;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import org.apache.logging.log4j.util.BiConsumer;
import superstitio.cards.DamageActionMaker;
import superstitio.utils.ActionUtility;


public abstract class CardOrb_CardTrigger extends CardOrb {
    private static final float DURATION = 0.5f;
    protected final BiConsumer<CardOrb_CardTrigger, AbstractCard> action;
    private float checkDur = DURATION;

    public CardOrb_CardTrigger(AbstractCard card, BiConsumer<CardOrb_CardTrigger, AbstractCard> action_thisCard_targetCard) {
        super(card);
        this.action = action_thisCard_targetCard;
    }

    public static AbstractMonster getHoveredMonsterSafe() {
        return DamageActionMaker.getMonsterOrFirstMonster(ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster"));
    }

    @Override
    protected void onRemoveCard() {
        AbstractDungeon.effectList.add(new ExhaustCardEffect(card));
    }

    @Override
    public abstract boolean shouldRemove();

    @Override
    public void update() {
        super.update();

        switch (this.movingType) {
            case focusOnMonster:
                showEvokeNum();
                this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
                updateIfFocusOnMonster();
                this.drawOrder = DrawOrder.middle;
                if (!ifHoveredRightCard()) {
                    this.movingType = CardOrbMovingType.Idle;
                    break;
                }
                this.movingType = checkAndSetTheType();
                break;
            case focusOnSelf:
                showEvokeNum();
                this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
                updateIfFocusOnSelf();
                this.drawOrder = DrawOrder.middle;
                if (!ifHoveredRightCard()) {
                    this.movingType = CardOrbMovingType.Idle;
                    break;
                }
                this.movingType = checkAndSetTheType();
                break;
            case focusOnNothing:
                showEvokeNum();
                this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
                updateIfFocusOnNothing();
                this.drawOrder = DrawOrder.middle;
                if (!ifHoveredRightCard()) {
                    this.movingType = CardOrbMovingType.Idle;
                    break;
                }
                this.movingType = checkAndSetTheType();
                break;
            case Moving:
                showEvokeNum();
                this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
                if (checkShouldStopMoving()) {
                    this.movingType = CardOrbMovingType.Idle;
                }
                this.drawOrder = DrawOrder.top;
                break;
            case Idle:
            default:
                showPassiveNum();
                updateAnimationIdle();
                if (ifHoveredRightCard()) {
                    this.movingType = checkAndSetTheType();
                }
                break;
        }

//        if (this.movingType == CardOrbMovingType.Idle)
//            updateAnimationIdle();
//        this.movingIsStop = checkShouldStopMoving();
//        if (movingType != CardOrbMovingType.Idle)
//            this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
//        if (ifHoveredRightCard()) {
//            checkAndSetTheType().get();
//            return;
//        }
//        if (movingIsStop) {
//            this.movingType = CardOrbMovingType.Idle;
//            this.canInterrupt = true;
//        }
//
//        return;

    }

    protected void showEvokeNum() {
        card.costForTurn = evokeAmount;
        card.isCostModified = true;
        card.beginGlowing();
    }

    protected void showPassiveNum() {
        card.stopGlowing();
        card.costForTurn = passiveAmount;
        card.isCostModified = false;
    }


    protected void updateIfFocusOnMonster() {
        AbstractCreature target = getHoveredMonsterSafe();
        if (!ActionUtility.isAlive(target)) return;
        this.tryMoveTo(new Vector2(this.cX - (this.cX - target.hb.cX) / 3, this.cY - (this.cY - target.hb.cY) / 3 + YOffsetWhenHovered()));
    }

    protected CardOrbMovingType checkAndSetTheType() {
        switch (targetType) {
            case ENEMY:
            case ALL_ENEMY:
                this.movingType = CardOrbMovingType.focusOnMonster;
                break;
            case SELF:
                this.movingType = CardOrbMovingType.focusOnSelf;
                break;
            case NONE:
            case ALL:
            case SELF_AND_ENEMY:
            default:
                this.movingType = CardOrbMovingType.focusOnNothing;
                break;
        }
        return this.movingType;
    }

    protected boolean ifHoveredRightCard() {
        return (boolean) ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "isHoveringCard")
                && (AbstractDungeon.player.hoveredCard != null) && this.cardMatcher.test(AbstractDungeon.player.hoveredCard);
    }

    public CardOrb setTargetType(AbstractCard.CardTarget cardTarget) {
        this.targetType = cardTarget;
        return this;
    }

    protected void updateIfFocusOnSelf() {
        if (!ActionUtility.isAlive(AbstractDungeon.player)) return;
        this.tryMoveTo(new Vector2(this.cX - (this.cX - AbstractDungeon.player.hb.cX) / 2.5f,
                this.cY - (this.cY - AbstractDungeon.player.hb.cY) / 2.5f + YOffsetWhenHovered()));
    }

    protected void updateIfFocusOnNothing() {
        this.tryMoveTo(new Vector2(this.cX, this.cY + YOffsetWhenHovered() + YOffsetWhenHovered()));
    }

    public void StartHitCreature(AbstractCreature target) {
        this.movingType = CardOrbMovingType.Moving;
        AbstractCreature creature = DamageActionMaker.getTargetOrFirstMonster(target);
        this.tryMoveTo(new Vector2(this.cX - (this.cX - creature.hb.cX) / 1.2f, this.cY - (this.cY - creature.hb.cY) / 1.2f));
        setCanNotInterrupt();
    }

}
