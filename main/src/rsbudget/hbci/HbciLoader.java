/**
 * 
 */
package rsbudget.hbci;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.time.DateUtils;
import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.HBCIVersion;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.prefs.PreferencesService;
import rs.baselib.util.CommonUtils;
import rs.baselib.util.RsMonth;
import rsbudget.data.api.RsBudgetDaoFactory;

/**
 * Performs the actual HBCI requests.
 * @author ralph
 *
 */
public class HbciLoader implements Runnable {

	private static Logger log = LoggerFactory.getLogger(HbciLoader.class);
	protected Properties accountProperties;
	protected RsMonth month = new RsMonth();
	protected List<HbciTransaction> transactions;
	
	/**
	 * Constructor.
	 */
	public HbciLoader() {
	}

	
	/**
	 * Returns the month.
	 * @return the month
	 */
	public RsMonth getMonth() {
		return month;
	}


	/**
	 * Sets the month.
	 * @param month the month to set
	 */
	public void setMonth(RsMonth month) {
		this.month = month;
	}


	@Override
	public void run() {
		File parentDir = PreferencesService.INSTANCE.getUserPreferencesHome(RsBudgetDaoFactory.APPLICATION_KEY);
		transactions = new ArrayList<>();
		try {
			File file      = new File(parentDir, "hbci-account.properties");
			accountProperties = new Properties();
			accountProperties.load(new FileInputStream(file));
			Properties props = new Properties();
			file      = new File(parentDir, "hbci.properties");
			props.load(new FileInputStream(file));
			MyCallback callback = new MyCallback();
			HBCIUtils.initThread(props, callback);
			HBCIUtils.setParam("log.loglevel.default", "3");
			HBCIUtils.setParam("log.filter","2");
			HBCIUtils.setParam("client.passport.default", "PinTan");
			HBCIUtils.setParam("client.passport.PinTan.filename", parentDir.getAbsolutePath()+"/pintan.txt");
			Konto konto = new Konto(accountProperties.getProperty("BLZ"), accountProperties.getProperty("NUMBER"));

			HBCIPassport passport = AbstractHBCIPassport.getInstance();
			passport.fillAccountInfo(konto);
			HBCIVersion version = HBCIVersion.HBCI_300;

			HBCIHandler hbciHandle =new HBCIHandler(version.getId(), passport);
			HBCIJob job = hbciHandle.newJob("KUmsAll"); // Umsätze
			// Kontostand ist: SaldoReq	
			job.setParam("my", konto);
			job.setParam("startdate", new Date(getMonth().getBegin().getTimeInMillis()-3*DateUtils.MILLIS_PER_DAY)); // 21.5.2018 (inclusive)
			if (getMonth().getEnd().getTimeInMillis()+3*DateUtils.MILLIS_PER_DAY < System.currentTimeMillis()) {
				job.setParam("enddate",   new Date(getMonth().getEnd().getTimeInMillis()+3*DateUtils.MILLIS_PER_DAY)); // 21.6.2018 (inclusive!)
			}
			job.addToQueue();
			HBCIExecStatus status = hbciHandle.execute();
			GVRKUms result = (GVRKUms)job.getJobResult();
			if (status.isOK()) {
				List<?> lines=result.getFlatData();
				for (Iterator<?> j=lines.iterator(); j.hasNext(); ) { // alle Umsatzeinträge durchlaufen
					GVRKUms.UmsLine entry=(GVRKUms.UmsLine)j.next();
					HbciTransaction tx = new HbciTransaction(entry);
					transactions.add(tx);
					tx.debug(log);
				}

			}
			log.info(callback.getCalls()+ "calls");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		new File(parentDir, "pintan.txt").delete();
	}

	public List<HbciTransaction> getTransactions() {
		return transactions;
	}
	
	public class MyCallback extends AbstractHBCICallback {

		private int callsExpected = 153; // without status=18;
		private int calls = 0;
		
		
		/**
		 * Returns the calls.
		 * @return the calls
		 */
		public int getCalls() {
			return calls;
		}

		/**
		 * 
		 * {@inheritDoc}
		 */
		public void callback(HBCIPassport passport, int type, String displayMessage, int dataType, StringBuffer result) {
			CallbackNeed needs[] = CallbackNeed.values();
			if (type < needs.length) {
				boolean processed = false;
				CallbackNeed need = needs[type];
				String needName = need.name();
				String argResult = result.toString();
				if (needName.startsWith("NEED_")) {
					needName = needName.substring(5);
					String needValue = accountProperties.getProperty(needName);
					if (needValue != null) {
						result.replace(0,result.length(),needValue);
						processed = true;
					}
				}
				if (!processed) {
					// React here to other callbacks
					log.info("callback="+passport+","+need.name()+","+displayMessage+","+dataType+","+argResult+" ==> UNPROCESSED");
				} else {
					log.info("callback="+passport+","+need.name()+","+displayMessage+","+dataType+","+argResult+" => "+result);
				}
			} else {
				log.info("callback="+passport+","+type+","+displayMessage+","+dataType+","+result+" ==> UNPROCESSED");
			}
			calls++;
			log.info("Step "+calls+"/"+callsExpected);
		}

		/**
		 * {@inheritDoc}
		 */
		public void log(String arg0, int arg1, Date arg2, StackTraceElement arg3) {
			log.info("log="+arg0+","+arg2+","+arg3);
		}

		/**
		 * {@inheritDoc}
		 */
		public void status(HBCIPassport passport, int type, Object[] args) {
			CallbackStatus statusValues[] = CallbackStatus.values();
			//log.info("status="+arg0+","+arg1+","+arg2);
			calls++;
			CallbackStatus status = null;
			if (type < statusValues.length) status = statusValues[type];
			String typeS = status != null ? status.name() : ""+type;
			if (args == null) {
				log.info("status="+passport+","+typeS+",NULL");
			} else {
				log.info("status="+passport+","+typeS+","+CommonUtils.join(";", args));
			}
			log.info("Step "+calls+"/"+callsExpected);
		}

	}

}
