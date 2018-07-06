/**
 * 
 */
package rsbudget.login;

import java.io.File;
import java.net.URL;
import java.util.Locale;
import java.util.prefs.BackingStoreException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.translation.TranslationService;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.LoggerFactory;

import rs.baselib.prefs.IPreferences;
import rs.baselib.prefs.IPreferencesService;
import rs.baselib.prefs.PreferencesService;
import rs.baselib.util.CommonUtils;
import rs.e4.E4Utils;
import rs.e4.splash.ISplashFormHandler;
import rs.e4.splash.PasswordSplashHandler;
import rs.e4.swt.action.CocoaE4Handler;
import rs.e4.swt.action.CocoaUIEnhancer;
import rs.e4.util.DataUtils;
import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.util.DbConfigLocator;
import rsbudget.handlers.AboutHandler;
import rsbudget.handlers.ExitHandler;
import rsbudget.preferences.PreferencesHandler;
import rsbudget.preferences.PreferencesUtils;
import rsbudget.view.wizards.bootstrap.BootstrapWizard;
import rsbudget.view.wizards.bootstrap.BootstrapWizardLanguage;
import rsbudget.view.wizards.bootstrap.LanguageDialog;

/**
 * Handles the login.
 * @author ralph
 *
 */
public class LifecycleHandler {

	private LocalResourceManager resourceManager;

	@PostContextCreate
	public void login(IEclipseContext context) {
		IEclipseContext ctx = E4Utils.getTopContext();
		Locale locale = PreferencesUtils.getLocale();
		Locale.setDefault(locale);
		ctx.set(TranslationService.LOCALE, locale);
		context.set(TranslationService.LOCALE, locale);
		cleanupUserXmi();

		Display display = PlatformUI.createDisplay();
		File dbConfigFile = DbConfigLocator.getUserDbConfigFile();
		boolean isReady = dbConfigFile.exists();
		if (!isReady) {
			// Ask for language
			LanguageDialog dlg = new LanguageDialog(display.getActiveShell());
			if (dlg.open() == IDialogConstants.CANCEL_ID) {
				System.exit(0);
				return;
			}
			BootstrapWizardLanguage.setLanguage(dlg.getValue());
			try {
				Plugin.getUserPreferences().get("languages/userInterface", BootstrapWizardLanguage.getLanguageKey());
			} catch (BackingStoreException e) {
				e.printStackTrace(); // Warn, its not important
			}
			// Run the setup wizard

			BootstrapWizard wizard = new BootstrapWizard();
			WizardDialog dialog = new WizardDialog(display.getActiveShell(), wizard);
			int rc = dialog.open();
			if (rc == IDialogConstants.CANCEL_ID) {
				System.exit(0);
				return;
			}
		} else {		
			// Ask for the password now
			ISplashFormHandler formHandler = new RsBudgetLoginFormHandler();
			PasswordSplashHandler handler = new RsBudgetPasswordSplashHandler(formHandler);
			Shell shell = new Shell(SWT.NO_TRIM|SWT.ON_TOP);
			shell.setBackgroundMode(SWT.INHERIT_DEFAULT);

			resourceManager = new LocalResourceManager(JFaceResources.getResources(), shell);


			Image image = getImage("resources/images/splash.jpg");
			shell.setSize(image.getBounds().width, image.getBounds().height);
			shell.setBackgroundImage(image);

			// Show the splash
			handler.init(shell);
			shell.setVisible(false);

		}

		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		DataUtils.register(factory, ctx);
		ctx.set(IPreferencesService.class, PreferencesService.INSTANCE);
		try {
			ctx.set(IPreferences.class, Plugin.getUserPreferences());
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}

		if (CommonUtils.isMac()) {
			try {
				CocoaUIEnhancer enhancer = new CocoaUIEnhancer(Plugin.APPLICATION_NAME);
				CocoaE4Handler exitHandler = new CocoaE4Handler(ExitHandler.class, Plugin.translate("e4xmi.menuitem.exit.label"), ctx);
				CocoaE4Handler aboutHandler = new CocoaE4Handler(AboutHandler.class, Plugin.translate("e4xmi.menu.help.about"), ctx);
				CocoaE4Handler settingsHandler = new CocoaE4Handler(PreferencesHandler.class, Plugin.translate("e4xmi.menuitem.preferences.label"), ctx);
				enhancer.hookApplicationMenu( display, exitHandler, aboutHandler, settingsHandler);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	/**
	 * Loads the image.
	 * @param path
	 * @return
	 */
	protected Image getImage(String path) {
		Bundle bundle = FrameworkUtil.getBundle(getClass());
		URL url = FileLocator.find(bundle, new Path(path), null);
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
		return resourceManager.createImage(descriptor);

	}

	protected void cleanupUserXmi() {
		try {
			IPreferences prefs = Plugin.getUserPreferences();
			if (prefs.getBoolean("resetApplication", false)) {
				File f = new File(PreferencesService.INSTANCE.getUserPreferencesHome(Plugin.APPLICATION_KEY), "workspace/.metadata/.plugins/org.eclipse.e4.workbench/workbench.xmi");
				if (f.exists()) {
					f.delete();
				}
				prefs.putBoolean("resetApplication", false);
				prefs.flush();
			}
		} catch (BackingStoreException e) {
			// Ignore this
		} catch (Throwable t) {
			LoggerFactory.getLogger(getClass()).warn("Cannot reset application", t);
		}
	}
	
}
