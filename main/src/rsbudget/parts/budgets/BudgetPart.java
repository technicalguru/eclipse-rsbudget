/**
 * 
 */
package rsbudget.parts.budgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.internal.contexts.EclipseContext;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.FontDescriptor;
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
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import rs.baselib.io.FileFinder;
import rs.baselib.util.IWrapper;
import rs.data.api.bo.IGeneralBO;
import rs.data.event.DaoEvent;
import rs.data.event.IDaoListener;
import rs.e4.E4Utils;
import rs.e4.SwtUtils;
import rs.e4.celledit.BeanEditingSupportModel;
import rs.e4.celledit.CellEditorActivationStrategy;
import rs.e4.celledit.ComboBoxEditingSupport;
import rs.e4.celledit.EnumerationEditingSupportModel;
import rs.e4.celledit.FloatEditingSupport;
import rs.e4.celledit.IComboBoxEditingSupportModel;
import rs.e4.celledit.IEditingSupportModel;
import rs.e4.celledit.TextEditingSupport;
import rs.e4.swt.action.ButtonHandler;
import rs.e4.util.AbstractColumnLabelProvider;
import rs.e4.util.BeanColumnLabelProvider;
import rs.e4.util.BeanLabelProvider;
import rs.e4.util.ColumnAdapter;
import rsbudget.Plugin;
import rsbudget.celledit.BoSelectionSupportModel;
import rsbudget.celledit.EditingSupportModelListener;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.bo.PeriodicalBudget;
import rsbudget.data.api.bo.PeriodicalTransaction;
import rsbudget.data.util.PlannedPeriod;
import rsbudget.preferences.PreferencesUtils;
import rsbudget.util.CurrencyColumnLabelProvider;
import rsbudget.util.CurrencyLabelProvider;
import rsbudget.util.RsBudgetColors;
import rsbudget.util.TableColumnResizeListener;

/**
 * The budget planning (general).
 * @author ralph
 *
 */
public class BudgetPart {

	private FormToolkit toolkit;
	private LocalResourceManager resourceManager;
	public static BudgetListSorter SORTER = new BudgetListSorter();

	private Table table;
	private TableViewer tableViewer;
	private TableColumn tblclmn1;
	private TableColumn tblclmn2;
	private TableColumn tblclmn3;
	private TableColumn tblclmn4;
	private TableColumn tblclmn5;
	private TableColumn tblclmn6;
	private TableColumn tblclmn7;
	private Label incomeMonth;
	private Label expensesMonth;
	private Label balanceMonth;
	private Label incomeYear;
	private Label expensesYear;
	private Label balanceYear;
	private Label incomeMonthAverage;
	private Label expensesMonthAverage;
	private Label balanceAverage;
	
	@Inject
	private RsBudgetDaoFactory factory;
	
	@Inject
	private ESelectionService selectionService;
	
	@Inject
	private UISynchronize uiSynchronize;
	
	@Inject
	private MPart part;
	
	@Inject
	private IEclipseContext context;
	
	/** The list of budgets currently displayed */
	private IObservableList budgets;
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
			BudgetPart.this.handleDaoEvent(event);
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
		
		createInfoSection(container);
		createTableSection(container, menuService);
		
