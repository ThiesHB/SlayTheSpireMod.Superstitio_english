package superstitioapi.utils;


/**
 * 使用方法：在updateDescriptionArgs中放入setDescriptionArgs函数即可使用
 * 使用前需要覆盖自身的updateDescription以接入，否则不会生效
 */
public interface updateDescriptionAdvanced {
    void setDescriptionArgs(Object... args);

    void updateDescriptionArgs();

    String getDescriptionStrings();
}
