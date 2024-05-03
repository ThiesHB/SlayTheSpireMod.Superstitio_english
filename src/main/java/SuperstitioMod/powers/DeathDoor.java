package SuperstitioMod.powers;

import SuperstitioMod.DataManager;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnPlayerDeathPower;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class DeathDoor extends AbstractLupaPower implements OnPlayerDeathPower {
    public static final String POWER_ID = DataManager.MakeTextID(DeathDoor.class.getSimpleName() );
    public DeathDoor(final AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount);
    }

    @Override
    public void updateDescriptionArgs() {
        setDescriptionArgs(this.amount);
    }

    @Override
    public boolean onPlayerDeath(AbstractPlayer abstractPlayer, DamageInfo damageInfo) {
        addToBot(new HealAction(this.owner,this.owner,1));
        addToBot_applyPowerToOwner(new AtDeathDoor(this.owner));
        addToBot_AutoRemoveOne(this);
        return false;
    }
}
