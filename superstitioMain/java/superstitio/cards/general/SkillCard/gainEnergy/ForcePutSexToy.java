package superstitio.cards.general.SkillCard.gainEnergy;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cards.general.GeneralCard;
import superstitio.powers.SexToy;
import superstitioapi.actions.AutoDoneInstantAction;

import static superstitio.cards.general.TempCard.SexToy.getRandomSexToyName;

public class ForcePutSexToy extends GeneralCard {
    public static final String ID = DataManager.MakeTextID(ForcePutSexToy.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.COMMON;

    public static final CardTarget CARD_TARGET = CardTarget.SELF;

    private static final int COST = 1;
    private static final int MAGIC = 1;
    private static final int UPGRADE_MAGIC = 1;
    private static final int DAMAGE = 1;

    private static final int UPGRADE_DAMAGE = 1;

    public ForcePutSexToy() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
//        this.setupDamage(DAMAGE, UPGRADE_DAMAGE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        for (int i = 0; i < magicNumber; i++) {
            addToBot_applyPower(new SexToy(AbstractDungeon.player, 1, getRandomSexToyName()));
        }
        addToBot(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, this.magicNumber));
        AutoDoneInstantAction.addToBotAbstract(() -> AbstractDungeon.player.powers.stream()
                .filter(power -> power instanceof SexToy)
                .map(power -> (SexToy) power)
                .forEach(SexToy::Trigger));
    }

    @Override
    public void upgradeAuto() {
    }
}
