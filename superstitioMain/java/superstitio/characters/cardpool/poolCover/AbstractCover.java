package superstitio.characters.cardpool.poolCover;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitioapi.cardPool.BaseCardPool;

@AutoAdd.Ignore
public abstract class AbstractCover extends GeneralCard implements BaseCardPool.IsCardPoolCover {
    public static final String ID = DataManager.MakeTextID(AbstractCover.class);

    public AbstractCover(String id, CustomCard symbol) {
        super(id, symbol.type, -2, symbol.rarity, symbol.target);
        this.textureImg = symbol.textureImg;
        if (textureImg != null) {
            this.loadCardImage(textureImg);
        }
    }

    @Override
    public AbstractCard getThis() {
        return this;
    }

    @Override
    public void upgradeAuto() {
    }

    @Override
    public void use(final AbstractPlayer player, final AbstractMonster monster) {
    }
}
