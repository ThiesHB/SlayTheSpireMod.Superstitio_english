package superstitio.relics.interFace

interface Countdown {
    fun setCounter(value:Int)
    fun getCounter(): Int

    fun tryGetCounter(): Int {
        if (getCounter() == DefaultCounterOfRelic) {
            setCounter(getStarterNum())
        }
        return getCounter()
    }

    fun CountReduce() {
        setCounter( tryGetCounter() - 1)
        CountCheck()
    }

    fun CountCheck() {
        if (tryGetCounter() != Zero) return
        onCountZero()
        if (ShouldRepeat())
            setCounter(getStarterNum())
    }

    fun onCountZero()

    fun ShouldRepeat(): Boolean {
        return true
    }

    fun getStarterNum(): Int

    companion object {
        const val Zero: Int = 0
        const val DefaultCounterOfRelic: Int = -1
    }
}
