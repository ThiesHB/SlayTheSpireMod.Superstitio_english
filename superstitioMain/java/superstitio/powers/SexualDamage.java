package superstitio.powers;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import superstitio.DataManager;
import superstitio.delayHpLose.UnBlockAbleIgnoresTempHPDamage;
import superstitioapi.InBattleDataManager;
import superstitioapi.SuperstitioApiSubscriber;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.powers.interfaces.OnPostApplyThisPower;

import static superstitioapi.InBattleDataManager.subscribeManageGroups;

public class SexualDamage extends AbstractSuperstitioPower implements HealthBarRenderPower, OnPostApplyThisPower<SexualDamage>,
        SuperstitioApiSubscriber.AtEndOfPlayerTurnPreCardSubscriber {
    public static final String POWER_ID = DataManager.MakeTextID(SexualDamage.class);
    private static final Color BarColor = Color.PURPLE.cpy();
    protected final AbstractCreature giver;

    public SexualDamage(final AbstractCreature owner, int amount, AbstractCreature giver) {
        super(POWER_ID, owner, amount);
        this.giver = giver;
//        ReflectionHacks.setPrivate(this, "greenColor", Color.PINK.cpy());
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public int getHealthBarAmount() {
        return this.amount;
    }

    @Override
    public Color getColor() {
        return BarColor;
    }

    @Override
    public void renderAmount(SpriteBatch sb, float x, float y, Color c) {
        float temp = this.fontScale;
        this.fontScale *= 1.5f;
        super.renderAmount(sb, x, y, c);
        this.fontScale = temp;
    }

    @Override
    public void receiveAtEndOfPlayerTurnPreCard() {
//        this.owner.damage(BindingHelper.makeInfo(new DamageModContainer(this, new UnBlockAbleDamage()), giver, amount, DamageType.HP_LOSS));
        this.flash();
        DamageActionMaker.maker(this.giver, this.amount, this.owner)
                .setDamageModifier(this, new UnBlockAbleIgnoresTempHPDamage())
                .setDamageType(DamageInfo.DamageType.HP_LOSS)
//                .setDamageType(   DataManager.CanOnlyDamageDamageType.UnBlockAbleDamageType)
                .setEffect(AbstractGameAction.AttackEffect.POISON)
                .addToTop();
        addToBot_removeSpecificPower(this);
    }

    @Override
    public void InitializePostApplyThisPower(SexualDamage addedPower) {
        if (subscribeManageGroups.stream()
                .anyMatch(iSubscriber -> iSubscriber instanceof SexualDamage && ((SexualDamage) iSubscriber).owner == this.owner))
            return;
        InBattleDataManager.Subscribe(this);
    }

    @Override
    public void onRemove() {
        InBattleDataManager.UnSubscribe(this);
    }
}
