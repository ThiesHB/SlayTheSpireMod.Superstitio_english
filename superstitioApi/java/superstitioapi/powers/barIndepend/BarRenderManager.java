package superstitioapi.powers.barIndepend;

import basemod.interfaces.OnPowersModifiedSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitioapi.utils.RenderInBattle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BarRenderManager implements RenderInBattle, OnPowersModifiedSubscriber {
    //    private final AbstractCreature creature;
    private final List<RenderOnThing> powerBars = new ArrayList<>();

    public BarRenderManager() {
//        this.creature = creature;
        RenderInBattle.Register(RenderType.Panel, this);
    }

    public Stream<HasBarRenderOnCreature> findPowers() {
        return getAllPowers().stream().filter(power -> power instanceof HasBarRenderOnCreature).map(power -> (HasBarRenderOnCreature) power);
    }

    private ArrayList<AbstractPower> getAllPowers() {
        ArrayList<AbstractPower> allPower = new ArrayList<>(AbstractDungeon.player.powers);
        AbstractDungeon.getMonsters().monsters.forEach(monster -> allPower.addAll(monster.powers));
        return allPower;
    }

    public Optional<RenderOnThing> findMatch_powerPointToBar(HasBarRenderOnCreature power) {
        return powerBars.stream().filter(bar -> Objects.equals(bar.uuid_self, power.uuidPointTo())).findAny();
    }

    public Optional<HasBarRenderOnCreature> findMatch_barHasPower(RenderOnThing bar) {
        return findPowers().filter(power -> bar.isUuidInThis(power.uuidPointTo())).findAny();
    }

    public void AutoRegisterAndRemove() {
        List<HasBarRenderOnCreature> power_HasNoBar =
                findPowers().filter(power -> !findMatch_powerPointToBar(power).isPresent()).collect(Collectors.toList());
        List<RenderOnThing> bar_HasNoPower =
                powerBars.stream().filter(bar -> !findMatch_barHasPower(bar).isPresent()).collect(Collectors.toList());
        bar_HasNoPower.forEach(this.powerBars::remove);
        power_HasNoBar.forEach(power -> powerBars.add(power.makeNewBarRenderOnCreature().apply(power::getBarRenderHitBox, power)));
    }

    public void AutoMakeMessage() {
        findPowers().forEach(power -> powerBars.forEach(barRenderOnCreature -> barRenderOnCreature.tryApplyMessage(power.makeMessage())));
    }


    @Override
    public void render(SpriteBatch sb) {
        powerBars.forEach(bar -> bar.render(sb));
    }

    @Override
    public void update() {
        powerBars.forEach(RenderOnThing::update);
    }

    @Override
    public void receivePowersModified() {
        AutoRegisterAndRemove();
        AutoMakeMessage();
    }
}
