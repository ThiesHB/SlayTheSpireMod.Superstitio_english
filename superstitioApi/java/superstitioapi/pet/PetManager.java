package superstitioapi.pet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitioapi.SuperstitioApiSubscriber;
import superstitioapi.actions.AutoDoneInstantAction;
import superstitioapi.utils.RenderInBattle;

import java.util.ArrayList;
import java.util.List;

import static superstitioapi.actions.AutoDoneInstantAction.*;

public class PetManager implements RenderInBattle, SuperstitioApiSubscriber.AtEndOfPlayerTurnSubscriber {
    public final List<AbstractMonster> monsters = new ArrayList<>();

    public void addPet(AbstractMonster monster) {
        monsters.add(monster);
    }

    @Override
    public void receiveAtEndOfPlayerTurn() {
        monsters.forEach(AbstractMonster::rollMove);
    }

    @Override
    public void render(SpriteBatch sb) {
        monsters.forEach(monster -> render(sb));
    }

    @Override
    public void update() {
        monsters.forEach(monster -> {
            if (monster.isDeadOrEscaped())
                addToBotAbstract(() -> monsters.remove(monster));
        });
    }
}
