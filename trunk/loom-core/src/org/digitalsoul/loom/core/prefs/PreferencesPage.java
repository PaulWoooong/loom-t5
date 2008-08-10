/**
 * Copyright 2008 Loom Developers. This file is part of the loom eclipse plugin for eclipse
 * and is licensed under the GPL version 3. 
 * Please refer to the URL http://www.gnu.org/licenses/gpl-3.0.html for details.
 */
package org.digitalsoul.loom.core.prefs;

import org.digitalsoul.loom.core.EditorFileOpener;
import org.digitalsoul.loom.core.LoomConstants;
import org.digitalsoul.loom.core.LoomCorePlugin;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

/**
 * Class PreferencesPage
 */
public class PreferencesPage extends PreferencePage implements IWorkbenchPreferencePage {

    /**
     * 
     */
    protected Combo fileExtensionCombo;
    
    /**
     * 
     */
    protected Text templateFragmentRootTextField;
    
    /**
     * 
     */
    protected Button createTemplateInJavaFolderCheckbox;
    
    /**
     * 
     */
    private String[] fileExtensions = { LoomConstants.TML_FILE_EXTENSION, LoomConstants.HTML_FILE_EXTENSION };

    /**
     * 
     */
    public PreferencesPage() {
        setPreferenceStore(LoomCorePlugin.getDefault().getPreferenceStore());
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    @Override
    protected Control createContents(Composite parent) {

        Composite composite = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        composite.setLayout(layout);
        Label label = new Label(composite, SWT.NONE);
        label.setText("Template File Ending");
        fileExtensionCombo = new Combo(composite, SWT.READ_ONLY);
        fileExtensionCombo.setItems(fileExtensions);
        fileExtensionCombo.setVisibleItemCount(fileExtensions.length);
        
        label = new Label(composite, SWT.NONE);
        label.setText("Template Package Fragment Root");
        templateFragmentRootTextField = new Text(composite, SWT.BORDER);
        GridData data = new GridData();
        data.minimumWidth = 350;
        data.grabExcessHorizontalSpace = true;
        templateFragmentRootTextField.setLayoutData(data);
        
        createTemplateInJavaFolderCheckbox = new Button(composite, SWT.CHECK);
        createTemplateInJavaFolderCheckbox.setText("Always create a template in the java file's folder");
        createTemplateInJavaFolderCheckbox.addListener(SWT.Selection, new Listener() {
            
            public void handleEvent(Event event) {
                if (event.widget == createTemplateInJavaFolderCheckbox) {
                    boolean selected = createTemplateInJavaFolderCheckbox.getSelection();
                    templateFragmentRootTextField.setEnabled(!selected);
                }
            } 
        });
        setupLoadedPreferences();
        return parent;
    }

    /**
     * 
     */
    protected void setupLoadedPreferences() {
        String selectedFileExtension = Preferences.getTemplateFileExtension();
        String templateFragmentRootPath = Preferences.getTemplateFragmentRootPath();
        Boolean isCreateTemplateInJavaFolder = Preferences.isCreateTemplateInJavaFolder();
        selectComboFileExtension(selectedFileExtension);
        templateFragmentRootTextField.setText(templateFragmentRootPath);
        createTemplateInJavaFolderCheckbox.setSelection(isCreateTemplateInJavaFolder);
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    @Override
    protected void performDefaults() {
        super.performDefaults();
        String defaultFileExtension = getPreferenceStore().getDefaultString(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY);
        String defaultTemplateFragmentRootPath = getPreferenceStore().getDefaultString(LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY);
        boolean isCreateTemplateInJavaFolder = getPreferenceStore().getDefaultBoolean(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY);
        selectComboFileExtension(defaultFileExtension);
        templateFragmentRootTextField.setText(defaultTemplateFragmentRootPath);
        createTemplateInJavaFolderCheckbox.setSelection(isCreateTemplateInJavaFolder);
    }

    /**
     * @param selectedFileExtension
     */
    private void selectComboFileExtension(String selectedFileExtension) {
        for (int i = 0; i < fileExtensions.length; i++) {
            if (fileExtensions[i].equals(selectedFileExtension)) {
                fileExtensionCombo.select(i);
                break;
            }
        }
    }

    /**
     * @see org.eclipse.jface.preference.PreferencePage#performOk()
     */
    @Override
    public boolean performOk() {
        String selectedItem = fileExtensionCombo.getItem(fileExtensionCombo.getSelectionIndex());
        getPreferenceStore().setValue(LoomConstants.TEMPLATE_FILE_EXTENSION_KEY, selectedItem);
        getPreferenceStore().setValue(LoomConstants.CREATE_TEMPLATE_IN_JAVA_FOLDER_KEY, createTemplateInJavaFolderCheckbox.getSelection());
        getPreferenceStore().setValue(LoomConstants.TEMPLATE_PACKAGE_FRAGMENT_ROOT_KEY, templateFragmentRootTextField.getText());
        EditorFileOpener.getInstance().setTemplateFileExtension(selectedItem); 
        return super.performOk();
    }

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench) {
    }
}
