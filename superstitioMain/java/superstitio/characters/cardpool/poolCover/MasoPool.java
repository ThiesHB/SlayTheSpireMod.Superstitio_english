package superstitio.characters.cardpool.poolCover;

import com.megacrit.cardcrawl.cards.AbstractCard;
import superstitio.DataManager;
import superstitio.cards.CardOwnerPlayerManager;
import superstitio.cards.maso.SkillCard.CutWrist;

import java.util.function.Predicate;

public class MasoPool extends AbstractCover {
    public static final String ID = DataManager.MakeTextID(MasoPool.class);

    public MasoPool() {
        super(ID, new CutWrist());
    }

    @Override
    public Predicate<AbstractCard> getAddedCard() {
        return CardOwnerPlayerManager::isOnlyMasoCard;
    }

    @Override
    public Predicate<AbstractCard> getBanedCard() {
        return null;
    }
}
