package org.eclipse.rwt.widgets.upload.demo;

import java.io.IOException;

import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.rwt.lifecycle.IEntryPoint;
import org.eclipse.rwt.widgets.Upload;
import org.eclipse.rwt.widgets.UploadAdapter;
import org.eclipse.rwt.widgets.UploadEvent;
import org.eclipse.rwt.widgets.UploadItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class EntryPoint implements IEntryPoint{

    private Composite uploadContainer;
    private Composite styleComp;
    private boolean enabled = true;
    private Upload upload;
    protected Label uploadPathLabel;
    private Label uploadProgressLabel;
    protected String uploadBtnText = "Upload";
    protected String browseBtnText = "Browse";

    public EntryPoint(){
    }

    public int createUI(){
        Display display = PlatformUI.createDisplay();
        
        final Shell mainShell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX);
        mainShell.setLayout(new FillLayout());
        mainShell.setText("Upload test suite");
        
        createContent(mainShell);
        
        mainShell.addShellListener(new ShellAdapter() {


            public void shellClosed(ShellEvent e){
                mainShell.dispose();
            }
        });
        
        mainShell.setBounds(100, 50, 600, 400);
        mainShell.open();
        while( !mainShell.isDisposed() ) {
          if( !display.readAndDispatch() ) {
            display.sleep();
          }
        }
        
        return 0;
    }

    private void createContent(final Composite parent){
        Composite container = new Composite(parent, SWT.NONE);
        
        container.setLayout(new GridLayout(2, false));
        
        // Container for upload widget
        this.uploadContainer = new Composite(container, SWT.BORDER);
        this.uploadContainer.setLayout(new RowLayout(SWT.VERTICAL));
        GridDataFactory.fillDefaults().grab(true, true).applyTo(this.uploadContainer);
        
        // Container for Widget settings
        this.styleComp = new Composite(container, SWT.BORDER);
        this.styleComp.setLayout(new RowLayout(SWT.VERTICAL));
        GridDataFactory.fillDefaults().applyTo(this.styleComp);
        
       
        createStyleButton("Border", SWT.BORDER, true);
        createEnablementButton();
        createUploadFlagButton("Show progress", Upload.SHOW_PROGRESS, false);
        createUploadFlagButton("Show upload button", Upload.SHOW_UPLOAD_BUTTON, false);
        createUploadFlagButton("Fire progress events", Upload.FIRE_PROGRESS_EVENTS, false);
        
        new Label(this.styleComp, SWT.HORIZONTAL | SWT.SEPARATOR);
        
        createNew();
        
        
        final Button btnStartUpload = new Button(this.styleComp, SWT.PUSH);
        btnStartUpload.setText("Upload file");
        btnStartUpload.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e){
                EntryPoint.this.upload.performUpload();
            }
        });
        
        
        final Button btnChangeBrowseBtn = new Button(this.styleComp, SWT.PUSH);
        btnChangeBrowseBtn.setText("Change browse button text");
        btnChangeBrowseBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e){
                InputDialog dlg = new InputDialog(btnChangeBrowseBtn.getShell(), "Enter new text", "Enter new text for browse button", EntryPoint.this.browseBtnText, null);
                if (dlg.open() == InputDialog.OK) {
                    EntryPoint.this.browseBtnText = dlg.getValue();
                    EntryPoint.this.upload.setBrowseButtonText(EntryPoint.this.browseBtnText);
                }
            }
        });
        
        final Button btnChangeUploadBtn = new Button(this.styleComp, SWT.PUSH);
        btnChangeUploadBtn.setText("Change upload button text");
        btnChangeUploadBtn.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e){
                InputDialog dlg = new InputDialog(btnChangeUploadBtn.getShell(), "Enter new text", "Enter new text for upload button (if visible)", EntryPoint.this.uploadBtnText, null);
                if (dlg.open() == InputDialog.OK) {
                    EntryPoint.this.uploadBtnText = dlg.getValue();
                    EntryPoint.this.upload.setUploadButtonText(EntryPoint.this.uploadBtnText);
                }
            }
        });
        
        new Label(this.styleComp, SWT.HORIZONTAL | SWT.SEPARATOR);
        
        this.uploadPathLabel = new Label(this.styleComp, SWT.NONE);
        this.uploadProgressLabel = new Label(this.styleComp, SWT.NONE);
        
        
    }

    
    private void handleUploadFinished(final Upload upload) {
        final String lastFileUploaded = upload.getLastFileUploaded();
        UploadItem uploadItem = upload.getUploadItem();
        
        System.out.println("filename     : " + uploadItem.getFileName());
        System.out.println("path         : " + uploadItem.getFilePath());
        System.out.println("content-type : " + uploadItem.getContentType());
        System.out.println("stream       : " + uploadItem.getFileInputStream());
        
        MessageDialog.openInformation(upload.getShell(), "File uploaded", lastFileUploaded);
        try {
            if (uploadItem.getFileInputStream() != null) {
				uploadItem.getFileInputStream().close();
			}
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    protected Button createStyleButton(final String name, final int style, final boolean checked){
        Button button = new Button(this.styleComp, SWT.CHECK);
        button.setText(name);
        button.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(final SelectionEvent event){
                createNew();
            }
        });
        button.setData("style", new Integer(style));
        button.setSelection(checked);
        return button;
    }

    protected Button createUploadFlagButton(final String name, final int style, final boolean checked){
        Button button = new Button(this.styleComp, SWT.CHECK);
        button.setText(name);
        button.addSelectionListener(new SelectionAdapter(){
            public void widgetSelected(final SelectionEvent event){
                createNew();
            }
        });
        button.setData("flags", new Integer(style));
        button.setSelection(checked);
        return button;
    }
    
    protected int getStyle() {
        int result = SWT.NONE;
        Control[] ctrls = this.styleComp.getChildren();
        if( ctrls.length == 0 ) {
          result = SWT.NONE;
        } else {
          for( int i = 0; i < ctrls.length; i++ ) {
            if( ctrls[ i ] instanceof Button ) {
              Button button = ( Button )ctrls[ i ];
              if (button.getSelection()) {
                Object data = button.getData( "style" );
                if( data != null && data instanceof Integer ) {
                  int style = (( Integer )data).intValue();
                  result |= style;
                }
              }
            }
          }
        }
        return result;
      }

    protected int getUploadFlags() {
        int result = SWT.NONE;
        Control[] ctrls = this.styleComp.getChildren();
        if( ctrls.length == 0 ) {
          result = SWT.NONE;
        } else {
          for( int i = 0; i < ctrls.length; i++ ) {
            if( ctrls[ i ] instanceof Button ) {
              Button button = ( Button )ctrls[ i ];
              if (button.getSelection()) {
                Object data = button.getData( "flags" );
                if( data != null && data instanceof Integer ) {
                  int style = (( Integer )data).intValue();
                  result |= style;
                }
              }
            }
          }
        }
        return result;
      }
    
    
    protected void createNew(){
        
        Control[] controls = this.uploadContainer.getChildren();
        for( int i = 0; i < controls.length; i++ ) {
          controls[ i ].dispose();
        }
        
        
        this.upload = new Upload(this.uploadContainer, getStyle(), getUploadFlags());
        this.upload.setBrowseButtonText(this.browseBtnText);
        this.upload.setUploadButtonText(this.uploadBtnText);
        
        this.upload.addModifyListener(new ModifyListener() {


            public void modifyText(ModifyEvent event){
                EntryPoint.this.uploadPathLabel.setText(EntryPoint.this.upload.getPath());
                EntryPoint.this.uploadPathLabel.getParent().layout();
            }
            
        });
        
        this.upload.addUploadListener(new UploadAdapter() {

            public void uploadFinished(UploadEvent uploadEvent ){
                EntryPoint.this.uploadProgressLabel.setText("upload finished");
                EntryPoint.this.uploadProgressLabel.getParent().layout();
                handleUploadFinished(EntryPoint.this.upload);
            }

            public void uploadInProgress(UploadEvent uploadEvent){
                int percent = (int) ((float)uploadEvent.getUploadedParcial() / (float)uploadEvent.getUploadedTotal() * 100);
                EntryPoint.this.uploadProgressLabel.setText(String.valueOf(percent) + " %");
                EntryPoint.this.uploadProgressLabel.getParent().layout();
            }
        });

        
        this.uploadContainer.layout();
    }

    protected Button createEnablementButton( ) {
        final Button button = new Button( this.styleComp, SWT.CHECK );
        button.setText( "Enabled" );
        button.setSelection( this.enabled );
        button.addSelectionListener( new SelectionAdapter() {
        public void widgetSelected( final SelectionEvent event ) {
            EntryPoint.this.enabled = button.getSelection();
            updateEnabled();
          }
        } );
        return button;
      }


    private void updateEnabled(){
        Control[] controls = this.uploadContainer.getChildren();
        for (int i = 0; i < controls.length; i++) {
            controls[i].setEnabled(this.enabled);
        }
    }
}
