package org.digitalsoul.loom.core.wizard;


import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.wizard.Wizard;

import java.io.IOException;

public class CreateTemplateWizard extends Wizard {

    private CreateTemplateWizardPage page;

    public CreateTemplateWizard(IJavaElement javaElement, String filename) {
        page = new CreateTemplateWizardPage("Create Template");
        page.setDescription("Choose a package and a name for your Template");
        page.init(javaElement, filename);
        addPage(page);
    }
    
    @Override
    public boolean canFinish() {
        return page.isPageComplete();
    }

    @Override
    public boolean needsPreviousAndNextButtons() {
        return false;
    }


    @Override
    public boolean performFinish() {
        IPath newFilePath = page.getNewTemplatePath();
        try {
            newFilePath.toFile().createNewFile();
            page.getPackageFragment().getResource().refreshLocal(IResource.DEPTH_INFINITE, null);
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        catch (CoreException e) {
            e.printStackTrace();
        }
        return true;
    }
}
