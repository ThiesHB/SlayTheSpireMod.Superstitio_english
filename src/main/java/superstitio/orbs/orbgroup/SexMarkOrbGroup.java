package superstitio.orbs.orbgroup;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.megacrit.cardcrawl.cards.CardQueueItem;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import superstitio.InBattleDataManager;
import superstitio.actions.AutoDoneInstantAction;
import superstitio.cards.lupa.TempCard.GangBang;
import superstitio.orbs.SexMarkEmptySlot;
import superstitio.orbs.SexMarkOrb;
import superstitio.orbs.SexMarkOrb_Inside;
import superstitio.orbs.SexMarkOrb_Outside;
import superstitio.orbs.actions.GiveSexMarkToOrbGroupInstantAction;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class SexMarkOrbGroup extends OrbGroup {
    public static final int SexMarkSetupOrbMax = 5;
    private final boolean fillSide;


    public SexMarkOrbGroup(Hitbox hitbox) {
        super(hitbox, SexMarkSetupOrbMax, new SexMarkEmptySlot());
        this.hitbox.moveX(this.hitbox.cX + this.hitbox.width);
        this.hitbox.moveY(this.hitbox.cY - this.hitbox.height * 0.25f);
        this.letEachOrbToSlotPlaces();
        this.fillSide = MathUtils.randomBoolean();
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static SexMarkOrb makeSexMarkOrb(SexMarkType sexMarkType) {
        switch (sexMarkType) {
            case OutSide:
                return new SexMarkOrb_Outside();
            case Inside:
                return new SexMarkOrb_Inside();
            default:
                return new SexMarkOrb_Inside();
        }
    }

    public static void addToBot_GiveMarkToOrbGroup(String sexName, SexMarkOrbGroup.SexMarkType sexMarkType) {
        AbstractDungeon.actionManager.addToBottom(
                new GiveSexMarkToOrbGroupInstantAction((SexMarkOrbGroup) InBattleDataManager.orbGroups.stream()
                        .filter(orbGroup -> orbGroup instanceof SexMarkOrbGroup).findAny().orElse(null),
                        makeSexMarkOrb(sexMarkType).setSexMarkName(sexName)));
    }

    @Override
    protected Vector2 makeSlotPlace(int slotIndex) {
        return makeSlotPlaceHeart(this.hitbox.width, slotIndex);
    }

    protected final Vector2 makeSlotPlaceHeart(final float scale, final int slotIndex) {
        return makeHeartLine2(scale, OffsetPercentageBySlotIndex_Cycle(slotIndex) * 360 + ((this.GetMaxOrbs() % 2 == 0) ? 0 : -144));
    }

    protected final Vector2 makeHeartLine1(final float scale, final float angle) {
        return new Vector2(1, 0)
                .setLength((1 - MathUtils.sinDeg(angle)) * scale)
                .setAngle(angle);
    }

    protected final Vector2 makeHeartLine2(final float scale, float angle) {
        Vector2 vector2 = new Vector2();
        float sinDeg = MathUtils.sinDeg(angle);
        vector2.x = 16 * sinDeg * sinDeg * sinDeg;
        vector2.y = 15 * MathUtils.cosDeg(angle) - 5 * MathUtils.cosDeg(2 * angle) - 2 * MathUtils.cosDeg(3 * angle) - MathUtils.cosDeg(4 * angle);
        vector2.x = vector2.x * (scale / 32);
        vector2.y = vector2.y * (scale / 32);
        return vector2;
    }

    protected final Vector2 makeHeartLine3(final float scale, float angle) {
        Vector2 vector2 = new Vector2();
        float sinDeg = MathUtils.sinDeg(angle);
        vector2.x = 16 * sinDeg;
        vector2.y = 15 * MathUtils.cosDeg(angle) - 6 * MathUtils.cosDeg(2 * angle) - 2 * MathUtils.cosDeg(3 * angle);
        vector2.x = vector2.x * (scale / 48);
        vector2.y = vector2.y * (scale / 48);
        return vector2;
    }

    public void evokeOrb(SexMarkOrb exampleSexMarkOrb) {
        for (int i = 0; i < orbs.size(); i++) {
            AbstractOrb orb = orbs.get(i);
            if (!(orb instanceof SexMarkOrb)) continue;
            SexMarkOrb markOrb = (SexMarkOrb) orb;
            if (!Objects.equals(markOrb.sexMarkName, exampleSexMarkOrb.sexMarkName)) continue;
            evokeOrbAndNotFill(i);
        }
    }

    @Override
    protected void onOrbChannel(AbstractOrb channeledOrb) {
        if (this.hasEmptySlot()) return;
        AtomicInteger attackAmount = new AtomicInteger();
        AtomicInteger blockAmount = new AtomicInteger();
        orbs.forEach(orb -> {
            if (isEmptySlot(orb)) return;
            if (orb instanceof SexMarkOrb) {
                SexMarkOrb sexMarkOrb = (SexMarkOrb) orb;
                attackAmount.addAndGet(sexMarkOrb.attack());
                blockAmount.addAndGet(sexMarkOrb.block());
            }
        });
        GangBang gangBang = new GangBang(attackAmount.get(), blockAmount.get() , ScoreTheGangBang());
        AutoDoneInstantAction.addToBotAbstract(() -> AbstractDungeon.actionManager.addCardQueueItem(new CardQueueItem(gangBang, null, 0, true,
                true), true));
        AutoDoneInstantAction.addToBotAbstract(() -> {
            int bound = orbs.size();
            for (int i = 0; i < bound; i++) {
                this.evokeOrbAndNotFill(i);
            }
        });
    }

    private int ScoreTheGangBang() {
        return orbs.stream().filter(orb -> orb instanceof SexMarkOrb).map(orb -> ((SexMarkOrb) orb).sexMarkName).collect(Collectors.toSet()).size();
    }

    @Override
    public int findFirstEmptyOrb() {
        int index = -1;
        for (int i = 0; i < orbs.size(); ++i) {
            AbstractOrb o = orbs.get(MapIndex(i));
            if (isEmptySlot(o)) {
                index = MapIndex(i);
                break;
            }
        }
        return index;
    }

    private int MapIndex(final int index) {
        final int countTotal = this.GetMaxOrbs();
        if (countTotal % 2 == 1) {
            return MapIndexTool(index, countTotal);
        } else {
            int plusOneResult = MapIndexTool(index, countTotal + 1);
            if (plusOneResult == countTotal) return 0;
            else return plusOneResult;
        }
    }

    private int MapIndexTool(final int index, final int countTotal) {
        int mid = (countTotal - 1) / 2;
        if (index % 2 == 1) {
            if (fillSide) return mid + (index + 1) / 2;
            return mid - (index + 1) / 2;
        } else {
            if (fillSide) return mid - index / 2;
            return mid + index / 2;
        }
    }

    @Override
    protected void onOrbEvoke(AbstractOrb evokedOrb) {
    }

    public enum SexMarkType {
        Inside, OutSide
    }
}
