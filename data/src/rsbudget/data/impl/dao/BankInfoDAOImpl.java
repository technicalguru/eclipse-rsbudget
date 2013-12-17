/**
 * 
 */
package rsbudget.data.impl.dao;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import rs.baselib.util.DefaultComparator;
import rs.data.util.DaoCollectionIterator;
import rs.data.util.IDaoIterator;
import rsbudget.data.api.bo.BankInfo;
import rsbudget.data.api.dao.BankInfoDAO;
import rsbudget.data.impl.bo.BankInfoImpl;
import rsbudget.data.type.BankInfoId;

/**
 * Implementation for bank information.
 * @author ralph
 *
 */
public class BankInfoDAOImpl extends AbstractRsBudgetFileDAO<BankInfoId, BankInfo> implements BankInfoDAO {

	public static final String BLZ_PROPERTIES = "blz.properties";

	/** All objects listed with their Key */
	private Map<BankInfoId, BankInfo> objects = new HashMap<>();
	/** All objects listed with their Key */
	private Map<String, BankInfo> objectsByBlz = new HashMap<>();
	/** objects in natural order */
	private List<BankInfo> objectsSorted = new ArrayList<>();

	/**
	 * Constructor.
	 */
	public BankInfoDAOImpl() {
		load();
	}

	/**
	 * Loads the objects for the DAO.
	 */
	protected void load() {
		Collection<BankInfo> l = loadObjects();
		for (BankInfo o : l) {
			add(o);
		}
	}
	
	/**
	 * Loads the bank information without the DAO interference.
	 * @return sorted list of business objects.
	 */
	public static List<BankInfo> loadObjects() {
		List<BankInfo> rc = new ArrayList<>();
		InputStream in = null;
		try {
			URL url = FileFinder.find(BLZ_PROPERTIES);
			in = url.openStream(); 
			Properties p = new Properties();
			p.load(in);
			for (Object key : p.keySet()) {
				if (((String)key).trim().length() == 0) continue;
				String value = p.getProperty((String)key);
				if (value.trim().length() == 0) continue;
				String a[] = value.split("\\|");
				String s1 = a.length > 0 ? a[0] : null;
				String s2 = a.length > 1 ? a[1] : null;
				String s3 = a.length > 2 ? a[2] : null;
				String s4 = a.length > 3 ? a[3] : null;
				String s5 = a.length > 4 ? a[4] : null;
				String s6 = a.length > 5 ? a[5] : null;
				String s7 = a.length > 6 ? a[6] : null;
				String s8 = a.length > 7 ? a[7] : null;
				BankInfo object = new BankInfoImpl(url, (String)key, s1, s2, s3, s4, s5, s6, s7, s8);
				rc.add(object);
				//   s1       s2      s3       s4      s5                   s6                          s7  s8
				// Postbank|Berlin|PBNKDEFF100|24|10.10.10.255|https://hbci.postbank.de/banking/hbci.do|220|plus|
			}
			Collections.sort(rc, DefaultComparator.INSTANCE);
		} catch (Exception e) {
			LoggerFactory.getLogger(BankInfoDAOImpl.class).error("Cannot load BLZ information:", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {}
			}
		}
		return rc;
	}
	
	/**
	 * Adds the given information to our list.
	 * @param info info to add
	 */
	protected void add(BankInfo info) {
		objects.put(info.getId(), info);
		objectsByBlz.put(info.getBlz(), info);
		objectsSorted.add(info);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getObjectCount() {
		return objects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getDefaultObjectCount() {
		return objects.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankInfo findBy(BankInfoId id) {
		return objects.get(id);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BankInfo> findAll(int firstResult, int maxResults) {
		if (firstResult < 0) firstResult = 0;
		if (maxResults < 0) maxResults = objectsSorted.size();
		firstResult = Math.min(firstResult, objectsSorted.size());
		maxResults = Math.min(maxResults, objectsSorted.size()-firstResult);
		return objectsSorted.subList(firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<BankInfo> findDefaultAll(int firstResult, int maxResults) {
		if (firstResult < 0) firstResult = 0;
		if (maxResults < 0) maxResults = objectsSorted.size();
		firstResult = Math.min(firstResult, objectsSorted.size());
		maxResults = Math.min(maxResults, objectsSorted.size()-firstResult);
		return objectsSorted.subList(firstResult, maxResults);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<BankInfo> iterateAll(int firstResult, int maxResults) {
		if (firstResult < 0) firstResult = 0;
		if (maxResults < 0) maxResults = objectsSorted.size();
		firstResult = Math.min(firstResult, objectsSorted.size());
		maxResults = Math.min(maxResults, objectsSorted.size()-firstResult);
		return new DaoCollectionIterator<BankInfo>(objectsSorted.subList(firstResult, maxResults));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public IDaoIterator<BankInfo> iterateDefaultAll(int firstResult,	int maxResults) {
		if (firstResult < 0) firstResult = 0;
		if (maxResults < 0) maxResults = objectsSorted.size();
		firstResult = Math.min(firstResult, objectsSorted.size());
		maxResults = Math.min(maxResults, objectsSorted.size()-firstResult);
		return new DaoCollectionIterator<BankInfo>(objectsSorted.subList(firstResult, maxResults));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteAll() {
		// We do not support it
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int deleteDefaultAll() {
		// We do not support it
		return 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void refresh(BankInfo arg0) {
		// Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _create(BankInfo object) {
		// Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _delete(BankInfo object) {
		// Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void _save(BankInfo object) {
		// Not supported
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankInfo newInstance() {
		// We do not support it
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void create(BankInfo object, boolean setCreationDate) {
		// We do not support it
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void save(BankInfo object, boolean setChangeDate) {
		// We do not support it
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(BankInfo object) {
		// We do not support it
	}

	/*************************** SPECIAL BANK INFO HANDLING ***************************/
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BankInfo findBy(String blz) {
		return objectsByBlz.get(blz);
	}

	
}
