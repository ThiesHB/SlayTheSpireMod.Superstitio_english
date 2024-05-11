package superstitio.powers.barIndepend;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import superstitio.utils.CardUtility;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 在Register之后就可以自动绘制和更新
 */
public interface RenderInBattle {

    ArrayList<RenderInBattle> RENDER_IN_BATTLES = new ArrayList<>();

    static void Register(RenderInBattle... renderThings) {
        Collections.addAll(RENDER_IN_BATTLES, renderThings);
    }

    void render(SpriteBatch sb);

    void update();

    default void updateAnimation() {
    }

    class RenderInBattlePatch {
        @SpirePatch(clz = AbstractRoom.class, method = "update")
        public static class MonsterUpdateOrbPatch {
            public static void Postfix(final AbstractRoom _inst) {
                if (CardUtility.isNotInBattle()) return;
                RENDER_IN_BATTLES.forEach(RenderInBattle::update);
                RENDER_IN_BATTLES.forEach(RenderInBattle::updateAnimation);
            }
        }

        @SpirePatch(clz = AbstractRoom.class, method = "render", paramtypez = {SpriteBatch.class})
        public static class MonsterRenderOrbPatch {
            @SpireInsertPatch(rloc = 13)
            public static void Insert(final AbstractRoom _inst, final SpriteBatch sb) {
                if (CardUtility.isNotInBattle()) return;
                sb.setColor(Color.WHITE);
                RENDER_IN_BATTLES.forEach(renderInBattle -> renderInBattle.render(sb));
            }
        }
    }
}