		updateInfoPart();
	}
	
	protected void createInfoSection(Composite parent) {
		
		Composite composite = toolkit.createComposite(parent, SWT.NONE);
		composite.setFont(resourceManager.createFont(FontDescriptor.createFrom("Tahoma", 8, SWT.BOLD)));
		composite.setLayout(new GridLayout(6, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		toolkit.paintBordersFor(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.income.monthly"));
		incomeMonth = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.income.yearly"));
		incomeYear = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.income.average"));
		incomeMonthAverage = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.expenses.monthly"));
		expensesMonth = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.expenses.yearly"));
		expensesYear = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.expenses.average"));
		expensesMonthAverage = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.balance.monthly"));
		balanceMonth = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.balance.yearly"));
		balanceYear = createValue(composite);
		
		createLabel(composite, Plugin.translate("part.budgets.label.balance.average"));
		balanceAverage = createValue(composite);

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
			BudgetBackgroundProvider backgroundProvider = new BudgetBackgroundProvider();
			EditingSupportModelListener editListener = new EditingSupportModelListener();
			
			TableViewerColumn column1 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn1 = column1.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("budgets", "cycle", 80, 10, true));
			tblclmn1.setText(Plugin.translate("part.budgets.column.cycle.title"));
			AbstractColumnLabelProvider lp = new BeanColumnLabelProvider("plannedPeriod", resourceManager); 
			lp.addColorProvider(backgroundProvider);
			column1.setLabelProvider(lp);
			column1.getColumn().addControlListener(new TableColumnResizeListener("budgets", "cycle"));
			IComboBoxEditingSupportModel periodModel = new EnumerationEditingSupportModel(PlannedPeriod.class, "plannedPeriod");
			periodModel.addEditingSupportModelListener(editListener);
			column1.setEditingSupport(new ComboBoxEditingSupport(tableViewer, periodModel, false));
			
			TableViewerColumn column2 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn2 = column2.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("budgets", "occurance", 70, 10, true));
			tblclmn2.setText(Plugin.translate("part.budgets.column.occurance.title"));
			lp = new SequenceLabelProvider(resourceManager);
			lp.addColorProvider(backgroundProvider);
			column2.setLabelProvider(lp);
			column2.getColumn().addControlListener(new TableColumnResizeListener("budgets", "occurance"));
			IComboBoxEditingSupportModel sequenceModel = new SequenceNumberSupportModel();
			sequenceModel.addEditingSupportModelListener(editListener);
			column2.setEditingSupport(new ComboBoxEditingSupport(tableViewer, sequenceModel, false));

			TableViewerColumn column3 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn3 = column3.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("budgets", "text", 250, 10, true));
			tblclmn3.setText(Plugin.translate("part.budgets.column.text.title"));
			lp = new BeanColumnLabelProvider("text", resourceManager);
			lp.addColorProvider(backgroundProvider);
			column3.setLabelProvider(lp);
			column3.getColumn().addControlListener(new TableColumnResizeListener("budgets", "text"));
			IEditingSupportModel textModel = new BeanEditingSupportModel("text");
			textModel.addEditingSupportModelListener(editListener);
			column3.setEditingSupport(new TextEditingSupport(tableViewer, textModel, false));
			
			TableViewerColumn column4 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn4 = column4.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("budgets", "details", 350, 10, true));
			tblclmn4.setText(Plugin.translate("part.budgets.column.details.title"));
			lp = new BeanColumnLabelProvider("details", resourceManager);
			lp.addColorProvider(backgroundProvider);
			column4.setLabelProvider(lp);
			column4.getColumn().addControlListener(new TableColumnResizeListener("budgets", "details"));
			IEditingSupportModel detailsModel = new BeanEditingSupportModel("details");
			detailsModel.addEditingSupportModelListener(editListener);
			column4.setEditingSupport(new TextEditingSupport(tableViewer, detailsModel, false));
			
			TableViewerColumn column5 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn5 = column5.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("budgets", "budget", 110, 10, true));
			tblclmn5.setText(Plugin.translate("part.budgets.column.budget.title"));
			lp = new BeanColumnLabelProvider("txBudget", new BeanLabelProvider("name"), resourceManager);
			lp.addColorProvider(backgroundProvider);
			column5.setLabelProvider(lp);
			column5.getColumn().addControlListener(new TableColumnResizeListener("budgets", "budget"));
			IComboBoxEditingSupportModel budgetModel = new BoSelectionSupportModel<PeriodicalBudget>(factory.getPeriodicalBudgetDAO(), "txBudget");
			budgetModel.addEditingSupportModelListener(editListener);
			column5.setEditingSupport(new ComboBoxEditingSupport(tableViewer, budgetModel, true));

			TableViewerColumn column6 = new TableViewerColumn(tableViewer, SWT.NONE);
			tblclmn6 = column6.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("budgets", "category", 110, 10, true));
			tblclmn6.setText(Plugin.translate("part.budgets.column.category.title"));
			lp = new BeanColumnLabelProvider("category.name", resourceManager);
			lp.addColorProvider(backgroundProvider);
			column6.setLabelProvider(lp);
			column6.getColumn().addControlListener(new TableColumnResizeListener("budgets", "category"));
			IComboBoxEditingSupportModel categoryModel = new BoSelectionSupportModel<Category>(factory.getCategoryDAO(), "category");
			categoryModel.addEditingSupportModelListener(editListener);
			column6.setEditingSupport(new ComboBoxEditingSupport(tableViewer, categoryModel, false));
			
			TableViewerColumn column7 = new TableViewerColumn(tableViewer, SWT.RIGHT);
			tblclmn7 = column7.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("budgets", "amount", 100, 10, true));
			tblclmn7.setText(Plugin.translate("part.budgets.column.amount.title"));
			lp = new CurrencyColumnLabelProvider("amount", resourceManager);
			lp.addColorProvider(backgroundProvider);
			column7.setLabelProvider(lp);
			column7.getColumn().addControlListener(new TableColumnResizeListener("budgets", "amount"));
			IEditingSupportModel amountModel = new BeanEditingSupportModel("amount");
			amountModel.addEditingSupportModelListener(editListener);
			column7.setEditingSupport(new FloatEditingSupport(tableViewer, amountModel, false));
			
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
		menuService.registerContextMenu(table, "rs.rcpplugins.rsbudget.popupmenu.budgets");
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
		b1.setToolTipText(Plugin.translate("e4xmi.part.budgets.toolitem.newtx.tooltip"));
		createButtonHandler(b1, newHandler);
		Button b2 = new Button(parent, SWT.PUSH);
		b2.setImage(resourceManager.createImage(ImageDescriptor.createFromURL(FileFinder.find("resources/icons/table-delete-row.png"))));
		b2.setToolTipText(Plugin.translate("e4xmi.part.budgets.toolitem.deletetx.tooltip"));
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
	protected BudgetRowWrapper[] toArray(Object array[]) {
		if (array == null) return null;
		BudgetRowWrapper rc[] = new BudgetRowWrapper[array.length];
		int i=0;
		for (Object o : array) rc[i++] = (BudgetRowWrapper)o;
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
			BudgetRowWrapper row = null;
			if (src == factory.getPeriodicalBudgetDAO()) row = new BudgetRowWrapper((PeriodicalBudget)bo);
			else row = new BudgetRowWrapper((PeriodicalTransaction)bo);
			if (row.isTransaction()) {
				budgets.add(row);
			} else {
				int index = budgets.size();
				for (int i=0; i<budgets.size(); i++) {
					if (((BudgetRowWrapper)budgets.get(i)).isTransaction()) {
						index = i; break;
					}
				}
				budgets.add(index, row);
			}
			break;
		}
		case OBJECT_DELETED:
			Object found = null;
			for (Object o : budgets) {
				if (o.equals(bo)) { found = o; break; }
			}
			budgets.remove(found);
			break;
		case ALL_DEFAULT_DELETED:
		case ALL_DELETED:
			// Dunno yet what to do
			budgets.clear();
		default:
		}
	}
	
	/**
	 * Edit the given row.
	 * @param row
	 */
	public void edit(final Object row) {
		if (row instanceof BudgetRowWrapper) {
			uiSynchronize.asyncExec(new Runnable() {				
				@Override
				public void run() {
			tableViewer.reveal(row);
			tableViewer.editElement(row, 2);
				}
			});
		} else {
			for (Object o : budgets) {
				if (((IWrapper)o).getWrapped().equals(row)) {
					edit((BudgetRowWrapper)o);
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
		if (row instanceof BudgetRowWrapper) {
			tableViewer.reveal(row);
			tableViewer.setSelection(new StructuredSelection(row));
		} else {
			for (Object o : budgets) {
				if (((IWrapper)o).getWrapped().equals(row)) {
					reveal((BudgetRowWrapper)o);
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
	 * Disposes everything.
	 */
	@PreDestroy
	public void dispose() {
		factory.getPeriodicalBudgetDAO().removeDaoListener(daoListener);
		factory.getPeriodicalTransactionDAO().removeDaoListener(daoListener);
		budgets.removeChangeListener(changeListener);
		toolkit.dispose();
		budgets.dispose();
	}
	
	/**
	 * Creates the initial model.
	 * @return
	 */
	protected IObservableList createModel() {
		if (budgets != null) return budgets;
		List<BudgetRowWrapper> l = new ArrayList<>();
		factory.begin();
		try {
			List<PeriodicalBudget> pb = factory.getPeriodicalBudgetDAO().findDefaultAll();
			if (pb != null) {
				for (PeriodicalBudget o : pb) l.add(new BudgetRowWrapper(o));
			}
			List<PeriodicalTransaction> tx = factory.getPeriodicalTransactionDAO().findDefaultAll();
			if (tx != null) {
				for (PeriodicalTransaction o : tx) l.add(new BudgetRowWrapper(o));
			}
		} finally {
			factory.commit();
		}
		Collections.sort(l, SORTER);
		budgets = Properties.selfList(BudgetRowWrapper.class).observe(l);
		budgets.addChangeListener(changeListener);
		bind(BudgetRowWrapper.PROPERTY_PLANNED_PERIOD);
		bind(BudgetRowWrapper.PROPERTY_SEQUENCE_NUMBER);
		bind(BudgetRowWrapper.PROPERTY_TX_BUDGET);
		bind(BudgetRowWrapper.PROPERTY_AMOUNT);
		
		// Listen to the DAOs for changes
		factory.getPeriodicalBudgetDAO().addDaoListener(daoListener);
		factory.getPeriodicalTransactionDAO().addDaoListener(daoListener);
		return budgets;
		
	}
	
	protected void bind(String property) {
		IObservableList list = BeanProperties.value(BudgetRowWrapper.class, property).observeDetail(budgets);
		list.addChangeListener(changeListener);
	}
	
	/**
	 * Update all labels.
	 */
	protected void updateInfoPart() {
		float incomeMonth          = 0;
		float expensesMonth        = 0;
		float balanceMonth         = 0;
		float incomeYear           = 0;
		float expensesYear         = 0;
		//float balanceYear          = 0;
		float incomeMonthAverage   = 0;
		float expensesMonthAverage = 0;
		float balanceMonthAverage  = 0;

		// Budgets
		for (Object o : budgets) {
			BudgetRowWrapper row = (BudgetRowWrapper)o;
			// If there is a budget, we need to ignore it
			if (row.getTxBudget() != null) continue;
			// This is the same for budgets and transactions
			float amount = row.getAmount();
			if (row.isBudget()) amount = ((PeriodicalBudget)row.getWrapped()).getPlanned();
			if (amount >= 0) {
				switch (row.getPlannedPeriod()) {
				case WEEKLY:
					incomeMonth += 4*amount; break;
				case MONTHLY:
					incomeMonth += amount;					
					break;
				case QUARTERLY:
					incomeYear += 4*amount; break;
				case HALF_YEARLY:
					incomeYear += 2*amount; break;
				case YEARLY:
					incomeYear += amount; break;
				}
			} else {
				switch (row.getPlannedPeriod()) {
				case WEEKLY:
					expensesMonth += 4*amount; break;
				case MONTHLY:
					expensesMonth += amount; 
					break;
				case QUARTERLY:
					expensesYear += 4*amount; break;
				case HALF_YEARLY:
					expensesYear += 2*amount; break;
				case YEARLY:
					expensesYear += amount; break;
				}
			}
		}

		// Some totals
		balanceMonth = incomeMonth + expensesMonth;
		//balanceYear  = incomeYear + expensesYear;
		incomeMonthAverage = incomeMonth + incomeYear / 12;
		expensesMonthAverage = expensesMonth + expensesYear / 12;
		balanceMonthAverage = incomeMonthAverage + expensesMonthAverage;
		float totalIncomeYear = 12*incomeMonth + incomeYear;
		float totalExpensesYear = 12*expensesMonth + expensesYear;
		float totalBalanceYear = totalIncomeYear + totalExpensesYear;
		
		// Update the values
		updateValueField(this.incomeMonth, incomeMonth);
		updateValueField(this.expensesMonth, expensesMonth);
		updateValueField(this.balanceMonth, balanceMonth);
		updateValueField(this.incomeYear, totalIncomeYear);
		updateValueField(this.expensesYear, totalExpensesYear);
		updateValueField(this.balanceYear, totalBalanceYear);
		updateValueField(this.incomeMonthAverage, incomeMonthAverage);
		updateValueField(this.expensesMonthAverage, expensesMonthAverage);
		updateValueField(this.balanceAverage, balanceMonthAverage);	
	
	}

	/**
	 * Set the given field to the value and format it.
	 * @param field label field
	 * @param amount amount
	 */
	private void updateValueField(Label field, float amount) {
		if (field.isDisposed()) return;
		field.setText(CurrencyLabelProvider.INSTANCE.getText(amount));
		RGB rgb = RsBudgetColors.RGB_BLACK;
		if (amount < 0) {
			rgb = RsBudgetColors.RGB_RED;
		}
		field.setForeground(resourceManager.createColor(rgb));
	}

	/**
	 * Creates a label for the info section.
	 * @param parent composite as parent of the label
	 * @param text text to be displayed
	 * @return the created label
	 */
	protected Label createLabel(Composite parent, String text) {
		Label rc = toolkit.createLabel(parent, text, SWT.NONE);
		rc.setFont(resourceManager.createFont(SwtUtils.deriveBoldFont(rc.getFont())));
		rc.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
		return rc;
	}
	
	/**
	 * Returns the value label.
	 * @param parent parent composite
	 * @return the created label
	 */
	protected Label createValue(Composite parent) {
		Label rc = toolkit.createLabel(parent, Plugin.translate("part.budgets.label.amount"), SWT.RIGHT);
		rc.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		return rc;
	}
	
	/**
	 * Returns the budgets.
	 * @return
	 */
	protected IObservableList getBudgets() {
		return budgets;
	}
	
	/**
	 * Provides the color for the background.
	 * @author ralph
	 *
	 */
	protected class BudgetBackgroundProvider extends ColumnAdapter {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public RGB getBackground(Object element, Object cellValue) {
			if (((BudgetRowWrapper)element).isBudget()) return RsBudgetColors.RGB_BG_BUDGET;
			return null;
		}
		
	}
	
	public static class BudgetListSorter implements Comparator<BudgetRowWrapper> {

		@Override
		public int compare(BudgetRowWrapper o1, BudgetRowWrapper o2) {
			if (o1.isTransaction() && o2.isBudget()) return 1;
			if (o1.isBudget() && o2.isTransaction()) return -1;
			if (o1.isBudget() == o2.isBudget()) {
				int cmp = o1.getDisplayOrder() - o2.getDisplayOrder();
				if (cmp != 0) return cmp;
			}
			return o1.getCreationDate().compareTo(o2.getCreationDate());
		}
		
	}

}

