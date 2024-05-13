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
import superstitio.Logger;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.orbs.CardOrb;
import superstitio.orbs.CardOrb_CardTrigger;
import superstitio.orbs.actions.ChannelOnOrbGroupAction;
import superstitio.utils.CardUtility;

import java.util.Objects;
import java.util.stream.Stream;

public class HangUpCardGroup extends OrbGroup implements OnCardUseSubscriber {

    private static final int SetupOrbMax = 0;
    private int remove_check_counter = 10;

    public HangUpCardGroup(Hitbox hitbox) {
        super(hitbox, SetupOrbMax, new EmptyOrbSlot());
        this.hitbox.moveX(this.hitbox.cX + this.hitbox.width * 2);
        this.hitbox.moveY(this.hitbox.cY + this.hitbox.height * 0.5f);
    }

    public static void addToBot_AddCardOrbToOrbGroup(CardOrb orb) {
        AbstractDungeon.actionManager.addToBottom(
                new ChannelOnOrbGroupAction(InBattleDataManager.getHangUpCardOrbGroup().orElse(null), orb));
    }

    @Override
    public void render(SpriteBatch sb) {
        getCardOrbStream().filter(orb -> orb.drawOrder == CardOrb.DrawOrder.bottom).forEach(orb -> orb.render(sb));
        getCardOrbStream().filter(orb -> orb.drawOrder == CardOrb.DrawOrder.middle).forEach(orb -> orb.render(sb));
        getCardOrbStream().filter(orb -> orb.drawOrder == CardOrb.DrawOrder.top).forEach(orb -> orb.render(sb));
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
        this.remove_check_counter--;
        if (this.remove_check_counter >= 0) return;
        this.remove_check_counter = 10;
        this.forEachOrbInThisOrbGroup(CardOrb.class, orb -> {
            orb.checkShouldRemove();
            if (orb.shouldRemove || CardUtility.isNotInBattle())
                AutoDoneInstantAction.addToBotAbstract(() -> evokeOrb(orb));
        });
    }


    public void evokeOrb(CardOrb exampleCardOrb) {
        for (int i = 0; i < orbs.size(); i++) {
            AbstractOrb orb = orbs.get(i);
            if (!(orb instanceof CardOrb)) continue;
            CardOrb cardOrb = (CardOrb) orb;
            if (!Objects.equals(cardOrb, exampleCardOrb)) continue;
            evokeOrbAndNotFill(i);
        }
        this.decreaseMaxOrbs(1);
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
        this.forEachOrbInThisOrbGroup(CardOrb_CardTrigger.class, CardOrb_CardTrigger::onCardUsed, abstractCard);
        Logger.temp("receiveCardUsed+GroupOrb");
    }

//    @Override
//    public boolean receivePreMonsterTurn(AbstractMonster abstractMonster) {
//        AutoDoneInstantAction.addToBotAbstract(() -> {
//            int bound = orbs.size();
//            for (int i = 0; i < bound; i++) {
//                this.evokeOrb(0);
//            }
//            this.decreaseMaxOrbs(orbs.size());
//        });
//        return super.receivePreMonsterTurn(abstractMonster);
//    }
}
