package org.eclipse.swt.uno;

import com.sun.star.awt.*;
import com.sun.star.lang.*;

public class UnoWindow extends UnoScrollableControl {

	public UnoWindow() {
		super();
//		createDialog(UnoLoader.xMCF);
		createWindow();

		w.addWindowListener(new XWindowListener() {

			@Override
			public void disposing(EventObject arg0) {
				System.out.println("dispose");
				disposed = true;

			}

			@Override
			public void windowShown(EventObject arg0) {
				System.out.println("windowShown");
			}

			@Override
			public void windowResized(WindowEvent arg0) {
				System.out.println("windowResized");

			}

			@Override
			public void windowMoved(WindowEvent arg0) {

				System.out.println("windowMoved");

			}

			@Override
			public void windowHidden(EventObject arg0) {
				System.out.println("windowHidden");

			}
		});


		tw.addTopWindowListener(new XTopWindowListener() {

			@Override
			public void disposing(EventObject arg0) {
				System.out.println("disposing");

			}

			@Override
			public void windowOpened(EventObject arg0) {
				System.out.println("windowOpened");

			}

			@Override
			public void windowNormalized(EventObject arg0) {
				System.out.println("windowNormalized");

			}

			@Override
			public void windowMinimized(EventObject arg0) {
				System.out.println("windowMinimized");

			}

			@Override
			public void windowDeactivated(EventObject arg0) {
				System.out.println("windowDeactivated");

			}

			@Override
			public void windowClosing(EventObject arg0) {
				System.out.println("windowClosing");
				dispose();
//				UnoLoader.terminate();
			}

			@Override
			public void windowClosed(EventObject arg0) {
				System.out.println("windowClose");

			}

			@Override
			public void windowActivated(EventObject arg0) {
				System.out.println("windowActivated");

			}
		});

	}

	// This is most probably wrong. In UNO there isn't something like a content pane
	public UnoWindow getContentPane() {
		return this;
	}

	private void createWindow() {
		// Describe the properties of the container window.
		// Tip: It is possible to use native window handle of a java window
		// as parent for this. see chapter "OfficeBean" for further informations
		com.sun.star.awt.WindowDescriptor aDescriptor = new com.sun.star.awt.WindowDescriptor();

		aDescriptor.Type = com.sun.star.awt.WindowClass.TOP;
		aDescriptor.WindowServiceName = "window";
		aDescriptor.ParentIndex = -1;
		aDescriptor.Parent = null;
		aDescriptor.Bounds = new com.sun.star.awt.Rectangle(100, 100, 800, 800);

		aDescriptor.WindowAttributes = com.sun.star.awt.WindowAttribute.BORDER
				| com.sun.star.awt.WindowAttribute.MOVEABLE | com.sun.star.awt.WindowAttribute.SIZEABLE
				| com.sun.star.awt.WindowAttribute.CLOSEABLE;

		XWindowPeer parentPeer = UnoLoader.xToolkit.createWindow(aDescriptor);
		w = qi(com.sun.star.awt.XWindow.class, parentPeer);

		tw = qi(XTopWindow.class, w);
		p = qi(XWindowPeer.class, w);
	}

// Tried to use Dialog instead of a plain window in order to set the title. This did not work, because the method execute()
// method had to be called for the dialog which only returns if the dialog is closed. Therefore, we need to stick to plain
// XWindow until we find a better approach
//
//	private XMultiServiceFactory xMSFDialogModel;
//	private XNameContainer xDlgModelNameContainer;
//	private XControl xDialogControl;
//	private XControlContainer xDlgContainer;
//
//
//    private void createDialog(XMultiComponentFactory _xMCF) {
//        try {
//            Object oDialogModel =  _xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlDialogModel", UnoLoader.xContext);
//
//            // The XMultiServiceFactory of the dialogmodel is needed to instantiate the controls...
//            xMSFDialogModel = qi(XMultiServiceFactory.class, oDialogModel);
//
//            // The named container is used to insert the created controls into...
//            xDlgModelNameContainer = qi(XNameContainer.class, oDialogModel);
//
//            // create the dialog...
//            Object oUnoDialog = _xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlDialog", UnoLoader.xContext);
//            xDialogControl = qi(XControl.class, oUnoDialog);
//
//            // The scope of the control container is public...
//            xDlgContainer = qi(XControlContainer.class, oUnoDialog);
//
//            qi(XTopWindow.class, xDlgContainer);
//
//            // link the dialog and its model...
//            XControlModel xControlModel = qi(XControlModel.class, oDialogModel);
//            xDialogControl.setModel(xControlModel);
//
//            XMultiPropertySet multiPropertySet = qi(XMultiPropertySet.class, xDlgModelNameContainer);
//            multiPropertySet.setPropertyValues( new String[] {"Height", "Moveable", "Name","PositionX","PositionY", "Sizeable", "Step", "TabIndex", "Width"},
//                    new Object[] { Integer.valueOf(400), Boolean.TRUE, "MyTestDialog", Integer.valueOf(0),Integer.valueOf(0), Boolean.TRUE, Integer.valueOf(0), Short.valueOf((short) 0), Integer.valueOf(400)});
//
//            XWindowPeer xWindowParentPeer = UnoLoader.xToolkit.getDesktopWindow();
//            xDialogControl.createPeer(UnoLoader.xToolkit, xWindowParentPeer);
//
//            p = xDialogControl.getPeer();
//			w = qi(XWindow.class, xDlgContainer);
//			tw = qi(XTopWindow.class, w);
//
//			w.setVisible(false);
//
//        } catch (com.sun.star.uno.Exception exception) {
//            exception.printStackTrace(System.err);
//        }
//    }
//
//	public void setTitle(String title)  {
//        XPropertySet propertySet = UnoRuntime.queryInterface(XPropertySet.class, xMSFDialogModel);
//
//        try {
//			propertySet.setPropertyValue("Title", title);
//		} catch (IllegalArgumentException | UnknownPropertyException | PropertyVetoException
//				| WrappedTargetException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	@Override
//	public void setVisible(boolean visible) {
//		if (visible) {
////		    XDialog xDialog = qi(XDialog.class, xDialogControl);
////		    xDialog.execute();
//		}
//		super.setVisible(visible);
//	}

}
