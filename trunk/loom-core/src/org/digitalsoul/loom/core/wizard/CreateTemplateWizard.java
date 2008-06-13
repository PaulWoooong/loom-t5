package org.digitalsoul.loom.core.wizard;


import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.wizard.Wizard;

public class CreateTemplateWizard extends Wizard {

    private CreateTemplateWizardPage page;
//    private String relativeFolder;
 //   private String projectPath;
   // private String filename;
    private IJavaElement javaElement;

    public CreateTemplateWizard(IJavaElement javaElement, String filename, String projectPath, String relativeFolder) {
   //     this.filename = filename;
        this.javaElement = javaElement;
        //filename = filename.replace("." + LoomConstants.JAVA_FILE_EXTENSION, "." + LoomConstants.TEMPLATE_FILE_EXTENSION);
       // this.projectPath = projectPath;
        //this.relativeFolder = relativeFolder;
        //System.err.println(projectPath);
        page = new CreateTemplateWizardPage("Create Template");
        page.setDescription("Choose a package and a name for your Template");
        page.init(javaElement, filename);
        //setWindowTitle("Create Template");
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
        // create the file
       // System.err.println(page.getCreatedType().getPath());
        System.err.println(page.getNewTemplatePath());
        return true;
    }
}
