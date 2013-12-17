/**
 * 
 */
package rsbudget.login;

import org.eclipse.jface.resource.FontDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import rs.e4.splash.ISplashFormHandler;
import rs.e4.splash.PasswordSplashHandler;
import rsbudget.Plugin;

/**
 * The RSBudget specific splash.
 * @author ralph
 *
 */
public class RsBudgetPasswordSplashHandler extends PasswordSplashHandler {

	/**
	 * Constructor.
	 * @param formHandler
	 */
	public RsBudgetPasswordSplashHandler(ISplashFormHandler formHandler) {
		super(formHandler);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void createBranding(Composite parent) {
		Label header = new Label(parent, SWT.NONE);
		header.setText(Plugin.APPLICATION_NAME+" "+Plugin.APPLICATION_SPLASH_VERSION);
		header.setFont(getResourceManager().createFont(FontDescriptor.createFrom("Arial", 17, SWT.BOLD))); //$NON-NLS-1$
		header.setForeground(getResourceManager().createColor(new RGB(100,100,100)));
		GridData gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		header.setLayoutData(gd);
		
		Label subtitle = new Label(parent, SWT.NONE);
		subtitle.setText(Plugin.APPLICATION_SUBTITLE);
		subtitle.setFont(getResourceManager().createFont(FontDescriptor.createFrom("Arial", 10, SWT.BOLD|SWT.ITALIC))); //$NON-NLS-1$
		subtitle.setForeground(getResourceManager().createColor(new RGB(180,180,180)));
		gd = new GridData(SWT.FILL, SWT.TOP, true, false);
		gd.heightHint = 60;
		subtitle.setLayoutData(gd);
	}

	
}
