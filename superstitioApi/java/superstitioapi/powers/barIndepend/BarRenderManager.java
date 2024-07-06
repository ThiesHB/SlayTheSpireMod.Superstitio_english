package superstitioapi.powers.barIndepend;

import basemod.interfaces.OnPowersModifiedSubscriber;
import basemod.interfaces.PostPowerApplySubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import superstitioapi.renderManager.inBattleManager.RenderInBattle;
import superstitioapi.utils.CreatureUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BarRenderManager implements RenderInBattle, OnPowersModifiedSubscriber, PostPowerApplySubscriber {
    //    private final AbstractCreature creature;
    private final List<RenderOnThing> bars = new ArrayList<>();

    public BarRenderManager() {
//        this.creature = creature;
        RenderInBattle.Register(RenderType.Panel, this);
    }

    /**
     * 如果多个显示在同一个BAR上面，则必须调用这个函数，否则会显示错误
     */
    public void removeChunk(HasBarRenderOnCreature hasBarRenderOnCreature) {
        bars.forEach(bar -> bar.removeChunk(hasBarRenderOnCreature));
    }

    public Stream<HasBarRenderOnCreature> findPowers() {
        return getAllPowers().stream().filter(power -> power instanceof HasBarRenderOnCreature).map(power -> (HasBarRenderOnCreature) power);
    }

    public Optional<RenderOnThing> findMatch_powerPointToBar(HasBarRenderOnCreature power) {
        return bars.stream().filter(bar -> Objects.equals(bar.uuid_self, power.uuidPointTo())).findAny();
    }

    public Optional<HasBarRenderOnCreature> findMatch_barHasPower(RenderOnThing bar) {
        return findPowers().filter(power -> bar.isUuidInThis(power.uuidOfSelf())).findAny();
    }

    public void AutoRegisterAndRemove() {
        List<HasBarRenderOnCreature> power_HasNoBar =
                findPowers().filter(power -> !findMatch_powerPointToBar(power).isPresent()).collect(Collectors.toList());
        List<RenderOnThing> bar_HasNoPower =
                bars.stream().filter(bar -> !findMatch_barHasPower(bar).isPresent()).collect(Collectors.toList());
        for (RenderOnThing renderOnThing : bar_HasNoPower) {
            this.bars.remove(renderOnThing);
        }
        for (HasBarRenderOnCreature power : power_HasNoBar) {
            RenderOnThing added = power.makeNewBarRenderOnCreature().apply(power::getBarRenderHitBox, power);
            bars.add(added);
        }
    }

    public void AutoMakeMessage() {
        findPowers().forEach(power -> bars.forEach(barRenderOnCreature -> barRenderOnCreature.tryApplyMessage(power.makeMessage())));
    }

    private ArrayList<AbstractPower> getAllPowers() {
        return CreatureUtility.getListMemberFromPlayerAndEachMonsters(creature -> creature.powers);
    }

    @Override
    public void render(SpriteBatch sb) {
        bars.forEach(bar -> bar.render(sb));
    }

    @Override
    public void update() {
        bars.forEach(RenderOnThing::update);
    }

    @Override
    public void receivePowersModified() {
        AutoRegisterAndRemove();
        AutoMakeMessage();
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower abstractPower, AbstractCreature abstractCreature, AbstractCreature abstractCreature1) {
        AutoRegisterAndRemove();
        AutoMakeMessage();
    }
}
