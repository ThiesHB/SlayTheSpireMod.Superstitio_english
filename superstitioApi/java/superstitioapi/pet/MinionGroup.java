package superstitioapi.pet;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;
import java.util.stream.Collectors;

public class MinionGroup extends MonsterGroup {
    public MinionGroup(AbstractMonster[] input) {
        super(input);
    }

    public List<AbstractMonster> getMonsterInMinion() {
        return this.monsters.stream()
                .filter(monster -> monster instanceof Minion)
                .map(monster -> ((Minion) monster).monster)
                .collect(Collectors.toList());
    }

    @Override
    public void update() {
        for (AbstractMonster monster : this.monsters) {
            monster.update();
        }
        if (AbstractDungeon.screen == AbstractDungeon.CurrentScreen.DEATH) {
            this.hoveredMonster = null;
            return;
        }
        this.hoveredMonster = null;
        for (AbstractMonster monster : this.getMonsterInMinion()) {
            if (monster.isDying || monster.isEscaping) continue;
            monster.hb.update();
            monster.intentHb.update();
            monster.healthHb.update();
            if (!monster.hb.hovered && !monster.intentHb.hovered && !monster.healthHb.hovered) continue;
            if (!AbstractDungeon.player.isDraggingCard) {
                this.hoveredMonster = monster;
                break;
            }
        }
        if (this.hoveredMonster == null) {
            AbstractDungeon.player.hoverEnemyWaitTimer = -1.0F;
        }
    }

    @Override
    public void applyEndOfTurnPowers() {
        for (final AbstractMonster m : this.getMonsterInMinion()) {
            if (!m.isDying && !m.isEscaping) {
                m.applyEndOfTurnTriggers();
            }
        }
        for (final AbstractMonster m : this.getMonsterInMinion()) {
            if (!m.isDying && !m.isEscaping) {
                for (final AbstractPower p2 : m.powers) {
                    p2.atEndOfRound();
                }
            }
        }
    }

    @Override
    public void applyPreTurnLogic() {
        for (final AbstractMonster m : this.getMonsterInMinion()) {
            if (!m.isDying && !m.isEscaping) {
                if (!m.hasPower("Barricade")) {
                    m.loseBlock();
                }
                m.applyStartOfTurnPowers();
            }
        }
    }
}
