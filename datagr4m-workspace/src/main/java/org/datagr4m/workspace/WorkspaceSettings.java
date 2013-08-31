package org.datagr4m.workspace;

import java.io.File;

public class WorkspaceSettings {
	// public static String CRAWLS_FOLDER = "data/crawls/";

	public static String DATA = "data/";
	public static String WORKSPACES = "workspaces/";
	public static String CRAWLS = "crawls/";
	public static String WORKSPACES_FOLDER = DATA + WORKSPACES;
	
	public static String DEFAULT_WORKSPACE = "default";
	
	protected File data;// = new File(WORKSPACES_FOLDER);
	protected String workspacesFolder;
	protected String crawlsFolder;

	public WorkspaceSettings() {
		this(DEFAULT_WORKSPACE);
	}
	
	public WorkspaceSettings(String name) {
		this(new File(DATA), name);
	}
	
	public WorkspaceSettings(final File data) {
		this(data, DEFAULT_WORKSPACE);
	}

	public WorkspaceSettings(final File data, String name) {
		this.data = data;
		this.workspacesFolder = data.getPath() + "/" + WORKSPACES;
		this.crawlsFolder = data.getPath() + "/" +CRAWLS;
		setName(name);
	}

	public void setName(String name) {
		this.name = name;
		this.workspaceFolder = workspacesFolder + name;

		// content
		dataprismFile = workspaceFolder + "/dataprism.xml";
		repositoryMLFile = workspaceFolder + "/devices.xml";
		topologyMLFile = workspaceFolder + "/topology.xml";
		layoutMLFile = workspaceFolder + "/layout.xml";
		mapBinFile = workspaceFolder + "/map.bin";

		// cache
		repositoryCache = workspaceFolder + "/cache/repository.bin";
		workspaceCache = workspaceFolder + "/cache/workspace.bin";
		topologyCache = workspaceFolder + "/cache/topology.bin";
		visibilityCache = workspaceFolder + "/cache/visibility.bin";
		linkProcessorCache = workspaceFolder + "/cache/links.bin";
		graphCache = workspaceFolder + "/cache/graph.bin";
		graphMLCache = workspaceFolder + "/cache/graph.graphml";
		renamerCache = workspaceFolder + "/cache/renamer.bin";
		hsrpMatrixCache = workspaceFolder + "/cache/hsrp-matrix-debug.csv";

		// confs
		confsFolder = crawlsFolder + name + "/";
	}

	public String getName() {
		return name;
	}

	public String getWorkspaceFolder() {
		return workspaceFolder;
	}
	
	public String getDataprismFile() {
        return dataprismFile;
    }

    public String getTopologyMLFile() {
		return topologyMLFile;
	}

	public String getLayoutMLFile() {
		return layoutMLFile;
	}

	public String getRepositoryMLFile() {
		return repositoryMLFile;
	}
	
	public String getMapBinFile() {
        return mapBinFile;
    }

    public String getRepositoryCache() {
		return repositoryCache;
	}

	public String getWorkspaceCache() {
		return workspaceCache;
	}

	public String getTopologyCache() {
		return topologyCache;
	}

	public String getVisibilityCache() {
		return visibilityCache;
	}

	public String getGraphCache() {
		return graphCache;
	}

	public String getGraphMLCache() {
		return graphMLCache;
	}

	public String getRenamerCache() {
		return renamerCache;
	}

	public String getHsrpMatrixCache() {
		return hsrpMatrixCache;
	}

	public String getConfsFolder() {
		return confsFolder;
	}

	public String getLinkProcessorCache() {
		return linkProcessorCache;
	}
	
	public String getWorkspacesPath(){
		return workspacesFolder;
    }

	/****************/

	public boolean exists(String file) {
		return new File(file).exists();
	}

	public boolean hasTopologyCache() {
		return exists(getTopologyCache());
	}

	public boolean hasTopologyML() {
		return exists(getTopologyMLFile());
	}

	public boolean hasRepositoryML() {
		return exists(getRepositoryMLFile());
	}

	public boolean hasLayoutML() {
		return exists(getLayoutMLFile());
	}

	/****************/

	protected String name = "default";

	protected String workspaceFolder;
	
	protected String dataprismFile;

	protected String repositoryMLFile;
	protected String topologyMLFile;
	protected String layoutMLFile;
	protected String mapBinFile;
	
	protected String repositoryCache;
	protected String workspaceCache;
	protected String topologyCache;
	protected String visibilityCache;
	protected String linkProcessorCache;
	protected String graphCache;
	protected String graphMLCache;
	protected String renamerCache;
	protected String hsrpMatrixCache;
	protected String confsFolder;

}
