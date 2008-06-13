package org.digitalsoul.loom.core.wizard;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.ui.wizards.NewTypeWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class CreateTemplateWizardPage extends NewTypeWizardPage {

    private Text nameField;
    private String filename;
    private IStatus status;

    protected CreateTemplateWizardPage(String pageName) {
        super(false, pageName);
    }

    public void init(IJavaElement javaElement, String filename) {
        initContainerPage(javaElement);
        initTypePage(javaElement);
        this.filename = filename;
    }

//    private boolean isPackageFragmentExisting() {
//        if (getPackageFragment() == null) {
//            return false;
//        }
//        else return getPackageFragment().exists();
//    }

    @Override
    public boolean isPageComplete() {
        return !status.matches(IStatus.ERROR) && !isFileAlreadyAvailable();
    }

    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        composite.setLayout(new GridLayout(4, false));
        createPackageControls(composite, 4);
        createNameRow(composite);
        setControl(composite);
    }
    
    @Override
    protected IStatus packageChanged() {
        IStatus status = super.packageChanged();
        this.status = status;
        updateStatus(status);
        return status;
    }

//    @Override
//    protected void handleFieldChanged(String fieldName) {
//
//        if (nameField != null) { 
//            
//          setPageComplete(isPageComplete());
//          
//          if (!isPageComplete()) {
//              if (isFileAlreadyAvailable()) {
//                  setErrorMessage("This template already exists");
//              }
//              else if (!isPackageFragmentExisting()) {
//                  setMessage("This package doesn't exist but will be created", IStatus.WARNING);
//              }
//          }
//          else setErrorMessage(null);
//        }
//    }

    private void createNameRow(Composite composite) {
        Label label = new Label(composite, SWT.NONE);
        label.setText("Name: ");
        label.setLayoutData(new GridData());
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        nameField = new Text(composite, SWT.BORDER);
        nameField.setText(filename);
        nameField.setLayoutData(gridData);
        nameField.addModifyListener(new ModifyListener () {

            public void modifyText(ModifyEvent e) {
                setPageComplete(isPageComplete());
            }               
        });
        new Composite(composite, SWT.NONE);
        new Composite(composite, SWT.NONE);
    }
    
    private boolean isFileAlreadyAvailable() {
        boolean templateAlreadyExists = getNewTemplatePath().toFile().exists();
        boolean cannotWriteTemplate = getNewTemplatePath().toFile().canWrite();

        if (templateAlreadyExists) {
            setErrorMessage("This template already exists");
        }
        else if (cannotWriteTemplate) {
            setErrorMessage("No permission to write file");
        }
        else setErrorMessage(null);
        return templateAlreadyExists;
    }

    public String getText() {
        return nameField.getText();
    }

    public IPath getNewTemplatePath() {
        IPath path = getPackageFragment().getResource().getLocation();
       // if (getPackageFragment() != null) {
            
//        }
//        else {
//            System.err.println("no packagefragment found");
//            String text = getPackageText();
//            path = getPackageFragmentRoot().getResource().getLocation();
//            for (String part : text.split(".")) {
//                path = path.append(part);
//            }
//        }
        path = path.append(nameField.getText());
        return path;
    }
}
