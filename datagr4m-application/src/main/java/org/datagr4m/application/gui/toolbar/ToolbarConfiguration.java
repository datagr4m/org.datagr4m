package org.datagr4m.application.gui.toolbar;

public class ToolbarConfiguration {
    protected boolean floatable = true;
    
    protected boolean buttonCrawl = true;
    protected boolean buttonImport = true;
    protected boolean buttonParse = true;
    
    protected boolean buttonModel = true;
    protected boolean buttonLayout = true;
    protected boolean buttonTopology = true;

    protected boolean buttonRun = true;

    protected boolean search = false;
    
    public boolean isFloatable() {
        return floatable;
    }

    public void setFloatable(boolean floatable) {
        this.floatable = floatable;
    }
    
    public boolean isButtonParse() {
        return buttonParse;
    }

    public void setButtonParse(boolean buttonParse) {
        this.buttonParse = buttonParse;
    }

    public boolean isButtonCrawl() {
        return buttonCrawl;
    }

    public void setButtonCrawl(boolean buttonCrawl) {
        this.buttonCrawl = buttonCrawl;
    }

    public boolean isButtonImport() {
        return buttonImport;
    }

    public void setButtonImport(boolean buttonImport) {
        this.buttonImport = buttonImport;
    }

    public boolean isButtonModel() {
        return buttonModel;
    }

    public void setButtonModel(boolean buttonModel) {
        this.buttonModel = buttonModel;
    }

    public boolean isButtonLayout() {
        return buttonLayout;
    }

    public void setButtonLayout(boolean buttonLayout) {
        this.buttonLayout = buttonLayout;
    }

    public boolean isButtonTopology() {
        return buttonTopology;
    }

    public void setButtonTopology(boolean buttonTopology) {
        this.buttonTopology = buttonTopology;
    }

    public boolean isButtonRun() {
        return buttonRun;
    }

    public void setButtonRun(boolean buttonRun) {
        this.buttonRun = buttonRun;
    }

    public boolean isSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }   
}
