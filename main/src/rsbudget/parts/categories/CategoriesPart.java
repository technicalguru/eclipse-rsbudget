/**
 * 
 */
package rsbudget.parts.categories;

import java.util.Collections;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.resource.LocalResourceManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.widgets.FormToolkit;

import rs.baselib.util.DefaultComparator;
import rs.data.event.DaoEvent;
import rs.data.event.IDaoListener;
import rs.e4.celledit.BeanEditingSupportModel;
import rs.e4.celledit.CellEditorActivationStrategy;
import rs.e4.celledit.IEditingSupportModel;
import rs.e4.celledit.TextEditingSupport;
import rs.e4.jface.databinding.ObservableDaoList;
import rs.e4.util.BeanColumnLabelProvider;
import rs.e4.util.EmptyLabelProvider;
import rsbudget.Plugin;
import rsbudget.celledit.EditingSupportModelListener;
import rsbudget.data.api.RsBudgetDaoFactory;
import rsbudget.data.api.bo.Category;
import rsbudget.data.api.dao.CategoryDAO;

/**
 * The categories list.
 * @author ralph
 *
 */
public class CategoriesPart {

	private FormToolkit toolkit;
	private LocalResourceManager resourceManager;

	private Table table;
	private CheckboxTableViewer tableViewer;
	private CheckStateListener checkStateListener;
	private CheckStateProvider checkStateProvider;
	private TableColumn tblclmn1;
	private TableColumn tblclmn2;

	@Inject
	private RsBudgetDaoFactory factory;
	
	@Inject
	private ESelectionService selectionService;
	
	/** The data model */
	private ObservableDaoList<Category> categories;
	
	/** Listener to DAO inserts/deletes */
	private IDaoListener daoListener = new IDaoListener() {
		@Override
		public void handleDaoEvent(DaoEvent event) {
			CategoriesPart.this.handleDaoEvent(event);
		}
	};

	/**
	 * Creates the controls.
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void createControls(Composite parent, EMenuService menuService) {
		toolkit = new FormToolkit(parent.getDisplay());
		resourceManager = new LocalResourceManager(JFaceResources.getResources(), parent);

		Composite container = toolkit.createComposite(parent, SWT.NONE);
		toolkit.paintBordersFor(container);
		container.setLayout(new GridLayout(1, false));
		{
			// Create the table viewer for our table
			table = toolkit.createTable(container, SWT.MULTI | SWT.H_SCROLL
					| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER | SWT.CHECK);
			TableLayout tableLayout = new TableLayout();
			table.setLayout(tableLayout);
			tableViewer = new CheckboxTableViewer(table);
			TableViewerEditor.create(
					tableViewer, 
					new CellEditorActivationStrategy(tableViewer), 
					ColumnViewerEditor.TABBING_HORIZONTAL 
					| ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR
					| ColumnViewerEditor.TABBING_VERTICAL
					| ColumnViewerEditor.KEYBOARD_ACTIVATION);
			
			checkStateListener = new CheckStateListener();
			checkStateProvider = new CheckStateProvider();
			tableViewer.setCheckStateProvider(checkStateProvider);
			tableViewer.addCheckStateListener(checkStateListener);

			table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
			toolkit.paintBordersFor(table);
			table.setHeaderVisible(true);
			table.setLinesVisible(true);
			
			{
				EditingSupportModelListener editListener = new EditingSupportModelListener();
				TableViewerColumn column2 = new TableViewerColumn(tableViewer, SWT.CENTER);
				tblclmn2 = column2.getColumn();
				tableLayout.addColumnData(new ColumnWeightData(00, 60, false));
				tblclmn2.setText(Plugin.translate("part.categories.column.default.title"));
				tblclmn2.setAlignment(SWT.CENTER);
				column2.setLabelProvider(new BeanColumnLabelProvider("default", EmptyLabelProvider.getInstance(), resourceManager));

				TableViewerColumn column1 = new TableViewerColumn(tableViewer, SWT.NONE);
				tblclmn1 = column1.getColumn();
				tableLayout.addColumnData(new ColumnWeightData(150, 100, true));
				tblclmn1.setText(Plugin.translate("part.categories.column.name.title"));
				column1.setLabelProvider(new BeanColumnLabelProvider("name", resourceManager));
				IEditingSupportModel nameModel = new BeanEditingSupportModel("name");
				nameModel.addEditingSupportModelListener(editListener);
				column1.setEditingSupport(new TextEditingSupport(tableViewer, nameModel, false));
			}
						
			tableViewer.setContentProvider(new ObservableListContentProvider());
			categories = new ObservableDaoList<Category>(factory.getCategoryDAO());
			Collections.sort(categories, DefaultComparator.INSTANCE);
			tableViewer.setInput(categories);
		}

		// Propagate the selection
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged(event);
			}
		});
		
		// Context menu
		menuService.registerContextMenu(table, "rs.rcpplugins.rsbudget.popupmenu.categories");

		// Listen to the DAOs for changes
		factory.getCategoryDAO().addDaoListener(daoListener);
	}
	
	/**
	 * Handles the selection change event.
	 * @param event
	 */
	protected void handleSelectionChanged(SelectionChangedEvent event) {
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		// set the selection to the service
		selectionService.setSelection(
				selection.size() == 1 ? selection.getFirstElement() : selection.toArray());
	}
	
	/**
	 * Handles the create/delete notifications
	 * @param event
	 */
	protected void handleDaoEvent(DaoEvent event) {
		Category category = (Category)event.getObject();
		switch (event.getType()) {
		case OBJECT_CREATED: {
			tableViewer.reveal(category);
			tableViewer.editElement(category, 1);
			break;
		}
		default:
		}
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
		categories.dispose();
		factory.getCategoryDAO().removeDaoListener(daoListener);
	}
	
	/**
	 * The provider for the check state.
	 * @author ralph
	 *
	 */
	protected class CheckStateProvider implements ICheckStateProvider {

		@Override
		public boolean isChecked(Object element) {
			if (element instanceof Category) {
				return ((Category)element).isDefault();
			}
			return false;
		}

		@Override
		public boolean isGrayed(Object element) {
			return false;
		}
		
	}
	
	/**
	 * Persists the default check state.
	 * @author ralph
	 *
	 */
	protected class CheckStateListener implements ICheckStateListener {

		@Override
		public void checkStateChanged(CheckStateChangedEvent event) {
			Category category = (Category)event.getElement();
			if (event.getChecked() && (category.getId() > 0)) {
				factory.begin();
				CategoryDAO dao = factory.getCategoryDAO();
				Category oldDefault = dao.findDefault();
				if (oldDefault != null) oldDefault.setDefault(false);
				category.setDefault(true);
				if (oldDefault != null) dao.save(oldDefault);
				dao.save(category);
				factory.commit();
			} else {
				// No no no, don't do this!
			}
			tableViewer.refresh();
		}
		
	}

}
