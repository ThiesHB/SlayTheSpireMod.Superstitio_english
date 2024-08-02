package superstitio.cards.maso.SkillCard;

import basemod.cardmods.EtherealMod;
import basemod.helpers.CardModifierManager;
import com.evacipated.cardcrawl.mod.stslib.cards.targeting.SelfOrEnemyTargeting;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import superstitio.DataManager;
import superstitio.cardModifier.modifiers.damage.SexDamage;
import superstitio.cards.SuperstitioCard;
import superstitio.cards.maso.MasoCard;
import superstitio.powers.EasyBuildAbstractPowerForPowerCard;
import superstitioapi.SuperstitioApiSetup;
import superstitioapi.cards.DamageActionMaker;
import superstitioapi.utils.CreatureUtility;

import java.util.HashMap;
import java.util.Map;

import static superstitioapi.utils.CardUtility.getSelfOrEnemyTarget;


public class HumanCentipede extends MasoCard {
    public static final String ID = DataManager.MakeTextID(HumanCentipede.class);

    public static final CardType CARD_TYPE = CardType.SKILL;

    public static final CardRarity CARD_RARITY = CardRarity.UNCOMMON;

    public static final CardTarget CARD_TARGET = SelfOrEnemyTargeting.SELF_OR_ENEMY;

    private static final int COST = 1;
    private static final int MAGIC = 8;
    private static final int UPGRADE_MAGIC = 3;

    public HumanCentipede() {
        super(ID, CARD_TYPE, COST, CARD_RARITY, CARD_TARGET);
        this.setupMagicNumber(MAGIC, UPGRADE_MAGIC);
        DamageModifierManager.addModifier(this, new SexDamage());
        CardModifierManager.addModifier(this, new EtherealMod());
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster monster) {
        AbstractCreature target = getSelfOrEnemyTarget(this, monster);
        addToBot_applyPower(new HumanCentipedePower(target, this.magicNumber));
    }

    @Override
    public void upgradeAuto() {
    }

    public static class HumanCentipedePower extends EasyBuildAbstractPowerForPowerCard {
        public HumanCentipedePower(AbstractCreature owner, int amount) {
            super(owner, amount);
        }

        @Override
        public String getDescriptionStrings() {
            return powerCard.getEXTENDED_DESCRIPTION()[0];
        }

        @Override
        public int onAttacked(DamageInfo info, int damageAmount) {
            if (damageAmount > 0 && info.type == DamageInfo.DamageType.NORMAL) {
                Map<AbstractCreature, Integer> map = new HashMap<>();
                for (AbstractMonster monster : CreatureUtility.getAllAliveMonsters()) {
                    map.put(monster, this.amount);
                }
                map.put(AbstractDungeon.player, this.amount);
                DamageActionMaker.makeDamages(map)
                        .setSource(this.owner)
                        .setDamageType(DamageInfo.DamageType.THORNS)
                        .setEffect(SuperstitioApiSetup.DamageEffect.HeartMultiInOne)
                        .setDamageModifier(this,new SexDamage())
                        .addToBot();
            }

            return super.onAttacked(info, damageAmount);
        }

        @Override
        public void updateDescriptionArgs() {
            setDescriptionArgs(this.amount);
        }

        @Override
        protected SuperstitioCard makePowerCard() {
            return new HumanCentipede();
        }
    }
}
