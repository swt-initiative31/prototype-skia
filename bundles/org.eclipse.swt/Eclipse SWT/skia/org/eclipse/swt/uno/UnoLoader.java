package org.eclipse.swt.uno;

import java.lang.RuntimeException;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.comp.helper.*;
import com.sun.star.container.*;
import com.sun.star.frame.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;
import com.sun.star.uno.Exception;

public class UnoLoader {

	private static boolean loaded = false;

	 static XComponentContext xContext;
	 static XMultiComponentFactory xMCF;
	 static XMultiServiceFactory xMSF;
	 static XToolkit xToolkit;
	 static XDesktop xDesktop;

	 private static XControlContainer m_xDlgContainer;

	public static <T> T qi(Class<T> aType, Object o) {
		return UnoRuntime.queryInterface(aType, o);
	}

	public static synchronized void init() {

		if (!loaded) {
			try {
				xContext = com.sun.star.comp.helper.Bootstrap.bootstrap();
				if (xContext != null)
					System.out.println("Connected to a running office ...");
				xMCF = xContext.getServiceManager();

				Object oToolkit = xMCF.createInstanceWithContext("com.sun.star.awt.Toolkit", xContext);
				xToolkit = qi(XToolkit.class, oToolkit);

				Object oDesktop = xMCF.createInstanceWithContext("com.sun.star.frame.Desktop", xContext);
				xDesktop = qi(XDesktop.class, oDesktop);

//				String sUrl = "private:factory/swriter";
//
//				// Gegebenes ComponentLoader-Objekt
//				XComponentLoader xComponentLoader = (XComponentLoader)UnoRuntime.queryInterface(XComponentLoader.class,
//				      xDesktop);
//
//
//				// Lade Dokument mit leerem Controller
//				XComponent xComponent = xComponentLoader.loadComponentFromURL(sUrl, "_black", 0, new PropertyValue[0]);
//
//				// Hole das XModel Interface
//				XModel xModel = (XModel)UnoRuntime.queryInterface(XModel.class, xComponent);



			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			} catch (BootstrapException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		loaded = true;
	}

	public static void terminate() {
		xDesktop.terminate();
	}

	public static org.eclipse.swt.graphics.Point GetScreenSize() {
//		com.sun.star.awt.Rectangle posSize = xDesktop.getCurrentFrame().getContainerWindow().getPosSize();
		return new org.eclipse.swt.graphics.Point(1920//posSize.Width
												, 1080//posSize.Height
					);
	}

	public static XButton createButton() {
//		// create the dialog...
//		Object oUnoDialog;
//		try {
//			oUnoDialog = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlDialog", xContext);
//
//			Object oDialogModel = xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlDialogModel", xContext);
//			// The XMultiServiceFactory of the dialogmodel is needed to instantiate the
//			// controls...
//			XMultiServiceFactory m_xMSFDialogModel = UnoRuntime.queryInterface(XMultiServiceFactory.class,
//					oDialogModel);
//
//			// The named container is used to insert the created controls into...
//			XNameContainer m_xDlgModelNameContainer = UnoRuntime.queryInterface(XNameContainer.class, oDialogModel);
//
//			// The scope of the control container is private...
//			m_xDlgContainer = UnoRuntime.queryInterface(XControlContainer.class, oUnoDialog);
//			String sName = "CommandButton";
//			// create a controlmodel at the multiservicefactory of the dialog model...
//			Object oButtonModel = m_xMSFDialogModel.createInstance("com.sun.star.awt.UnoControlButtonModel");
//			// Set the properties at the model - keep in mind to pass the property names in
//			// alphabetical order!
//			XMultiPropertySet xButtonMPSet = UnoRuntime.queryInterface(XMultiPropertySet.class, oButtonModel);
//			// Set the properties at the model - keep in mind to pass the property names in
//			// alphabetical order!
//			xButtonMPSet.setPropertyValues(
//					new String[] { "Height", "Label", "Name", "PositionX", "PositionY", "PushButtonType", "Width" },
//					new Object[] { Integer.valueOf(14), "Hola", sName, Integer.valueOf(10),
//							Integer.valueOf(10), Short.valueOf((short) PushButtonType.OK_value), Integer.valueOf(55) });
//
//			// add the model to the NameContainer of the dialog model
//			m_xDlgModelNameContainer.insertByName(sName, oButtonModel);
//			XControl xButtonControl = m_xDlgContainer.getControl(sName);
//			return qi(XButton.class, xButtonControl);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		UnoDialogSampleSmall doIt = UnoDialogSampleSmall.doIt(null);
		return doIt.xButton;
	}

	private static class UnoDialogSampleSmall implements XActionListener {
		private XComponentContext m_xContext = null;
		private com.sun.star.lang.XMultiComponentFactory m_xMCF;
		private XMultiServiceFactory m_xMSFDialogModel;
		private XNameContainer m_xDlgModelNameContainer;
		private XControlContainer m_xDlgContainer;
		private XControl m_xDialogControl;
		private XDialog xDialog;

		private XWindowPeer m_xWindowPeer = null;

		private XComponent m_xComponent = null;
		private XButton xButton;

		/**
		 * Creates a new instance of UnoDialogSample
		 */
		private UnoDialogSampleSmall(XComponentContext _xContext, XMultiComponentFactory _xMCF) {
			m_xContext = _xContext;
			m_xMCF = _xMCF;
//			createDialog(m_xMCF);
//			initialize(
//					new String[] { "Height", "Moveable", "Name", "PositionX", "PositionY", "Step", "TabIndex", "Title",
//							"Width" },
//					new Object[] { Integer.valueOf(380), Boolean.TRUE, "MyTestDialog", Integer.valueOf(102),
//							Integer.valueOf(41), Integer.valueOf(0), Short.valueOf((short) 0), "OpenOffice",
//							Integer.valueOf(380) });
		}

		public static UnoDialogSampleSmall doIt(String args[]) {
			UnoDialogSampleSmall oUnoDialogSample = null;

			try {
				XComponentContext xContext = com.sun.star.comp.helper.Bootstrap.bootstrap();
				if (xContext != null) {
					System.out.println("Connected to a running office ...");
				}
				XMultiComponentFactory xMCF = xContext.getServiceManager();
				oUnoDialogSample = new UnoDialogSampleSmall(xContext, xMCF);


				oUnoDialogSample.insertButton(oUnoDialogSample, 106, 320, 50, "~Close dialog",
						(short) PushButtonType.OK_value);

//				oUnoDialogSample.executeDialog();
				return oUnoDialogSample;
			} catch (BootstrapException e) {
				System.err.println(e + e.getMessage());
				e.printStackTrace();
			} finally {
				// make sure always to dispose the component and free the memory!
				if (oUnoDialogSample != null) {
					if (oUnoDialogSample.m_xComponent != null) {
						oUnoDialogSample.m_xComponent.dispose();
					}
				}
			}

			return null;
		}

		private void createDialog(XMultiComponentFactory _xMCF) {
			try {
				Object oDialogModel = _xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlDialogModel", m_xContext);

				// The XMultiServiceFactory of the dialogmodel is needed to instantiate the
				// controls...
				m_xMSFDialogModel = UnoRuntime.queryInterface(XMultiServiceFactory.class, oDialogModel);

				// The named container is used to insert the created controls into...
				m_xDlgModelNameContainer = UnoRuntime.queryInterface(XNameContainer.class, oDialogModel);

				// create the dialog...
				Object oUnoDialog = _xMCF.createInstanceWithContext("com.sun.star.awt.UnoControlDialog", m_xContext);
				m_xDialogControl = UnoRuntime.queryInterface(XControl.class, oUnoDialog);

				// The scope of the control container is private...
				m_xDlgContainer = UnoRuntime.queryInterface(XControlContainer.class, oUnoDialog);

				UnoRuntime.queryInterface(XTopWindow.class, m_xDlgContainer);

				// link the dialog and its model...
				XControlModel xControlModel = UnoRuntime.queryInterface(XControlModel.class, oDialogModel);
				m_xDialogControl.setModel(xControlModel);
			} catch (com.sun.star.uno.Exception exception) {
				exception.printStackTrace(System.err);
			}
		}

		private short executeDialog() {
			if (m_xWindowPeer == null) {
				createWindowPeer();
			}
			xDialog = UnoRuntime.queryInterface(XDialog.class, m_xDialogControl);
			m_xComponent = UnoRuntime.queryInterface(XComponent.class, m_xDialogControl);
			return xDialog.execute();
		}

		private void initialize(String[] PropertyNames, Object[] PropertyValues) {
			try {
				XMultiPropertySet xMultiPropertySet = UnoRuntime.queryInterface(XMultiPropertySet.class,
						m_xDlgModelNameContainer);
				xMultiPropertySet.setPropertyValues(PropertyNames, PropertyValues);
			} catch (com.sun.star.uno.Exception ex) {
				ex.printStackTrace(System.err);
			}
		}

		/**
		 * create a peer for this dialog, using the given peer as a parent.
		 */
		private XWindowPeer createWindowPeer(XWindowPeer _xWindowParentPeer) {
			try {
				if (_xWindowParentPeer == null) {
					XWindow xWindow = UnoRuntime.queryInterface(XWindow.class, m_xDlgContainer);
					xWindow.setVisible(false);
					Object tk = m_xMCF.createInstanceWithContext("com.sun.star.awt.Toolkit", m_xContext);
					XToolkit xToolkit = UnoRuntime.queryInterface(XToolkit.class, tk);
					UnoRuntime.queryInterface(XReschedule.class, xToolkit);
					m_xDialogControl.createPeer(xToolkit, _xWindowParentPeer);
					m_xWindowPeer = m_xDialogControl.getPeer();
					return m_xWindowPeer;
				}
			} catch (com.sun.star.uno.Exception exception) {
				exception.printStackTrace(System.err);
			}
			return null;
		}

		/**
		 * Creates a peer for this dialog, using the active OO frame as the parent
		 * window.
		 */
		private XWindowPeer createWindowPeer() {
			return createWindowPeer(null);
		}

		/**
		 * makes a String unique by appending a numerical suffix
		 *
		 * @param _xElementContainer the com.sun.star.container.XNameAccess container
		 *                           that the new Element is going to be inserted to
		 * @param _sElementName      the StemName of the Element
		 */
		private static String createUniqueName(XNameAccess _xElementContainer, String _sElementName) {
			boolean bElementexists = true;
			int i = 1;
			String BaseName = _sElementName;
			while (bElementexists) {
				bElementexists = _xElementContainer.hasByName(_sElementName);
				if (bElementexists) {
					i += 1;
					_sElementName = BaseName + Integer.toString(i);
				}
			}
			return _sElementName;
		}

		private XButton insertButton(XActionListener _xActionListener, int _nPosX, int _nPosY, int _nWidth, String _sLabel,
				short _nPushButtonType) {
			xButton = null;
			try {
				// create a unique name by means of an own implementation...
				String sName = "CommandButton"; // createUniqueName(m_xDlgModelNameContainer, "CommandButton");

				// create a controlmodel at the multiservicefactory of the dialog model...
				Object oButtonModel = m_xMSFDialogModel.createInstance("com.sun.star.awt.UnoControlButtonModel");
				XMultiPropertySet xButtonMPSet = UnoRuntime.queryInterface(XMultiPropertySet.class, oButtonModel);
				// Set the properties at the model - keep in mind to pass the property names in
				// alphabetical order!
				xButtonMPSet.setPropertyValues(
						new String[] { "Height", "Label", "Name", "PositionX", "PositionY", "PushButtonType", "Width" },
						new Object[] { Integer.valueOf(14), _sLabel, sName, Integer.valueOf(_nPosX),
								Integer.valueOf(_nPosY), Short.valueOf(_nPushButtonType), Integer.valueOf(_nWidth) });

				// add the model to the NameContainer of the dialog model
				m_xDlgModelNameContainer.insertByName(sName, oButtonModel);
				XControl xButtonControl = m_xDlgContainer.getControl(sName);
				xButton = UnoRuntime.queryInterface(XButton.class, xButtonControl);
				// An ActionListener will be notified on the activation of the button...
				xButton.addActionListener(_xActionListener);
			} catch (com.sun.star.uno.Exception ex) {
				/*
				 * perform individual exception handling here. Possible exception types are:
				 * com.sun.star.lang.IllegalArgumentException,
				 * com.sun.star.lang.WrappedTargetException,
				 * com.sun.star.container.ElementExistException,
				 * com.sun.star.beans.PropertyVetoException,
				 * com.sun.star.beans.UnknownPropertyException, com.sun.star.uno.Exception
				 */
				ex.printStackTrace(System.err);
			}
			return xButton;
		}

		@Override
		public void disposing(EventObject rEventObject) {

		}

		@Override
		public void actionPerformed(ActionEvent rEvent) {
			try {
				// get the control that has fired the event,
				XControl xControl = UnoRuntime.queryInterface(XControl.class, rEvent.Source);
				XControlModel xControlModel = xControl.getModel();
				XPropertySet xPSet = UnoRuntime.queryInterface(XPropertySet.class, xControlModel);
				String sName = (String) xPSet.getPropertyValue("Name");
				// just in case the listener has been added to several controls,
				// we make sure we refer to the right one
				if (sName.equals("CommandButton1")) {

				}
			} catch (com.sun.star.uno.Exception ex) {
				/*
				 * perform individual exception handling here. Possible exception types are:
				 * com.sun.star.lang.WrappedTargetException,
				 * com.sun.star.beans.UnknownPropertyException, com.sun.star.uno.Exception
				 */
				ex.printStackTrace(System.err);
			}
		}

	}
}