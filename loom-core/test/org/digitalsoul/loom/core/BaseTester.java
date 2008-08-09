package org.digitalsoul.loom.core;

import org.eclipse.swt.widgets.Shell;

import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;


/**
 * Class BaseTester
 */
public class BaseTester {

    /**
     * @return
     */
    protected IEditorPart getActiveEditor() {
        IWorkbenchPage activePage = getActivePage();
        return activePage.getActiveEditor();
    }

    /**
     * @return
     */
    protected Shell getShell() {
        return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    }
    
    /**
     * @return
     */
    protected IWorkbenchPage getActivePage() {
        IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        return activePage;
    }
}
