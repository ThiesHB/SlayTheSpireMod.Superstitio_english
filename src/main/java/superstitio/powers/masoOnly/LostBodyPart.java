package superstitio.powers.masoOnly;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import superstitio.cards.general.FuckJob_Card;
import superstitio.powers.AbstractSuperstitioPower;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class LostBodyPart extends AbstractSuperstitioPower {

    private final List<FuckJob_Card.BodyPart> banedBodyPart;

    protected LostBodyPart(String id) {
        super(id, AbstractDungeon.player, -1, PowerType.BUFF, true);
        banedBodyPart = Arrays.stream(banedBodyPart()).collect(Collectors.toList());
    }

    @Override
    public void updateDescriptionArgs() {
    }

    @Override
    public boolean canPlayCard(AbstractCard card) {
        if (!(card instanceof FuckJob_Card)) return true;
        return canUseBody((FuckJob_Card) card);
    }

    private boolean canUseBody(FuckJob_Card card) {
        return !banedBodyPart.contains(FuckJob_Card.getBodyPartType(card));
    }

    public abstract FuckJob_Card.BodyPart[] banedBodyPart();
}
