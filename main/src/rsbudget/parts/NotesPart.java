/**
 * 
 */
package rsbudget.parts;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rsbudget.Plugin;
import rsbudget.data.RsBudgetModelService;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Plan;
import rsbudget.parts.transactions.TransactionsPart;
import rsbudget.statusbar.StatusEntry;

/**
 * The notes for a plan.
 * @author ralph
 *
 */
public class NotesPart {

	private static Logger log = LoggerFactory.getLogger(NotesPart.class);
	
	private FormToolkit toolkit;

	private Text text;
	private volatile Plan plan;
	
	private volatile boolean doSaving = true;
	private volatile long lastModification = System.currentTimeMillis();
	private volatile boolean hasChanged = false;
	private Thread savingThread = new Thread() {
		public void run() {
			runSaving();
		}
	};
	
	@Inject
	private RsBudgetDaoFactory factory;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	private	UISynchronize uiSynchronize;
	
	@Inject
	private MPart part;
	
	/**
	 * Constructor.
	 */
	public NotesPart() {
		RsBudgetDaoFactory factory = RsBudgetModelService.INSTANCE.getFactory();
		factory.begin();
		setPlan(factory.getPlanDAO().findCurrent());
		factory.commit();
	}

	/**
	 * Creates the controls.
	 */
	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService) {
		toolkit = new FormToolkit(parent.getDisplay());
		
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		toolkit.paintBordersFor(container);
		container.setLayout(new GridLayout(1, false));
		
		text = toolkit.createText(container, "", SWT.MULTI|SWT.WRAP);
		text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		text.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				handleModifyText(e);
			}
		});
		
		if (plan != null) setPlan(plan);
		savingThread.start();
	}
	
	/**
	 * Sets the focus to the table widget.
	 * (Required by E4)
	 */
	@Focus
	protected void setFocus() {
		text.setFocus();
	}
	
	public synchronized void handleModifyText(ModifyEvent e) {
		hasChanged = true;
		setTitle();
		lastModification = System.currentTimeMillis();
	}
	
	public void runSaving() {
		while (isSaving()) {
			if (isChanged()) { 
				if (System.currentTimeMillis() - getLastModification() > 2000L) {
					doSave();
				} else {
				}
			}
			try {
				Thread.sleep(500);
			} catch (Exception e) {}
		}
	}
		
	protected synchronized long getLastModification() {
		return lastModification;
	}
	
	protected synchronized boolean isSaving() {
		return doSaving;
	}
	
	protected synchronized boolean isChanged() {
		return hasChanged;
	}
	
	public void doSave() {
		try {
			if (plan != null) {
				String s = getText();
				factory.begin();
				plan.setComment(s);
				factory.getPlanDAO().save(plan);
				factory.commit();
				hasChanged = false;
				setTitle();
			}
		} catch (Exception e) {
			log.error("Cannot save notes", e);
			setStatus(Plugin.translate("CannotSaveNotes")+e.getLocalizedMessage());
		}
	}
	
	public String getText() {
		TextGetter textGetter = new TextGetter();
		uiSynchronize.syncExec(textGetter);
		return textGetter.getText();
	}
	
	@Inject @Optional
	public synchronized void setPlan(@UIEventTopic(TransactionsPart.TOPIC_SELECTED_PLAN) Plan plan) {
		if (text != null) {
			this.plan = plan;
			String s = plan != null ? plan.getComment() : "";
			if (s == null) s = "";
			text.setText(s);
			hasChanged = false;
			setTitle();
			lastModification = System.currentTimeMillis();
			text.setEnabled(plan != null);
		} else {
			this.plan = plan;
			hasChanged = false;
		}
	}
	
	/**
	 * Disposes everything.
	 */
	@PreDestroy
	public void dispose() {
		stopSaving();
		toolkit.dispose();
	}
	
	/**
	 * Stops the saving process.
	 */
	protected synchronized void stopSaving() {
		doSaving = false;
	}
	
	/**
	 * Sets the status message.
	 * @param s the message to be set.
	 */
	protected void setStatus(String s) {
		if (eventBroker != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("STATUS", s);
			eventBroker.post(StatusEntry.TOPIC, map);
		}
	}
	
	protected void setTitle() {
		part.setDirty(hasChanged);
	}
	
	protected class TextGetter implements Runnable {
		private String text;
		
		public void run() {
			text = NotesPart.this.text.getText();
		}
		
		public String getText() {
			return text;
		}
	}
}
