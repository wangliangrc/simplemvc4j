package com.clark.app;

import static com.clark.mvc.MultiCore.MAIN_CORE;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.clark.mvc.UIWorker;

public class Main {

    public static void main(String[] args) {
        final Display display = Display.getDefault();
        MAIN_CORE.setUIWorker(new UIWorker() {

            @Override
            public void postTask(Runnable task) {
                display.asyncExec(task);
            }
        });

        Shell shell = new Shell();
        shell.setSize(480, 320);
        shell.setBackground(new Color(display, new RGB(0, 0, 0)));

        FormLayout layout = new FormLayout();
        layout.marginWidth = 3;
        layout.marginHeight = 3;
        shell.setLayout(layout);

        Button button = new Button(shell, SWT.PUSH);
        button.setText("测试按钮");
        button.setBackground(new Color(display, 255, 0, 0));
        FormData data = new FormData();
        // data.left = new FormAttachment(50, 0);
        button.setLayoutData(data);

        Button button2 = new Button(shell, SWT.PUSH);
        button2.setText("测试按钮2");
        button2.setBackground(new Color(display, 0, 0, 255));
        FormData data2 = new FormData();
        data2.left = new FormAttachment(button);
        // data2.top = new FormAttachment(button);
        button2.setLayoutData(data2);

        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
        display.dispose();
    }

}
