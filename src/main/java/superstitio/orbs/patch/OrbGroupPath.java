package superstitio.orbs.patch;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.defect.TriggerEndOfTurnOrbsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import superstitio.orbs.orbgroup.OrbGroup;

public class OrbGroupPath {
    @SpirePatch(clz = AbstractDungeon.class, method = "onModifyPower")
    public static class ModifyPowerFocusPatch {
        public static void Postfix() {
            if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) return;
            OrbGroup.forEachOrbInEachOrbGroup(AbstractOrb::updateDescription);
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "update")
    public static class MonsterUpdateOrbPatch {
        public static void Postfix(final AbstractRoom _inst) {
            if (_inst.phase != AbstractRoom.RoomPhase.COMBAT) return;
            OrbGroup.forEachOrbInEachOrbGroup(orb -> {
                orb.update();
                orb.updateAnimation();
            });
        }
    }

    @SpirePatch(clz = AbstractRoom.class, method = "render", paramtypez = {SpriteBatch.class})
    public static class MonsterRenderOrbPatch {
        @SpireInsertPatch(rloc = 13)
        public static void Insert(final AbstractRoom _inst, final SpriteBatch sb) {
            if (_inst.phase != AbstractRoom.RoomPhase.COMBAT) return;
            sb.setColor(Color.WHITE);
            OrbGroup.forEachOrbInEachOrbGroup(AbstractOrb::render, sb);
        }
    }

    @SpirePatch(clz = TriggerEndOfTurnOrbsAction.class, method = "update")
    public static class EndTurnEffectOrbPatch {
        public static void Postfix(final TriggerEndOfTurnOrbsAction _inst) {
            if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) return;
            OrbGroup.forEachOrbInEachOrbGroup(AbstractOrb::onEndOfTurn);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnOrbs")
    public static class StartTurnEffectOrbPatch {
        public static void Postfix(final AbstractPlayer _inst) {
            if (AbstractDungeon.getCurrRoom().phase != AbstractRoom.RoomPhase.COMBAT) return;
            OrbGroup.forEachOrbInEachOrbGroup(AbstractOrb::onStartOfTurn);
        }
    }
}
