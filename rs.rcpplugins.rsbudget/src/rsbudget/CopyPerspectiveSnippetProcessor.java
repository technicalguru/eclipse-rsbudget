package rsbudget;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.MUIElement;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspectiveStack;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

/**
 * Copies the perspective snippet to the perspective stack.
 * @author ralph
 *
 */
public class CopyPerspectiveSnippetProcessor {
	
	/** The perspective stack ID to be copied to */
	private static final String MAIN_PERSPECTIVE_STACK_ID = "rs.rcpplugins.rsbudget.perspectivestack.main";

	@Execute
	public void execute(EModelService modelService, MApplication application) {
		MPerspectiveStack perspectiveStack = (MPerspectiveStack) modelService.find(MAIN_PERSPECTIVE_STACK_ID, application);
		MPerspective perspectiveClone;

		// clone each snippet that is a perspective and add the cloned perspective into the main PerspectiveStack
		boolean isFirst = true;
		for (MUIElement snippet : application.getSnippets()) {
			if (snippet instanceof MPerspective) {
				perspectiveClone = (MPerspective) modelService.cloneSnippet(application, snippet.getElementId(), null);
				perspectiveStack.getChildren().add(perspectiveClone);
				if (isFirst) {
					perspectiveStack.setSelectedElement(perspectiveClone);
					isFirst = false;
				}
			}
		}
	}
}