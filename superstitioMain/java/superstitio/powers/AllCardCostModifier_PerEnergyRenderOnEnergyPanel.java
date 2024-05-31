//package superstitio.powers;
//
//import com.badlogic.gdx.graphics.Color;
//import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
//import com.megacrit.cardcrawl.core.AbstractCreature;
//import com.megacrit.cardcrawl.helpers.Hitbox;
//import com.megacrit.cardcrawl.powers.AbstractPower;
//import superstitioapi.DataUtility;
//import superstitioapi.powers.AllCardCostModifier_PerEnergy;
//import superstitioapi.powers.barIndepend.HasBarRenderOnCreature;
//import superstitioapi.powers.barIndepend.HasBarRenderOnCreature_Power;
//import superstitioapi.powers.barIndepend.RenderOnThing;
//import superstitioapi.powers.barIndepend.TextRenderOnThing;
//import superstitioapi.powers.interfaces.HasAllCardCostModifyEffect;
//
//import java.util.function.BiFunction;
//import java.util.function.Supplier;
//
//public class AllCardCostModifier_PerEnergyRenderOnEnergyPanel extends AllCardCostModifier_PerEnergy
//        implements InvisiblePower, HasBarRenderOnCreature_Power {
//    public static final String POWER_ID = DataUtility.MakeTextID(AllCardCostModifier_PerEnergyRenderOnEnergyPanel.class);
//
//    public AllCardCostModifier_PerEnergyRenderOnEnergyPanel(AbstractCreature owner, int decreasedCost, int totalEnergy, HasAllCardCostModifyEffect holder) {
//        super(owner, decreasedCost, totalEnergy, holder);
//        this.ID = POWER_ID;
//    }
//
//    @Override
//    public BiFunction<Supplier<Hitbox>, HasBarRenderOnCreature, ? extends RenderOnThing> makeNewBarRenderOnCreature() {
//        return TextRenderOnThing::new;
//    }
//
//    @Override
//    public String uuidOfSelf() {
//        return this.ID;
//    }
//
//    @Override
//    public float Height() {
//        return SexualHeat.HEIGHT;
//    }
//
//    @Override
//    public Color setupBarOrginColor() {
//        return Color.WHITE.cpy();
//    }
//
//    @Override
//    public AbstractPower getSelf() {
//        return this;
//    }
//
//    @Override
//    public int maxBarAmount() {
//        return decreasedCost;
//    }
//
//    @Override
//    public String makeBarText() {
//        return "%d(%d)";
//    }
//}
