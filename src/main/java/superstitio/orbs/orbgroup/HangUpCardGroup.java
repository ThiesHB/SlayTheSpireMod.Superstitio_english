package superstitio.orbs.orbgroup;

import basemod.interfaces.OnCardUseSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.EmptyOrbSlot;
import superstitio.InBattleDataManager;
import superstitio.orbs.CardOrb;
import superstitio.orbs.actions.ChannelOnOrbGroupAction;

import java.util.ArrayList;
import java.util.stream.Stream;

public class HangUpCardGroup extends OrbGroup implements OnCardUseSubscriber {

    private static final int SetupOrbMax = 1;

    public HangUpCardGroup(Hitbox hitbox) {
        super(hitbox, SetupOrbMax, new EmptyOrbSlot());
        this.hitbox.moveX(this.hitbox.cX + this.hitbox.width * 2);
        this.hitbox.moveY(this.hitbox.cY + this.hitbox.height * 0.5f);
        this.letEachOrbToSlotPlaces();
    }

    public static void addToBot_AddCardOrbToOrbGroup(CardOrb orb) {
        AbstractDungeon.actionManager.addToBottom(
                new ChannelOnOrbGroupAction(InBattleDataManager.orbGroups.stream()
                        .filter(orbGroup -> orbGroup instanceof HangUpCardGroup).findAny().orElse(null), orb));
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        getCardOrbStream().filter(orb -> orb.card.hb.hovered).forEach(orb -> orb.render(sb));
    }

    private Stream<CardOrb> getCardOrbStream() {
        return this.orbs.stream().filter(orb -> orb instanceof CardOrb).map(orb -> ((CardOrb) orb));
    }

    @Override
    protected Vector2 makeSlotPlace(int slotIndex) {
        return super.makeSlotPlaceLine(this.hitbox.width, slotIndex);
    }

    @Override
    protected void onOrbChannel(AbstractOrb channeledOrb) {
    }

    @Override
    public void update() {
        super.update();
        removeUselessCard();
    }

    private void removeUselessCard() {
        ArrayList<AbstractOrb> abstractOrbs = this.orbs;
        for (int i = 0; i < abstractOrbs.size(); i++) {
            AbstractOrb orb = abstractOrbs.get(i);
            if (orb instanceof CardOrb && ((CardOrb) orb).shouldRemove()) {
                this.evokeOrb(i);
            }
        }
    }

    @Override
    public void evokeOrb(int slotIndex) {
        super.evokeOrb(slotIndex);
        this.decreaseMaxOrbs(1);
    }

    @Override
    public void channelOrb(AbstractOrb orb) {
        if (GetMaxOrbs() <= 0 || this.hasNoEmptySlot())
            this.increaseMaxOrbs(1);
        addNewOrb(orb);
    }

    @Override
    protected void onOrbEvoke(AbstractOrb evokedOrb) {
    }

    @Override
    public void receiveCardUsed(AbstractCard abstractCard) {
        getCardOrbStream()
                .forEach(orb -> orb.onCardUsed(abstractCard));
    }
}
