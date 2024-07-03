package superstitio.characters.cardpool;

import superstitio.characters.cardpool.poolCover.LupaPool;
import superstitioapi.cardPool.BaseCardPool;

public class LupaCardPool extends BaseCardPool {
    private static final LupaPool lupaCover = new LupaPool();


    public LupaCardPool() {
        super(lupaCover, 0, 0);
    }
}
