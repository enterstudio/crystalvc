package crystal.client;

import java.util.Collection;
import java.util.Hashtable;

import crystal.model.DataSource;

/**
 * Stores the preferences for a specific project.
 * 
 * @author rtholmes
 * 
 */
public class ProjectPreferences {

	/**
	 * Shortname -> data source map.
	 */
	private Hashtable<String, DataSource> _dataSources;

	/**
	 * The developer's environment; this corresponds to their own repository.
	 */
	private DataSource _myEnvironment = null;

	/**
	 * The temporary directory that should be used as scratch space to compute analysis results.
	 */
	private String _tempDirectory = null;

	/**
	 * Constructor.
	 * 
	 * @param myEnvironment
	 * @param tempDirectory
	 */
	public ProjectPreferences(DataSource myEnvironment, String tempDirectory) {
		_myEnvironment = myEnvironment;
		_tempDirectory = tempDirectory;
		_dataSources = new Hashtable<String, DataSource>();
	}

	/**
	 * Adds a new data source.
	 * 
	 * @param source
	 *            The source to add; the short name must be unique or an assertion will fail.
	 */
	public void addDataSource(DataSource source) {
		assert !_dataSources.containsKey(source.getShortName());

		_dataSources.put(source.getShortName(), source);
	}

	/**
	 * Returns the data sources.
	 * 
	 * @return
	 */
	public Collection<DataSource> getDataSources() {
		return _dataSources.values();
	}

	/**
	 * Returns the data source corresponding to the developer's environment.
	 * 
	 * @return
	 */
	public DataSource getEnvironment() {
		return _myEnvironment;
	}

	/**
	 * Returns the temp directory (scratch space).
	 * 
	 * @return
	 */
	public String getTempDirectory() {
		return _tempDirectory;
	}

	/**
	 * Updates the temp directory.
	 * 
	 * @param name
	 */
	void setTempDirectory(String name) {
		_tempDirectory = name;
	}

	/**
	 * Returns a specific data source
	 * 
	 * @param shortName
	 *            The shortName corresponding to the desired datasource.
	 * @return
	 */
	public DataSource getDataSource(String shortName) {
		return _dataSources.get(shortName);
	}

	/**
	 * Removes a data source.
	 * 
	 * @param sourceToRemove
	 */
	public void removeDataSource(DataSource sourceToRemove) {
		_dataSources.remove(sourceToRemove.getShortName());

	}
}
