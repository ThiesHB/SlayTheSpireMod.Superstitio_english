package SuperstitioMod.cards.ChooseSelfOrEnemy;

public interface ChooseSelfOrEnemyTarget {
    public boolean checkIsTargetSelfOrEnemy();
    public void setTarget_SelfOrEnemy();

    public int getShowAttractNum();

    public void setShowAttractNum(int amount);
}
