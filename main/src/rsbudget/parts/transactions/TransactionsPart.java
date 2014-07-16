/**
 * 
 */
package rsbudget.parts.transactions;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.internal.contexts.EclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import rs.baselib.io.FileFinder;
import rs.baselib.util.IWrapper;
import rs.baselib.util.RsMonth;
import rs.data.api.bo.IGeneralBO;
import rs.data.event.DaoEvent;
import rs.data.event.IDaoListener;
import rs.e4.E4Utils;
import rs.e4.SwtUtils;
import rs.e4.celledit.CellEditorActivationStrategy;
import rs.e4.celledit.ComboBoxEditingSupport;
import rs.e4.celledit.FloatEditingSupport;
import rs.e4.celledit.IComboBoxEditingSupportModel;
import rs.e4.celledit.IEditingSupportModel;
import rs.e4.celledit.RsDateEditingSupport;
import rs.e4.celledit.TextEditingSupport;
import rs.e4.swt.action.ButtonHandler;
import rs.e4.util.AbstractColumnLabelProvider;
import rs.e4.util.BeanColumnLabelProvider;
import rs.e4.util.ColumnAdapter;
import rs.e4.util.DateColumnLabelProvider;
import rsbaselib.util.MonthLabelProvider;
import rsbudget.Plugin;
import rsbudget.celledit.BoSelectionSupportModel;
import rsbudget.celledit.EditingSupportModelListener;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Budget;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.Plan;
import rsbudget.data.api.bo.PlannedTransaction;
import rsbudget.data.api.bo.RsBudgetBO;
import rsbudget.data.api.bo.Transaction;
import rsbudget.parts.budgets.BudgetRowWrapper;
import rsbudget.preferences.PreferencesUtils;
import rsbudget.util.CurrencyColumnLabelProvider;
import rsbudget.util.RsBudgetColors;
import rsbudget.util.TableColumnResizeListener;


/**
 * The main transaction list.
 * @author ralph
 *
 */
public class TransactionsPart {

	public static final String TOPIC_SELECTED_PLAN = "SelectedPlan";
	public static TransactionSorter SORTER = new TransactionSorter();
	public static final SimpleDateFormat HEADER_FORMATTER = new SimpleDateFormat("MMMM yyyy");
	public static final RGB LINK_COLOR = new RGB(0,0,204);
	private FormToolkit toolkit;
	private LocalResourceManager resourceManager;

	private Label lblTitle;
	private CurrentStatusView statusView;

	private Table table;
	private TableViewer tableViewer;
	private TableColumn tblclmn1;
	private TableColumn tblclmn2;
	private TableColumn tblclmn3;
	private TableColumn tblclmn4;
	private TableColumn tblclmn5;
	private TableColumn tblclmn6;
	private TableColumn tblclmn7;

	private ImageHyperlink hyperlinkPrevYear;
	private ImageHyperlink hprlnkPrev;
	private Hyperlink hprlnkJanuary;
	private Hyperlink hprlnkFebruary;
	private Hyperlink hprlnkMarch;
	private Hyperlink hprlnkApril;
	private Hyperlink hprlnkMay;
	private Hyperlink hprlnkJune;
	private Hyperlink hprlnkJuly;
	private Hyperlink hprlnkAugust;
	private Hyperlink hprlnkSeptember;
	private Hyperlink hprlnkOctober;
	private Hyperlink hprlnkNovember;
	private Hyperlink hprlnkDecember;
	private ImageHyperlink hprlnkNext;
	private ImageHyperlink hyperlinkNextYear;
	
	private IHyperlinkListener navigationListener = new HyperlinkAdapter() {
		@Override
		public void linkActivated(HyperlinkEvent e) {
			navigateTo(e.getSource());
		}
	};

	private Plan plan;
	private RsMonth month;
	
	@Inject
	private IEclipseContext context;
	
	@Inject
	private RsBudgetDaoFactory factory;
	
	@Inject
	private ESelectionService selectionService;
	
	@Inject
	private MPart part;
	
	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	private UISynchronize uiSynchronize;

