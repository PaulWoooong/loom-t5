package org.digitalsoul.loom.core;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


public class CreateTemplateAction implements IObjectActionDelegate {

    private ICompilationUnit javaElement;

    public void run(IAction action) {
        new TemplateBuilder().createTemplate(javaElement);
    }

    public void selectionChanged(IAction action, ISelection _selection) {
        IStructuredSelection selection = (IStructuredSelection) _selection;
        this.javaElement = (ICompilationUnit)selection.getFirstElement();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }
}
