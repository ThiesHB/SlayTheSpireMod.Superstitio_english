package SuperstitioMod.relics.interFace;

public interface Countdown {

    int Zero = 0;
    int DefaultCounterOfRelic = -1;
    int getCounter();

    void setCounter(int CounterNum);

    default int tryGetCounter() {
        if (getCounter() == DefaultCounterOfRelic) {
            setCounter(getStarterNum());
        }
        return getCounter();
    }

    default void CountReduce() {
        setCounter(tryGetCounter() - 1);
        CountCheck();
    }

    default void CountCheck() {
        if (tryGetCounter() != Zero) return;
        onCountZero();
        if (ShouldRepeat())
            setCounter(getStarterNum());
    }

    void onCountZero();

    default boolean ShouldRepeat() {
        return true;
    }

    int getStarterNum();
}
