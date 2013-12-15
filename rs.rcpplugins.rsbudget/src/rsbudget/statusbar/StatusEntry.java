/**
 * 
 */
package rsbudget.statusbar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.menu.MToolControl;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;

import rsbudget.Plugin;

/**
 * Displays the status text.
 * @author ralph
 *
 */
public class StatusEntry {

	/** The Event broker topic */
	public static final String TOPIC = "STATUS_TOPIC";
	
	private CLabel label;
	
	@Inject
	EModelService modelService;

	/**
	 * Constructor.
	 */
	public StatusEntry() {
	}

	@PostConstruct
	void createWidget(Composite parent, MToolControl toolControl) {
		label = new CLabel(parent, SWT.NONE);
		label.setText(Plugin.translate("status.ready"));
	}
	
	@PreDestroy
	void destroy() {
	}

	@Inject @Optional
	public void setStatus(@UIEventTopic(TOPIC) String s) {
		if ((s == null) || (s.trim().length() == 0)) s = Plugin.translate("status.ready");
		if (!label.isDisposed()) label.setText(s);
	}
}
