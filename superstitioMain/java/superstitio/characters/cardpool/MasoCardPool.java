package superstitio.characters.cardpool;

import superstitio.characters.cardpool.poolCover.MasoPool;
import superstitioapi.cardPool.BaseCardPool;

public class MasoCardPool extends BaseCardPool {
    private static final MasoPool masoCover = new MasoPool();

    public MasoCardPool() {
        super(masoCover, 0, 0);
    }
}
