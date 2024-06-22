package superstitioapi.pet;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.buttons.PeekButton;

import java.util.List;
import java.util.stream.Collectors;

public class MinionGroup extends MonsterGroup {

    public Minion hoveredCreature = null;

    public MinionGroup(AbstractMonster[] input) {
        super(input);
    }

    public List<AbstractCreature> getPetCores() {
        return this.monsters.stream()
                .filter(monster -> monster instanceof Minion)
                .map(monster -> ((Minion) monster).getPetCore())
                .collect(Collectors.toList());
    }

    public List<Minion> getAllMinion() {
        return this.monsters.stream()
                .filter(monster -> monster instanceof Minion)
                .map(monster -> (Minion) monster)
                .collect(Collectors.toList());
    }

    @Override
    public void render(SpriteBatch sb) {
        if (this.hoveredCreature != null && !this.hoveredCreature.isDead && !this.hoveredCreature.escaped && AbstractDungeon.player.hoverEnemyWaitTimer < 0.0F && (!AbstractDungeon.isScreenUp || PeekButton.isPeeking)) {
            this.hoveredCreature.renderTip(sb);
        }

        for (AbstractMonster m : this.monsters) {
            m.render(sb);
        }
    }

    @Override
    public void update() {
        for (AbstractMonster monster : this.monsters) {
            monster.update();
        }
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH) {
            this.hoveredCreature = null;
            return;
        }
        this.hoveredCreature = null;
        for (Minion monster : this.getAllMinion()) {
            if (monster.isDying || monster.isEscaping) continue;
            monster.updateHitBox();
            if (!monster.isHovered()) continue;
            if (!AbstractDungeon.player.isDraggingCard) {
                this.hoveredCreature = monster;
                break;
            }
        }
        if (this.hoveredCreature == null) {
            AbstractDungeon.player.hoverEnemyWaitTimer = -1.0F;
        }
    }

    @Override
    public void applyEndOfTurnPowers() {
        for (final AbstractCreature m : this.getPetCores()) {
            if (!m.isDying && !m.isEscaping) {
                m.applyEndOfTurnTriggers();
            }
        }
        for (final AbstractCreature m : this.getPetCores()) {
            if (!m.isDying && !m.isEscaping) {
                for (final AbstractPower p2 : m.powers) {
                    p2.atEndOfRound();
                }
            }
        }
    }

    @Override
    public void applyPreTurnLogic() {
        for (final AbstractCreature m : this.getPetCores()) {
            if (!m.isDying && !m.isEscaping) {
                if (!m.hasPower("Barricade")) {
                    m.loseBlock();
                }
                m.applyStartOfTurnPowers();
            }
        }
    }
}
