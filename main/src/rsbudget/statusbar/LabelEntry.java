/**
 * 
 */
package rsbudget.statusbar;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

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
public class LabelEntry {

	private CLabel label;
	
	@Inject
	EModelService modelService;

	/**
	 * Constructor.
	 */
	public LabelEntry() {
	}

	@PostConstruct
	void createWidget(Composite parent, MToolControl toolControl) {
		label = new CLabel(parent, SWT.NONE);
		String s = toolControl.getPersistedState().get("text");
		if (s == null) s = "";
		if (s.startsWith("%")) s = Plugin.translate(s);
		label.setText(s);
	}
	
	@PreDestroy
	void destroy() {
	}

}
