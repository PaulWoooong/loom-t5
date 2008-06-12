package org.digitalsoul.loom.core;

import org.digitalsoul.loom.core.prefs.Preferences;
import org.digitalsoul.loom.core.wizard.CreateTemplateWizard;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;


public class CreateTemplateAction implements IObjectActionDelegate {

    private IResource selectedFile;
    private IJavaElement javaElement;

    public void run(IAction action) {
        String templateFilename = javaElement.getElementName();
        templateFilename = templateFilename.replace(LoomConstants.JAVA_FILE_EXTENSION, Preferences.getTemplateFileExtension());
        String relativeFolder = selectedFile.getParent().getProjectRelativePath().toPortableString();
        String projectPath = selectedFile.getProject().getLocation().toPortableString();
        CreateTemplateWizard wizard = new CreateTemplateWizard(javaElement, templateFilename, projectPath, relativeFolder);
        new WizardDialog(LoomCorePlugin.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(), wizard).open(); 
    }

    public void selectionChanged(IAction action, ISelection _selection) {
        IStructuredSelection selection = (IStructuredSelection) _selection;
        this.javaElement = (IJavaElement)selection.getFirstElement();
        this.selectedFile = javaElement.getResource();
    }

    public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    }
}
