package superstitio.customStrings.interFace;

public interface HasDifferentVersionStringSet<T> {

    T getRightVersion();

    void initial();

    void initialSelfBlack();

    T getOriginVersion();

    void initialOrigin(T origin);

    Class<T> getSubClass();
}

