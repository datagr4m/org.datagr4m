package org.datagr4m.trials.topology;

import java.util.ArrayList;
import java.util.List;

public class TrialNeo4jTopologyEditorMockGraph {
	public void testProjection() {
	}
	
    public List<String> getGraphModelConfig() {
        List<String> conf = new ArrayList<String>();
        conf.add("type Planet{");
        conf.add(" property: name");
        conf.add(" label: name");
        conf.add("}");
        conf.add("type Specy{");
        conf.add(" property: name");
        conf.add(" label: name");
        conf.add("}");
        conf.add("type Planet{");
        conf.add(" property: name");
        conf.add(" label: name");
        conf.add("}");

        return conf;
    }
}
