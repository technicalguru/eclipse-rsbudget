/**
 * 
 */
package rsbudget.parts.history;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.core.databinding.property.list.IListProperty;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import rs.e4.SwtUtils;
import rs.e4.celledit.CellEditorActivationStrategy;
import rs.e4.celledit.EditingSupportModelAdapter;
import rs.e4.celledit.BigDecimalEditingSupport;
import rs.e4.celledit.IEditingSupportModel;
import rs.e4.util.AbstractColumnLabelProvider;
import rs.e4.util.ColumnAdapter;
import rs.e4.util.DateColumnLabelProvider;
import rsbudget.Plugin;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Account;
import rsbudget.data.api.bo.HistoricalItem;
import rsbudget.preferences.PreferencesUtils;
import rsbudget.util.RsBudgetColors;
import rsbudget.util.TableColumnResizeListener;

/**
 * The history list.
 * @author ralph
 *
 */
public class HistoryPart {

	public static final String KEY_PURPOSE = "rsbudget.purpose";
	public static final String KEY_OBJECT = "rsbudget.object";
	public static final String VALUE_VALUE = "rsbudget.value";
	public static final String VALUE_CHANGE = "rsbudget.change";

	private FormToolkit toolkit;
	private LocalResourceManager resourceManager;
	public static HistoryListSorter SORTER = new HistoryListSorter();

	/** The list of transactions currently displayed */
	private IObservableList<StatusRecord> records;

	private Table table;
	private TableViewer tableViewer;
	private List<TableColumn> columns = new ArrayList<TableColumn>();
	private HistoryFontProvider historyFontProvider;
	
	@Inject
	private RsBudgetDaoFactory factory;

	/**
	 * Constructor.
	 */
	public HistoryPart() {
	}

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

