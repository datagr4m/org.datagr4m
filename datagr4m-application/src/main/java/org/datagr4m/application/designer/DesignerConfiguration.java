package org.datagr4m.application.designer;

public class DesignerConfiguration {

    public boolean isDeviceTreeDisplayed() {
        return deviceTreeDisplayed;
    }

    public void setDeviceTreeDisplayed(boolean deviceTreeDisplayed) {
        this.deviceTreeDisplayed = deviceTreeDisplayed;
    }
    
    public boolean isAnimateSearchResults() {
		return animateSearchResults;
	}

	public void setAnimateSearchResults(boolean animateSearchResults) {
		this.animateSearchResults = animateSearchResults;
	}

	protected boolean deviceTreeDisplayed = true;
    protected boolean animateSearchResults = false;
}
