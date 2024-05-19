package superstitio.cards.maso.BaseCard;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import superstitio.DataManager;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.BaseCard.Invite;
import superstitio.delayHpLose.DelayRemoveDelayHpLoseBlock;

public class Invite_Maso extends Invite implements CardOwnerPlayerManager.IsMasoCard {
    public static final String ID = DataManager.MakeTextID(Invite.class.getSimpleName(), Invite_Maso.class);

    public Invite_Maso() {
        super(ID);
    }

    @Override
    protected AbstractBlockModifier makeNewBlockType() {
        return new DelayRemoveDelayHpLoseBlock();
    }
}
