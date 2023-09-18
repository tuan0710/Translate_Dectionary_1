package com.example.translate_dectionary;

public class ModelLanguage {
    String laguageCode;
    String languageTitle;

    public ModelLanguage(String laguageCode, String languageTitle) {
        this.laguageCode = laguageCode;
        this.languageTitle = languageTitle;
    }

    public String getLaguageCode() {
        return laguageCode;
    }

    public void setLaguageCode(String laguageCode) {
        this.laguageCode = laguageCode;
    }

    public String getLanguageTitle() {
        return languageTitle;
    }

    public void setLanguageTitle(String languageTitle) {
        this.languageTitle = languageTitle;
    }
}
