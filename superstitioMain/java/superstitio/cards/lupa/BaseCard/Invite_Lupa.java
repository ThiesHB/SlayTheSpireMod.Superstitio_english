package superstitio.cards.lupa.BaseCard;

import com.evacipated.cardcrawl.mod.stslib.blockmods.AbstractBlockModifier;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.block.SexBlock;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.general.BaseCard.Invite;

public class Invite_Lupa extends Invite implements CardOwnerPlayerManager.IsLupaCard {

    public static final String ID = DataManager.MakeTextID(Invite.class.getSimpleName(), Invite_Lupa.class);

    public Invite_Lupa() {
        super(ID);
    }

    @Override
    protected AbstractBlockModifier makeNewBlockType() {
        return new SexBlock();
    }
}
