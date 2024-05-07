package superstitio.powers.barIndepend;

import basemod.interfaces.OnPowersModifiedSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.Hitbox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BarRenderGroup implements RenderInBattle, OnPowersModifiedSubscriber {
    private final AbstractCreature creature;
    private final Hitbox hitbox;
    private final List<BarRenderOnCreature> powerBars = new ArrayList<>();

    public BarRenderGroup(AbstractCreature creature) {
        this.creature = creature;
        RenderInBattle.Register(this);
        hitbox = new Hitbox(creature.hb.x, creature.hb.y, creature.hb.width, creature.hb.height);
    }

    public Stream<HasBarRenderOnCreature> findPowers() {
        return creature.powers.stream().filter(power -> power instanceof HasBarRenderOnCreature).map(power -> (HasBarRenderOnCreature) power);
    }

    public Optional<BarRenderOnCreature> findMatch_powerPointToBar(HasBarRenderOnCreature power) {
        return powerBars.stream().filter(bar -> Objects.equals(bar.uuid_self, power.uuidPointTo())).findAny();
    }

    public Optional<HasBarRenderOnCreature> findMatch_barHasPower(BarRenderOnCreature bar) {
        return findPowers().filter(power -> bar.isUuidInThis(power.uuidPointTo())).findAny();
    }

    public void AutoRegisterAndRemove() {
        ArrayList<HasBarRenderOnCreature> power_HasNoBar =
                findPowers().filter(power -> !findMatch_powerPointToBar(power).isPresent()).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<BarRenderOnCreature> bar_HasNoPower =
                powerBars.stream().filter(bar -> !findMatch_barHasPower(bar).isPresent()).collect(Collectors.toCollection(ArrayList::new));
        bar_HasNoPower.forEach(this.powerBars::remove);
        power_HasNoBar.forEach(power -> powerBars.add(new BarRenderOnCreature(hitbox, power)));
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
        powerBars.forEach(BarRenderOnCreature::update);
    }

    @Override
    public void receivePowersModified() {
        AutoRegisterAndRemove();
        AutoMakeMessage();
    }
}
