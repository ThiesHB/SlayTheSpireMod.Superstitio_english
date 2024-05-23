package superstitio.hangUpCard;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.cards.DamageActionMaker;

import java.util.Optional;
import java.util.function.Consumer;


public abstract class CardOrb_AtStartOfTurn extends CardOrb {

    protected final Consumer<CardOrb_AtStartOfTurn> action;
    public int OrbCounter = 0;

    public CardOrb_AtStartOfTurn(AbstractCard card, CardGroup cardGroupReturnAfterEvoke, Consumer<CardOrb_AtStartOfTurn> action_thisCard_targetCard) {
        super(card, cardGroupReturnAfterEvoke);
        this.action = action_thisCard_targetCard;
    }

    public static AbstractMonster getHoveredMonsterSafe() {
        return DamageActionMaker.getMonsterOrFirstMonster(ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster"));
    }

    public static Optional<AbstractMonster> getHoveredMonster() {
        AbstractMonster hoveredMonster = ReflectionHacks.getPrivate(AbstractDungeon.player, AbstractPlayer.class, "hoveredMonster");
        if (hoveredMonster == null)
            return Optional.empty();
        return Optional.of(hoveredMonster);
    }

    @Override
    public void onEndOfTurn() {
        super.onEndOfTurn();
    }

    @Override
    protected void onRemoveCard() {
    }

    @Override
    public void checkShouldRemove() {
        if (shouldRemove) return;
        this.shouldRemove = this.OrbCounter <= 0 && checkShouldStopMoving();
    }

    @Override
    public void update() {
        super.update();
        this.movingType = this.movingType.get();
//        setEvokeAmount(OrbCounter - 1);
//        setPassiveAmount(OrbCounter);
    }

    @Override
    public void render(SpriteBatch spriteBatch) {
        super.render(spriteBatch);
    }

    public void forceAcceptAction() {
        actionAccept();
    }

    protected void actionAccept() {
        action.accept(this);
    }

//    protected FunctionReturnSelfType State_Moving() {
//        showPassiveNum();
//        this.card.targetDrawScale = DRAW_SCALE_MIDDLE;
//        this.drawOrder = DrawOrder.top;
//        if (checkShouldStopMoving()) {
//            return this::State_Idle;
//        }
//        return this::State_Moving;
//    }

//    public void StartHitCreature(AbstractCreature target) {
//        this.movingType = this::State_Moving;
//        AbstractCreature creature = DamageActionMaker.getTargetOrFirstMonster(target);
//        this.tryMoveTo(new Vector2(this.cX - (this.cX - creature.hb.cX) / 1.2f, this.cY - (this.cY - creature.hb.cY) / 1.2f));
//        this.card.superFlash(CardUtility.getColorFormCard(card));
//    }
}
