package superstitio.customStrings;

public interface HasSFWVersionWithT<T> extends HasSFWVersion {
    T getRightVersion();

    void initialOrigin();

    Class<T> getTClass();
}
