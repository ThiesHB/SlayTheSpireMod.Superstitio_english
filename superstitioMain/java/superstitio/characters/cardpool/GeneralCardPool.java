package superstitio.characters.cardpool;

import superstitio.characters.cardpool.poolCover.GeneralPool;
import superstitioapi.cardPool.BaseCardPool;

public class GeneralCardPool extends BaseCardPool {
    private static final GeneralPool generalCover = new GeneralPool();


    public GeneralCardPool() {
        super(generalCover, 0, 0);
    }
}
