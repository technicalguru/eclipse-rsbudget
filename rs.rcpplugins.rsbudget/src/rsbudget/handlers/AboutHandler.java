
package rsbudget.handlers;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProduct;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.ProductProperties;
import org.osgi.framework.Version;

import rs.baselib.crypto.EncryptionUtils;
import rs.baselib.io.FileFinder;
import rs.e4.about.RsAboutDialog;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.dao.SettingDAO;

/**
 * Handles the about command by opening the dialog.
 * @author ralph
 *
 */
public class AboutHandler {

	@Inject
	private RsBudgetDaoFactory factory;

	@Inject
	private SettingDAO settingDao;

	@Execute
	public void execute(Shell shell) {
		RsAboutDialog dlg = new RsAboutDialog(shell);
		dlg.setTitleImage(ImageDescriptor.createFromURL(FileFinder.find("resources/images/logo.jpg")));
		dlg.setAboutText(getAboutText());

		dlg.open();
	}

	protected String getAboutText() {
		String applicationKey;

		try {
			factory.begin();
			applicationKey = settingDao.getApplicationKey();
			factory.commit();
		} finally {
			try {
				factory.rollback();
			} catch (Exception e) {}
		}
		
		IProduct product = Platform.getProduct();
		String s = ProductProperties.getAboutText(product);
		s += "\n\nVersion: "+getVersionString(product.getDefiningBundle().getVersion());
		s += "\nBuild Id: 201311151251";
		s += "\n\n(C) Copyright by Ralph Schuster, published under GPL3";
		s += "\nVisit http://techblog.ralph-schuster.eu/rsbudget";
		s += "\n\n\nInstallation Key:\n"+EncryptionUtils.encodeBase64(applicationKey)+"\n";
		return s;
	}

	protected String getVersionString(Version version) {
		return version.getMajor()+"."+version.getMinor()+"."+version.getMicro();
	}

}