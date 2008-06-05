package org.eclipse.rwt.widgets.upload.demo;

import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

public class EntryPoint implements IEntryPoint{

    public EntryPoint(){
    }

    public int createUI(){
        Display display = PlatformUI.createDisplay();
        
        final Shell mainShell = new Shell(display, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX);
        mainShell.setLayout(new GridLayout(1, false));
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
        Label label;
        label = new Label(parent, SWT.NONE);
        label.setText("File upload widget 1");
        label.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        
        
        createUploadWidget1(parent);
        
        label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
        final GridData gridData = new GridData(SWT.FILL, SWT.NONE, true, false);
        gridData.heightHint = 50;
        label.setLayoutData(gridData);
        
        label = new Label(parent, SWT.NONE);
        label.setText("File upload widget 2");
        label.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        
        createUploadWidget2(parent);
        
        Button btnTest = new Button(parent, SWT.PUSH);
        btnTest.setText("open window");
        btnTest.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false));
        btnTest.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e){
                final Shell newShell = new Shell(parent.getShell(), SWT.DIALOG_TRIM | SWT.RESIZE);
                newShell.setLayout(new GridLayout(1, false));
                final Upload upload = new Upload(newShell, SWT.NONE, SWT.NONE);
                
                final Button btnUpload = new Button(newShell, SWT.PUSH);
                btnUpload.setText("Upload");
                btnUpload.addSelectionListener(new SelectionAdapter() {

                    @Override
                    public void widgetSelected(SelectionEvent e){
                        upload.performUpload();
                        handleUploadFinished(upload);
                        newShell.close();
                    }
                    
                });
                
                newShell.pack();
                newShell.open();
            }
            
        });
        
    }

    private void createUploadWidget1(Composite parent){
        final Upload upload = new Upload(parent, SWT.BORDER, SWT.NONE);
        upload.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        
        final Button btnStartUpload = new Button(parent, SWT.PUSH);
        btnStartUpload.setText("Upload file");
        btnStartUpload.setEnabled(false);
        btnStartUpload.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e){
                upload.performUpload();
        
                handleUploadFinished(upload);
            }
        });
        
        
        upload.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent event){
                if (!"".equals(upload.getPath())) {
                    btnStartUpload.setEnabled(true);
                }
            }
            
        });
        
    }
    
    private void createUploadWidget2(Composite parent){
        
        final Upload upload = new Upload(parent, SWT.NONE, Upload.FIRE_PROGRESS_EVENTS | Upload.SHOW_UPLOAD_BUTTON | Upload.SHOW_PROGRESS);
        upload.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

        final Label uploadProgress = new Label(parent, SWT.NONE);
        uploadProgress.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        
        final Button btnReset = new Button(parent, SWT.NONE);
        btnReset.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false));
        btnReset.setText("Reset upload widget");
        btnReset.addSelectionListener(new SelectionAdapter() {

            @Override
            public void widgetSelected(SelectionEvent e){
                upload.reset();
                uploadProgress.setText("");
            }
            
        });
        
        upload.addUploadListener(new UploadAdapter() {

            public void uploadFinished(UploadEvent uploadEvent ){
                uploadProgress.setText("100 %");
                handleUploadFinished(upload);
            }

            public void uploadInProgress(UploadEvent uploadEvent){
                int percent = (int) ((float)uploadEvent.getUploadedParcial() / (float)uploadEvent.getUploadedTotal() * 100);
                uploadProgress.setText(String.valueOf(percent) + " %");
            }
        });
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
    
}
