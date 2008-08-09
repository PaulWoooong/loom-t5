package org.digitalsoul.loom.core.wizard;

import org.digitalsoul.loom.core.TemplateBuilder;

import org.eclipse.jdt.core.ICompilationUnit;

import org.digitalsoul.loom.core.prefs.Preferences;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.wizard.Wizard;

public class CreateTemplateWizard extends Wizard {

    private CreateTemplateWizardPage page;
    private ICompilationUnit javaElement;

    public CreateTemplateWizard(ICompilationUnit javaElement, String filename) {
        this.javaElement = javaElement;
        page = new CreateTemplateWizardPage("Create Template");
        page.setDescription("Choose a package and a name for your Template");
        page.init(javaElement, filename);
        addPage(page);
        String templateFragmentRoot = Preferences.getTemplateFragmentRootPath().replaceAll("/", "");
        try {
            for (IPackageFragmentRoot root : javaElement.getJavaProject().getAllPackageFragmentRoots()) {
                String rootPath = root.getPath().toOSString();
                rootPath = rootPath.replace(root.getPath().segment(0), "");
                if (rootPath.replaceAll("/","").equals(templateFragmentRoot)) {
                    page.setPackageFragmentRoot(root, true);
                }
            }
        }
        catch (JavaModelException e) {
            e.printStackTrace();
        }
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
        new TemplateBuilder().createTemplate(javaElement);
        return true;
    }
}
