package SuperstitioMod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class OrgasmAction extends AbstractGameAction {
        private AbstractPlayer player;
        private int cheaperAmount;

        public OrgasmAction(int cheaperAmount) {
            this.actionType = AbstractGameAction.ActionType.CARD_MANIPULATION;
            this.player = AbstractDungeon.player;
            this.duration = Settings.ACTION_DUR_FAST;
            this.cheaperAmount = cheaperAmount;
        }

        public void update() {
            this.isDone = true;
        }
}
