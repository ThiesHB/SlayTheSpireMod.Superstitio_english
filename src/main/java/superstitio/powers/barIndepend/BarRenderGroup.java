package superstitio.powers.barIndepend;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BarRenderGroup implements RenderInBattle {
    private final AbstractCreature creature;
    List<BarRenderOnCreature_Power> powerBars = new ArrayList<>();

    public BarRenderGroup(AbstractCreature creature) {
        this.creature = creature;
        RenderInBattle.Register(this);
    }


    public static void RegisterToBarRenderOnCreature(final HasBarRenderOnCreature_Power owner, String uuid) {
        for (RenderInBattle renderInBattle : RenderInBattle.RENDER_IN_BATTLES) {
            if (!(renderInBattle instanceof BarRenderOnCreature_Power)) continue;
            BarRenderOnCreature_Power barRenderOnCreature = (BarRenderOnCreature_Power) renderInBattle;
            if (!Objects.equals(barRenderOnCreature.uuid_self, uuid)) continue;
            owner.setupAmountBar(barRenderOnCreature);
            return;
        }
        owner.setupAmountBar(new BarRenderOnCreature_Power(owner, uuid));
    }

    public Stream<HasBarRenderOnCreature_Power> findPowers() {
        return creature.powers.stream().filter(power -> power instanceof HasBarRenderOnCreature_Power).map(power -> (HasBarRenderOnCreature_Power) power);
    }

    public Optional<BarRenderOnCreature_Power> findMatch(HasBarRenderOnCreature_Power power) {
        return powerBars.stream().filter(bar -> Objects.equals(bar.uuid_self, power.uuidForBarRender())).findAny();
    }

    public Optional<HasBarRenderOnCreature_Power> findMatch(BarRenderOnCreature_Power bar) {
        return findPowers().filter(power -> Objects.equals(bar.uuid_self, power.uuidForBarRender())).findAny();
    }

    public void AutoRegister() {
        ArrayList<HasBarRenderOnCreature_Power> power_HasNoBar =
                findPowers().filter(power -> !findMatch(power).isPresent()).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<BarRenderOnCreature_Power> bar_HasNoPower =
                powerBars.stream().filter(bar -> !findMatch(bar).isPresent()).collect(Collectors.toCollection(ArrayList::new));
        bar_HasNoPower.forEach(bar -> this.powerBars.remove(bar));
    }


    @Override
    public void render(SpriteBatch sb) {
        powerBars.forEach(bar -> bar.render(sb));
    }

    @Override
    public void update() {
        powerBars.forEach(BarRenderOnCreature::update);
    }
}