	/** The list of transactions currently displayed */
	private IObservableList transactions;
	private Map<String, IObservableList> bindings = new HashMap<String, IObservableList>();
	
	/** Listener to changes of properties in budgets */
	private IChangeListener changeListener = new IChangeListener() {
		@Override
		public void handleChange(ChangeEvent event) {
			updateInfoPart();
		}
	};
	
	/** Listener to DAO inserts/deletes */
	private IDaoListener daoListener = new IDaoListener() {
		@Override
		public void handleDaoEvent(DaoEvent event) {
			TransactionsPart.this.handleDaoEvent(event);
		}
	};
	
	/**
	 * Creates the controls.
	 */
	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService) {
		toolkit = new FormToolkit(parent.getDisplay());
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);
		
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		toolkit.paintBordersFor(container);
		container.setLayout(new GridLayout(1, false));
		
		lblTitle = toolkit.createLabel(container, "Dezember 2011", SWT.NONE);
		lblTitle.setFont(resourceManager.createFont(SwtUtils.deriveBoldFont(SwtUtils.deriveFont(lblTitle.getFont(), 12))));
		lblTitle.setAlignment(SWT.CENTER);
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		createInfoSection(container);
		createTableSection(container, menuService);
		
		try {
			factory.begin();
			Plan plan = factory.getPlanDAO().findCurrent();
			if (plan != null) setModel(plan);
			else setModel(new RsMonth());
			factory.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}
	
	public void setModel(Plan plan) {
		this.plan = plan;
		if (plan != null) month = plan.getMonth();
		
		// Status view
		statusView.setModel(plan);

		// Table
		unbind(); // Avoid too much updates
		transactions.clear();
		try {
			factory.begin();
			List<TxRowWrapper> rc = new ArrayList<TxRowWrapper>();

			Set<Budget> budgets = plan.getBudgets();
			if (budgets != null) {
				for (Budget budget : budgets) {
					rc.add(new TxRowWrapper(budget));
				}
			}

			Set<RsBudgetBO<?>> txList = plan.getMixedTransactions();
			if (txList != null) {
				for (RsBudgetBO<?> tx : txList) {
					if (tx instanceof Transaction) rc.add(new TxRowWrapper((Transaction)tx));
					else if (tx instanceof PlannedTransaction) rc.add(new TxRowWrapper((PlannedTransaction)tx));
					else System.out.println("WHAT? "+tx);
				}
			}
			Collections.sort(rc, SORTER);
			for (TxRowWrapper wrapper : rc) transactions.add(wrapper);
		} finally {
			factory.commit();
		}

		bind(); // Re-enable updates
		updateInfoPart();
		eventBroker.post(TOPIC_SELECTED_PLAN, plan);
	}
	
	public void setModel(RsMonth month) {
		this.plan = null;
		this.month = month;
		
		// Status view
		statusView.setModel(month);

		// Table
		transactions.clear();
		
		updateInfoPart();
		eventBroker.post(TOPIC_SELECTED_PLAN, null);
	}

	protected void createInfoSection(Composite parent) {
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		toolkit.paintBordersFor(container);
		container.setLayout(new GridLayout(1, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		createNavigationSection(container);
		createValuesSection(container);
	}
	
	protected void createNavigationSection(Composite parent) {
		Composite container = toolkit.createComposite(parent, SWT.NONE);
		container.setLayout(new GridLayout(16, false));
		container.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.paintBordersFor(container);

		hyperlinkPrevYear = createImageHyperlink(container, "resources/icons/go-first.png");
		hprlnkPrev = createImageHyperlink(container, "resources/icons/go-previous.png");
		hprlnkJanuary = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.JANUARY));
		hprlnkFebruary = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.FEBRUARY));
		hprlnkMarch = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.MARCH));
		hprlnkApril = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.APRIL));
		hprlnkMay = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.MAY));
		hprlnkJune = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.JUNE));
		hprlnkJuly = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.JULY));
		hprlnkAugust = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.AUGUST));
		hprlnkSeptember = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.SEPTEMBER));
		hprlnkOctober = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.OCTOBER));
		hprlnkNovember = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.NOVEMBER));
		hprlnkDecember = createHyperlink(container, MonthLabelProvider.INSTANCE.getText(Calendar.DECEMBER));
		hprlnkNext = createImageHyperlink(container, "resources/icons/go-next.png");
		hyperlinkNextYear = createImageHyperlink(container, "resources/icons/go-last.png");
	}
	
	/**
	 * Creates an image hyperlink.
	 * @param parent
	 * @param imagePath
	 * @return
	 */
	protected ImageHyperlink createImageHyperlink(Composite parent, String imagePath) {
		ImageHyperlink rc = toolkit.createImageHyperlink(parent, SWT.NONE);
		rc.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		toolkit.paintBordersFor(rc);
		rc.setImage(createImage(imagePath));
		rc.addHyperlinkListener(navigationListener);
		return rc;
	}
	
	/**
	 * Creates the image from the path.
	 * @param path
	 * @return
	 */
	protected Image createImage(String path) {
		Bundle bundle = FrameworkUtil.getBundle(getClass()); 
		URL url = FileLocator.find(bundle, new Path(path), null);
		ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
		return resourceManager.createImage(descriptor);
	}
	
	/**
	 * Creates an hyperlink.
	 * @param parent
	 * @param text
	 * @return
	 */
	protected Hyperlink createHyperlink(Composite parent, String text) {
		Hyperlink rc = toolkit.createHyperlink(parent, text, SWT.NONE);
		rc.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		toolkit.paintBordersFor(rc);
		rc.addHyperlinkListener(navigationListener);
		rc.setForeground(resourceManager.createColor(LINK_COLOR));
		return rc;
	}
	
	/**
	 * Creates the section with the status summaries.
	 * @param parent
	 */
	protected void createValuesSection(Composite parent) {
		statusView = new CurrentStatusView(parent, resourceManager, SWT.NONE);
		ContextInjectionFactory.inject(statusView, context);
		statusView.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		toolkit.paintBordersFor(statusView);
	}

	/**
	 * Creates the table.
	 * @param parent
	 */
	protected void createTableSection(Composite parent, EMenuService menuService) {
		Composite tableButtonsParent = toolkit.createComposite(parent, SWT.NONE);
		createTableButtons(tableButtonsParent);
		tableButtonsParent.setLayoutData(new GridData(SWT.LEFT, SWT.BOTTOM, true, false, 1, 1));
		
		// Create the table viewer for our table
		table = toolkit.createTable(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		TableLayout tableLayout = new TableLayout();
		table.setLayout(tableLayout);
		tableViewer = new TableViewer(table);
		TableViewerEditor.create(
				tableViewer, 
				new CellEditorActivationStrategy(tableViewer), 
				ColumnViewerEditor.TABBING_HORIZONTAL 
				| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
				| ColumnViewerEditor.TABBING_VERTICAL
				| ColumnViewerEditor.KEYBOARD_ACTIVATION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		toolkit.paintBordersFor(table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		{
			TxBackgroundProvider backgroundProvider = new TxBackgroundProvider();
			EditingSupportModelListener editListener = new EditingSupportModelListener();
			
			TableViewerColumn column1 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn1 = column1.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("transactions", "date", 80, 10, true));
			tblclmn1.setText(Plugin.translate("%part.transactions.column.date.title"));
			AbstractColumnLabelProvider lp = new DateColumnLabelProvider("valueDate", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column1.setLabelProvider(lp);
			column1.getColumn().addControlListener(new TableColumnResizeListener("transactions", "date"));
			IEditingSupportModel dateModel = new TransactionSupportModel("valueDate");
			dateModel.addEditingSupportModelListener(editListener);
			column1.setEditingSupport(new RsDateEditingSupport(tableViewer, dateModel, false));
							
			TableViewerColumn column2 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn2 = column2.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("transactions", "text", 350, 10, true));
			tblclmn2.setText(Plugin.translate("%part.transactions.column.text.title"));
			lp = new BeanColumnLabelProvider("text", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column2.setLabelProvider(lp);
			column2.getColumn().addControlListener(new TableColumnResizeListener("transactions", "text"));
			IEditingSupportModel nameModel = new TransactionSupportModel("text");
			nameModel.addEditingSupportModelListener(editListener);
			column2.setEditingSupport(new TextEditingSupport(tableViewer, nameModel, false));
			
			TableViewerColumn column3 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn3 = column3.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("transactions", "details", 350, 10, true));
			tblclmn3.setText(Plugin.translate("%part.transactions.column.details.title"));
			lp = new BeanColumnLabelProvider("details", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column3.setLabelProvider(lp);
			column3.getColumn().addControlListener(new TableColumnResizeListener("transactions", "details"));
			IEditingSupportModel detailsModel = new TransactionSupportModel("details");
			detailsModel.addEditingSupportModelListener(editListener);
			column3.setEditingSupport(new TextEditingSupport(tableViewer, detailsModel, false));
			
			TableViewerColumn column4 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn4 = column4.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("transactions", "budget", 100, 10, true));
			tblclmn4.setText(Plugin.translate("%part.transactions.column.budget.title"));
			lp = new BeanColumnLabelProvider("txBudget.name", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column4.setLabelProvider(lp);
			column4.getColumn().addControlListener(new TableColumnResizeListener("transactions", "budget"));
			IComboBoxEditingSupportModel budgetModel = new BudgetSelectionSupportModel(factory.getBudgetDAO(), "txBudget");
			budgetModel.addEditingSupportModelListener(editListener);
			column4.setEditingSupport(new ComboBoxEditingSupport(tableViewer, budgetModel, true));
			
			TableViewerColumn column5 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn5 = column5.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("transactions", "category", 100, 10, true));
			tblclmn5.setText(Plugin.translate("%part.transactions.column.category.title"));
			lp = new BeanColumnLabelProvider("category.name", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column5.setLabelProvider(lp);
			column5.getColumn().addControlListener(new TableColumnResizeListener("transactions", "category"));
			IComboBoxEditingSupportModel categoryModel = new BoSelectionSupportModel<Category>(factory.getCategoryDAO(), "category");
			categoryModel.addEditingSupportModelListener(editListener);
			column5.setEditingSupport(new ComboBoxEditingSupport(tableViewer, categoryModel, false));
			
			TableViewerColumn column6 = new TableViewerColumn(tableViewer, SWT.RIGHT);
			tblclmn6 = column6.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("transactions", "plannedAmount", 100, 10, true));
			tblclmn6.setText(Plugin.translate("%part.transactions.column.planned.title"));
			lp = new CurrencyColumnLabelProvider("plannedAmount", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column6.setLabelProvider(lp);
			column6.getColumn().addControlListener(new TableColumnResizeListener("transactions", "plannedAmount"));
			IEditingSupportModel plannedModel = new TransactionSupportModel("plannedAmount");
			plannedModel.addEditingSupportModelListener(editListener);
			column6.setEditingSupport(new FloatEditingSupport(tableViewer, plannedModel, false));
			
			TableViewerColumn column7 = new TableViewerColumn(tableViewer, SWT.RIGHT);
			tblclmn7 = column7.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("transactions", "actualAmount", 100, 10, true));
			tblclmn7.setText(Plugin.translate("%part.transactions.column.actual.title"));
			lp = new CurrencyColumnLabelProvider("actualAmount", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column7.setLabelProvider(lp);
			column7.getColumn().addControlListener(new TableColumnResizeListener("transactions", "actualAmount"));
			IEditingSupportModel actualModel = new TransactionSupportModel("actualAmount");
			actualModel.addEditingSupportModelListener(editListener);
			column7.setEditingSupport(new FloatEditingSupport(tableViewer, actualModel, true));
			
		}
		
		// Content provider
		tableViewer.setContentProvider(new ObservableListContentProvider());
		tableViewer.setInput(createModel());
		
		// Propagate the selection
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged(event);
			}
		});
		
		// Context menu
		menuService.registerContextMenu(table, "rs.rcpplugins.rsbudget.popupmenu.transactions");
	}
	
	/**
	 * Creates the button for the table.
	 * @param parent parent composite
	 */
	protected void createTableButtons(Composite parent) {
		toolkit.paintBordersFor(parent);
		GridLayout gl = new GridLayout(2, false);
		gl.verticalSpacing = 0;
		parent.setLayout(gl);
		
		Object newHandler = E4Utils.getHandlerFor(part, Plugin.NEW_ROW_COMMAND_ID);
		Object deleteHandler = E4Utils.getHandlerFor(part, Plugin.DELETE_ROW_COMMAND_ID);
		
		Button b1 = new Button(parent, SWT.PUSH);
		b1.setImage(resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find("resources/icons/table-insert-row.png"))));
		b1.setToolTipText(Plugin.translate("e4xmi.part.transactions.menuitem.newtx.tooltip"));
		createButtonHandler(b1, newHandler);
		Button b2 = new Button(parent, SWT.PUSH);
		b2.setImage(resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find("resources/icons/table-delete-row.png"))));
		b2.setToolTipText(Plugin.translate("e4xmi.part.transactions.menuitem.deletetx.tooltip"));
		createButtonHandler(b2, deleteHandler);
	}
	
	/**
	 * Creates a button for the handler.
	 * @param button the button
	 * @param handler the handler
	 */
	protected void createButtonHandler(Button button, Object handler) {
		IEclipseContext tempContext = new EclipseContext(null);
		tempContext.set(Button.class, button);
		tempContext.set("handler", handler);
		ContextInjectionFactory.make(ButtonHandler.class, context, tempContext);
	}
	
	/**
	 * Handles the selection change event.
	 * @param event
	 */
	protected void handleSelectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		// set the selection to the service
		selectionService.setSelection(toArray(selection.toArray()));
	}
	
	/**
	 * Transform to typed array.
	 * @param array
	 * @return
	 */
	protected TxRowWrapper[] toArray(Object array[]) {
		if (array == null) return null;
		TxRowWrapper rc[] = new TxRowWrapper[array.length];
		int i=0;
		for (Object o : array) rc[i++] = (TxRowWrapper)o;
		return rc;
	}
	
	/**
	 * Handles the create/delete notifications
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	protected void handleDaoEvent(DaoEvent event) {
		Object src = event.getSource();
		IGeneralBO<?> bo = event.getObject();
		switch (event.getType()) {
		case OBJECT_CREATED: {
			TxRowWrapper row = null;
			if (src == factory.getBudgetDAO()) row = new TxRowWrapper((Budget)bo);
			else if (src == factory.getPlannedTransactionDAO()) row = new TxRowWrapper((PlannedTransaction)bo);
			else row = new TxRowWrapper((Transaction)bo);
			if (!row.isBudget()) {
				int idx = transactions.indexOf(row);
				if (idx >= 0) {
					TxRowWrapper existingRow = (TxRowWrapper)transactions.get(idx);
					if (existingRow != row) {
						transactions.set(idx, row);
					} else {
						transactions.add(row);
					}
				} else {
					transactions.add(row);
				}
			} else {
				int index = transactions.size();
				for (int i=0; i<transactions.size(); i++) {
					if (!((TxRowWrapper)transactions.get(i)).isBudget()) {
						index = i; break;
					}
				}
				transactions.add(index, row);
			}
			break;
		}
		case OBJECT_DELETED:
			if (bo instanceof Transaction) {
				if (((Transaction)bo).getPlannedTransaction() != null) {
					// We keep the planned TX
					tableViewer.refresh();
					break; 
				}
			}
			Object found = null;
			for (Object o : transactions) {
				if (o.equals(bo)) { found = o; break; }
			}
			transactions.remove(found);
			break;
		case ALL_DEFAULT_DELETED:
		case ALL_DELETED:
			// Dunno yet what to do
			transactions.clear();
		default:
		}
	}
	
	/**
	 * Edit the given row.
	 * @param row
	 */
	public void edit(final Object row) {
		if (row instanceof TxRowWrapper) {
			uiSynchronize.asyncExec(new Runnable() {				
				@Override
				public void run() {
					tableViewer.reveal(row);
					tableViewer.editElement(row, 1);
				}
			});
		} else {
			for (Object o : transactions) {
				if (((IWrapper)o).getWrapped().equals(row)) {
					edit((TxRowWrapper)o);
					break;
				}
			}
		}
	}
	
	/**
	 * Edit the given row.
	 * @param row
	 */
	public void reveal(Object row) {
		if (row instanceof TxRowWrapper) {
			tableViewer.reveal(row);
			tableViewer.setSelection(new StructuredSelection(row));
		} else {
			for (Object o : transactions) {
				if (((IWrapper)o).getWrapped().equals(row)) {
					reveal((TxRowWrapper)o);
					break;
				}
			}
		}
	}
	
	/**
	 * Sets the focus to the table widget.
	 * (Required by E4)
	 */
	@Focus
	protected void setFocus() {
		if (table != null) table.setFocus();
	}

	/**
	 * Issue navigation action.
	 * @param hyperlink the link activating this navigation
	 */
	protected void navigateTo(Object hyperlink) {
		if      (hyperlink == hyperlinkPrevYear)  navigateToYear(month.get(Calendar.YEAR)-1);
		else if (hyperlink == hyperlinkNextYear)  navigateToYear(month.get(Calendar.YEAR)+1);
		else if (hyperlink == hprlnkJanuary)      navigateToMonth(0);
		else if (hyperlink == hprlnkFebruary)     navigateToMonth(1);
		else if (hyperlink == hprlnkMarch)        navigateToMonth(2);
		else if (hyperlink == hprlnkApril)        navigateToMonth(3);
		else if (hyperlink == hprlnkMay)          navigateToMonth(4);
		else if (hyperlink == hprlnkJune)         navigateToMonth(5);
		else if (hyperlink == hprlnkJuly)         navigateToMonth(6);
		else if (hyperlink == hprlnkAugust)       navigateToMonth(7);
		else if (hyperlink == hprlnkSeptember)    navigateToMonth(8);
		else if (hyperlink == hprlnkOctober)      navigateToMonth(9);
		else if (hyperlink == hprlnkNovember)     navigateToMonth(10);
		else if (hyperlink == hprlnkDecember)     navigateToMonth(11);
		else if (hyperlink == hprlnkNext)         navigateToNextMonth();
		else if (hyperlink == hprlnkPrev)         navigateToPreviousMonth();
		
	}
	
	/**
	 * Navigate to this year.
	 * @param year year to show
	 */
	public void navigateToYear(int year) {
		navigateToMonth(month.get(Calendar.MONTH), year);
	}
	
	/**
	 * Navigate to this month of the same year.
	 * @param month month
	 */
	public void navigateToMonth(int month) {
		navigateToMonth(month, this.month.get(Calendar.YEAR));
	}
	
	/**
	 * Navigate to next month.
	 */
	public void navigateToNextMonth() {
		int m = month.get(Calendar.MONTH);
		int y = month.get(Calendar.YEAR);
		m++;
		if (m > 11) {
			m = 0;
			y++;
		}
		navigateToMonth(m, y);
	}
	
	/**
	 * Navigate to previous month.
	 */
	public void navigateToPreviousMonth() {
		int m = month.get(Calendar.MONTH);
		int y = month.get(Calendar.YEAR);
		m--;
		if (m < 0) {
			m = 11;
			y--;
		}
		navigateToMonth(m, y);
	}
	
	
	/**
	 * Navigate to this month.
	 * @param month month
	 * @param year year
	 */
	public void navigateToMonth(int month, int year) {
		factory.begin();
		RsMonth m = new RsMonth(month, year);
		Plan plan = factory.getPlanDAO().findBy(m);
		if (plan != null) setModel(plan);
		else setModel(m);
		factory.commit();
	}
	
	/**
	 * Disposes everything.
	 */
	@PreDestroy
	public void dispose() {
		factory.getPeriodicalBudgetDAO().removeDaoListener(daoListener);
		factory.getPeriodicalTransactionDAO().removeDaoListener(daoListener);
		transactions.removeChangeListener(changeListener);
		toolkit.dispose();
		transactions.dispose();
	}
	
	/**
	 * Creates the initial model.
	 * @return
	 */
	protected IObservableList createModel() {
		if (transactions != null) return transactions;
		List<TxRowWrapper> l = new ArrayList<>();
		transactions = Properties.selfList(BudgetRowWrapper.class).observe(l);
		bind();
		
		// Listen to the DAOs for changes
		factory.getBudgetDAO().addDaoListener(daoListener);
		factory.getTransactionDAO().addDaoListener(daoListener);
		factory.getPlannedTransactionDAO().addDaoListener(daoListener);
		return transactions;
	}
	
	/**
	 * Bind our change listener.
	 */
	protected void bind() {
		transactions.addChangeListener(changeListener);
		bind(TxRowWrapper.PROPERTY_DETAILS);
		bind(TxRowWrapper.PROPERTY_TX_BUDGET);
		bind(TxRowWrapper.PROPERTY_ACTUAL_AMOUNT);
		bind(TxRowWrapper.PROPERTY_PLANNED_AMOUNT);		
	}
	
	/**
	 * Unbind our change listener.
	 */
	protected void unbind() {
		transactions.removeChangeListener(changeListener);
		unbind(TxRowWrapper.PROPERTY_DETAILS);
		unbind(TxRowWrapper.PROPERTY_TX_BUDGET);
		unbind(TxRowWrapper.PROPERTY_ACTUAL_AMOUNT);
		unbind(TxRowWrapper.PROPERTY_PLANNED_AMOUNT);		
	}
	
	/**
	 * Bind change listener to a specific column.
	 * @param property column property
	 */
	protected void bind(String property) {
		IObservableList list = BeanProperties.value(TxRowWrapper.class, property).observeDetail(transactions);
		list.addChangeListener(changeListener);
		bindings.put(property, list);
	}
	
	/**
	 * Unbind change listener from a specific column.
	 * @param property column property
	 */
	protected void unbind(String property) {
		IObservableList list = bindings.get(property);
		list.removeChangeListener(changeListener);
	}
	
	/**
	 * Update all labels.
	 */
	protected void updateInfoPart() {
		uiSynchronize.asyncExec(new Runnable() {
			public void run() {
				if (lblTitle.isDisposed()) return;

				// The title
				lblTitle.setText(HEADER_FORMATTER.format(month.getTime()));

				// Navigation
				hyperlinkPrevYear.setToolTipText(""+(month.get(Calendar.YEAR)-1));
				hyperlinkNextYear.setToolTipText(""+(month.get(Calendar.YEAR)+1));

				statusView.updateValues();
				tableViewer.refresh();
			}
		});
	}
	
	
	/**
	 * Returns the plan.
	 * @return the plan
	 */
	public Plan getPlan() {
		return plan;
	}

	/**
	 * Returns the month.
	 * @return the month
	 */
	public RsMonth getMonth() {
		return month;
	}

	/**
	 * Returns the budgets.
	 * @return
	 */
	protected IObservableList getTransactions() {
		return transactions;
	}
	
	/**
	 * Provides the color for the background.
	 * @author ralph
	 *
	 */
	protected class TxBackgroundProvider extends ColumnAdapter {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public RGB getBackground(Object element, Object cellValue) {
			if (((TxRowWrapper)element).isBudget()) return RsBudgetColors.RGB_BG_BUDGET;
			return null;
		}
		
	}
	
	public static class TransactionSorter implements Comparator<TxRowWrapper> {

		@Override
		public int compare(TxRowWrapper o1, TxRowWrapper o2) {
			int rc = 0;
			if ((o1.isTransaction() || o1.isPlannedTransaction()) && o2.isBudget()) {
				rc = 1;
			} else if (o1.isBudget() && (o2.isTransaction() || o2.isPlannedTransaction())) {
				rc = -1;
			} else if (o1.isBudget() == o2.isBudget()) {
				rc = o1.getDisplayOrder() - o2.getDisplayOrder();
			} else {
				rc = o1.getDisplayOrder() - o2.getDisplayOrder();
				//rc = o1.getCreationDate().compareTo(o2.getCreationDate());
			}
			return rc;
		}
		
	}

	
}
