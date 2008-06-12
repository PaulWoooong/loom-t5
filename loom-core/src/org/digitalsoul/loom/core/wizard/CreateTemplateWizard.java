package org.digitalsoul.loom.core.wizard;


import org.digitalsoul.loom.core.LoomConstants;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class CreateTemplateWizard extends Wizard {

    private Page page;
    private String relativeFolder;
    private String projectPath;
    private String filename;

    public CreateTemplateWizard(IJavaElement javaElement, String filename, String projectPath, String relativeFolder) {
        this.filename = filename;
        //filename = filename.replace("." + LoomConstants.JAVA_FILE_EXTENSION, "." + LoomConstants.TEMPLATE_FILE_EXTENSION);
        this.projectPath = projectPath;
        this.relativeFolder = relativeFolder;
        //System.err.println(projectPath);
        page = new Page("Bla");
        page.setDescription("Choose a name and location for your Template");
        page.init(javaElement);
        setWindowTitle("Create Template");
        addPage(page);
        
    }
    
    @Override
    public boolean canFinish() {
        return !isFileAlreadyAvailable();
    }

    @Override
    public boolean needsPreviousAndNextButtons() {
        return false;
    }


    @Override
    public boolean performFinish() {
        // create the file
        return true;
    }
    
    private boolean isFileAlreadyAvailable() {
        // getText();
        return false;
    }

    class Page extends NewTypeWizardPage {

        private Text nameField;
        private Text locationField;

        protected Page(String pageName) {
            super(false, pageName);
            //initContainerPage(javaElement);
            //initTypePage(javaElement);
        }

        public void init(IJavaElement javaElement) {

            initContainerPage(javaElement);
            initTypePage(javaElement);
            
        }

        @Override
        public String getErrorMessage() {
            String msg = null;
            if (isFileAlreadyAvailable()) {
                msg = "This template already exists";
            }
            return msg;
        }

        @Override
        public boolean isPageComplete() {
            return !isFileAlreadyAvailable();
        }

        public void createControl(Composite parent) {
            //initializeDialogUnits(parent);      
            
            Composite composite = new Composite(parent, SWT.NONE);
            composite.setLayout(new GridLayout(4, false));
            createPackageControls(composite, 4);
            createNameRow(composite);
            //createLocationRow(composite);
            
            
            setControl(composite);
        }

        private void createLocationRow(final Composite composite) {
            Label label = new Label(composite, SWT.NONE);
            label.setText("Location: ");
            label.setLayoutData(new GridData());
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.grabExcessHorizontalSpace = true;
            locationField = new Text(composite, SWT.BORDER);
            locationField.setText(relativeFolder);
            locationField.setLayoutData(gridData);
            locationField.setEditable(false);
            locationField.addModifyListener(new ModifyListener () {
                
                public void modifyText(ModifyEvent e) {
                    setPageComplete(!isFileAlreadyAvailable());
                }               
            });
            Button button = new Button(composite, SWT.NONE);
            button.setText("Browse...");
            button.addListener(SWT.Selection, new Listener () {

                public void handleEvent(Event event) {
                    DirectoryDialog directoryDialog = new DirectoryDialog(composite.getShell(), SWT.OPEN);
                    directoryDialog.setFilterPath(projectPath + "/" + relativeFolder);
                    directoryDialog.open();
                }
            });
        }

        private void createNameRow(Composite composite) {
            Label label = new Label(composite, SWT.NONE);
            label.setText("Filename: ");
            label.setLayoutData(new GridData());
            GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
            gridData.grabExcessHorizontalSpace = true;
            nameField = new Text(composite, SWT.BORDER);
            nameField.setText(filename);
            nameField.setLayoutData(gridData);
            nameField.addModifyListener(new ModifyListener () {

                public void modifyText(ModifyEvent e) {
                    setPageComplete(!isFileAlreadyAvailable());
                }               
            });
            new Composite(composite, SWT.NONE);
            new Composite(composite, SWT.NONE);
        }

        public String getText() {
            return nameField.getText();
        }
    }
}
