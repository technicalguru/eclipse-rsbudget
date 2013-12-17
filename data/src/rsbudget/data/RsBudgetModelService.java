/**
 * 
 */
package rsbudget.data;

import org.apache.commons.configuration.XMLConfiguration;
import org.eclipse.core.runtime.IProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.data.api.IOsgiModelService;
import rs.data.impl.OsgiModelServiceImpl;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * Singleton service for the model.
 * @author ralph
 *
 */
public class RsBudgetModelService {

	public static final int TOTAL_LOAD_PROGRESS_TASK_COUNT = 6;
	
	private static Logger log = LoggerFactory.getLogger(RsBudgetModelService.class);
	
	public static RsBudgetModelService INSTANCE = new RsBudgetModelService();
	
	private RsBudgetDaoFactory factory;
	
	/**
	 * Returns the factory.
	 * @return
	 */
	public RsBudgetDaoFactory getFactory() {
		return getFactory(null, 0);
	}

	/**
	 * Returns the factory and tries to load it with the progress monitor if required.
	 * @param monitor
	 * @param progressOffset
	 * @return
	 */
	public RsBudgetDaoFactory getFactory(IProgressMonitor monitor, int progressOffset) {
		if (factory == null) loadFactory(monitor, progressOffset);
		else if (monitor != null) monitor.worked(TOTAL_LOAD_PROGRESS_TASK_COUNT+progressOffset);
		return factory;
	}
	
	/**
	 * Loads the factory with the progress monitor.
	 * @param monitor
	 * @param progressOffset
	 */
	protected synchronized void loadFactory(IProgressMonitor monitor, int progressOffset) {
		if (factory != null) {
			if (monitor != null) monitor.worked(TOTAL_LOAD_PROGRESS_TASK_COUNT+progressOffset);
			return;
		}
		
		try {
			if (monitor != null) monitor.worked(1+progressOffset);
			
			IOsgiModelService modelService = OsgiModelServiceImpl.getModelService();
			if (monitor != null) monitor.worked(2+progressOffset);
			
			XMLConfiguration pluginConfig = RsBudgetConfigurationService.getConfiguration();
			if (monitor != null) monitor.worked(3+progressOffset);
			
			modelService.setConfiguration(pluginConfig);
			if (monitor != null) monitor.worked(4+progressOffset);
			
			// Load the DAO factory
			
			factory = (RsBudgetDaoFactory)modelService.getFactory(IOsgiModelService.DEFAULT_NAME);
			if (monitor != null) monitor.worked(5+progressOffset);
		} catch (Exception e) {
			log.error("Cannot create application", e);
		}
		if (monitor != null) monitor.worked(TOTAL_LOAD_PROGRESS_TASK_COUNT+progressOffset);
	}

}