		createTableSection(container, menuService);
	}

	/**
	 * Creates the table.
	 * @param parent
	 */
	protected void createTableSection(Composite parent, EMenuService menuService) {
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
		historyFontProvider = new HistoryFontProvider();
		
		ModelListener modelListener = new ModelListener();
		{
			// DATE COLUMN
			TableViewerColumn columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT);
			TableColumn column = columnViewer.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("history", "date", 50, 10, true));
			column.setText(Plugin.translate("part.history.column.date.title"));
			column.addControlListener(new TableColumnResizeListener("history", "date"));
			AbstractColumnLabelProvider lp = new DateColumnLabelProvider("timestamp", resourceManager); 
			lp.addColorProvider(new HistoryBackgroundProvider(-1));
			lp.addFontProvider(historyFontProvider);
			columnViewer.setLabelProvider(lp);
			columns.add(column);

			// STATUS COLUMN
			columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT);
			column = columnViewer.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("history", "status", 80, 10, true));
			column.setText(Plugin.translate("part.history.column.status.title"));
			column.addControlListener(new TableColumnResizeListener("history", "status"));
			column.setData(KEY_PURPOSE, VALUE_VALUE);
			lp = new HistoryLabelProvider(0, 2, resourceManager); 
			lp.addColorProvider(new HistoryBackgroundProvider(0));
			lp.addColorProvider(new HistoryForegroundProvider(0));
			lp.addFontProvider(historyFontProvider);
			columnViewer.setLabelProvider(lp);
			columns.add(column);

			// STATUS CHANGE COLUMN
			columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT);
			column = columnViewer.getColumn();
			tableLayout.addColumnData(PreferencesUtils.getTableColumnData("history", "change", 80, 10, true));
			column.setText(Plugin.translate("part.history.column.change.title"));
			column.addControlListener(new TableColumnResizeListener("history", "change"));
			column.setData(KEY_PURPOSE, VALUE_CHANGE);
			lp = new HistoryLabelProvider(1, 2, resourceManager); 
			lp.addColorProvider(new HistoryBackgroundProvider(1));
			lp.addColorProvider(new HistoryForegroundProvider(1));
			lp.addFontProvider(historyFontProvider);
			columnViewer.setLabelProvider(lp);
			columns.add(column);

			factory.begin();

			// ADDITIONAL ACCOUNTS
			int index = 2;
			for (Account account : factory.getAccountDAO().findDefaultAll()) {
				if (account.isActive() && !account.isPlanningRelevant()) {
					columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT);
					column = columnViewer.getColumn();
					tableLayout.addColumnData(PreferencesUtils.getTableColumnData("history", "column"+index, 80, 10, true));
					column.setText(account.getName());
					column.setData(KEY_PURPOSE, VALUE_VALUE);
					column.setData(KEY_OBJECT, account);
					column.addControlListener(new TableColumnResizeListener("history", "column"+index));
					lp = new HistoryLabelProvider(index, 2, resourceManager); 
					lp.addColorProvider(new HistoryBackgroundProvider(index));
					lp.addColorProvider(new HistoryForegroundProvider(index));
					lp.addFontProvider(historyFontProvider);
					columnViewer.setLabelProvider(lp);
					IEditingSupportModel accountModel = new HistoryEditingSupportModel(account, index);
					accountModel.addEditingSupportModelListener(modelListener);
					columnViewer.setEditingSupport(new BigDecimalEditingSupport(tableViewer, accountModel, true, 2));
					columns.add(column);
					index++;

					columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT);
					column = columnViewer.getColumn();
					tableLayout.addColumnData(PreferencesUtils.getTableColumnData("history", "column"+index, 80, 10, true));
					column.setText(Plugin.translate("part.history.column.change.title"));
					column.setData(KEY_PURPOSE, VALUE_CHANGE);
					column.setData(KEY_OBJECT, account);
					column.addControlListener(new TableColumnResizeListener("history", "column"+index));
					lp = new HistoryLabelProvider(index, 2, resourceManager); 
					lp.addColorProvider(new HistoryBackgroundProvider(index));
					lp.addColorProvider(new HistoryForegroundProvider(index));
					lp.addFontProvider(historyFontProvider);
					columnViewer.setLabelProvider(lp);
					columns.add(column);
					index++;
				}

			}
			// ADDITIONAL HISTORICAL ITEMS
			for (HistoricalItem item : factory.getHistoricalItemDAO().findDefaultAll()) {
				if (item.isShowHistory()) {
					columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT);
					column = columnViewer.getColumn();
					tableLayout.addColumnData(PreferencesUtils.getTableColumnData("history", "column"+index, 80, 10, true));
					column.setText(item.getName());
					column.setData(KEY_PURPOSE, VALUE_VALUE);
					column.setData(KEY_OBJECT, item);
					column.addControlListener(new TableColumnResizeListener("history", "column"+index));
					lp = new HistoryLabelProvider(index, item.getScale(), resourceManager); 
					lp.addColorProvider(new HistoryBackgroundProvider(index));
					lp.addFontProvider(historyFontProvider);
					columnViewer.setLabelProvider(lp);
					IEditingSupportModel itemModel = new HistoryEditingSupportModel(item, index);
					itemModel.addEditingSupportModelListener(modelListener);
					columnViewer.setEditingSupport(new BigDecimalEditingSupport(tableViewer, itemModel, true, item.getScale()));
					columns.add(column);
					index++;

					if (item.isShowChanges()) {
						columnViewer = new TableViewerColumn(tableViewer, SWT.RIGHT);
						column = columnViewer.getColumn();
						tableLayout.addColumnData(PreferencesUtils.getTableColumnData("history", "column"+index, 80, 10, true));
						column.setText(Plugin.translate("part.history.column.change.title"));
						column.setData(KEY_PURPOSE, VALUE_CHANGE);
						column.setData(KEY_OBJECT, item);
						column.addControlListener(new TableColumnResizeListener("history", "column"+index));
						lp = new HistoryLabelProvider(index, item.getScale(), resourceManager); 
						lp.addColorProvider(new HistoryBackgroundProvider(index));
						lp.addFontProvider(historyFontProvider);
						columnViewer.setLabelProvider(lp);
						columns.add(column);
						index++;
					}
				}

			}

			factory.commit();
		}

		// Context menu
		menuService.registerContextMenu(table, "rs.rcpplugins.rsbudget.popupmenu.history");

		// Content provider
		tableViewer.setContentProvider(new ObservableListContentProvider());
		tableViewer.setInput(createModel());
		if (records.size() > 0) tableViewer.reveal(records.get(records.size()-1));
	}

	/**
	 * Sets the focus to the table widget.
	 * (Required by E4)
	 */
	@Focus
	protected void setFocus() {
		table.setFocus();
	}

	/**
	 * Disposes everything.
	 */
	@PreDestroy
	public void dispose() {
		toolkit.dispose();
		if (records != null) records.dispose();
	}

	/**
	 * Creates the initial model.
	 * @return
	 */
	protected IObservableList<StatusRecord> createModel() {
		if (records != null) return records;
		HistoryModelCreator modelCreator = new HistoryModelCreator(factory, tableViewer);
		List<StatusRecord> l = modelCreator.retrieveRecords();
		
		IListProperty<List<StatusRecord>, StatusRecord> property = Properties.selfList(StatusRecord.class);
		records = property.observe(l);
		bind();

		return records;
	}

	/**
	 * Bind our change listener.
	 */
	@SuppressWarnings("unchecked")
	protected void bind() {
		BeanProperties.value(StatusRecord.class, StatusRecord.PROPERTY_STATUS_DATE).observeDetail(records);
	}

	/**
	 * Unbind our change listener.
	 */
	protected void unbind() {
	}

	public String[] getTableHeaders() {
		String rc[] = new String[table.getColumnCount()];
		int i=0;
		for (TableColumn column : table.getColumns()) {
			rc[i++] = column.getText();
		}
		return rc;
	}
	
	/**
	 * Sorting the history records.
	 * @author ralph
	 *
	 */
	public static class HistoryListSorter implements Comparator<StatusRecord> {

		@Override
		public int compare(StatusRecord o1, StatusRecord o2) {
			return o1.getTimestamp().compareTo(o2.getTimestamp());
		}

	}

	/**
	 * Provides the color for the background.
	 * @author ralph
	 *
	 */
	protected class HistoryBackgroundProvider extends ColumnAdapter {

		private int index;
		
		public HistoryBackgroundProvider(int index) {
			this.index = index;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public RGB getBackground(Object element, Object cellValue) {
			if (index < 0) return RsBudgetColors.RGB_BG_ROW_HEADER;
			if (!((StatusRecord)element).isEditable(index)) return RsBudgetColors.RGB_BG_UNEDITABLE;
			return null;
		}
		
	}

	/**
	 * Provides the color for the foreground.
	 * @author ralph
	 *
	 */
	protected class HistoryForegroundProvider extends ColumnAdapter {

		private int index;
		
		public HistoryForegroundProvider(int index) {
			this.index = index;
		}
		/**
		 * {@inheritDoc}
		 */
		@Override
		public RGB getForeground(Object element, Object cellValue) {
			Number n = ((StatusRecord)element).getValue(index);
			if ((n != null) && (n.floatValue() < 0)) return RsBudgetColors.RGB_RED;
			return null;
		}
		
	}

	/**
	 * Provides a bold font when the month is january.
	 * @author ralph
	 *
	 */
	protected class HistoryFontProvider extends ColumnAdapter {

		private Font boldFont = resourceManager.createFont(SwtUtils.deriveBoldFont(table.getFont()));
		
		/**
		 * {@inheritDoc}
		 */
		@Override
		public Font getFont(Object element, Object cellValue) {
			int month = ((StatusRecord)element).getTimestamp().get(Calendar.MONTH);
			if (month == Calendar.JANUARY) {
				return boldFont;
			}
			return null;
		}
		
	}
	
	/**
	 * Updates the row that was edited.
	 * @author ralph
	 *
	 */
	protected class ModelListener extends EditingSupportModelAdapter {

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void afterSetValue(Object element, String beanProperty, Object value) {
			tableViewer.refresh(element);
		}
		
	}
}
