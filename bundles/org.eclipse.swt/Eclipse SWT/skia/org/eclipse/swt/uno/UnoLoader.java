package org.eclipse.swt.uno;

import java.lang.RuntimeException;

import com.sun.star.awt.*;
import com.sun.star.beans.*;
import com.sun.star.comp.helper.*;
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

	public static XWindow createWindow() {

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

		com.sun.star.awt.XWindowPeer xPeer = xToolkit.createWindow(aDescriptor);
		com.sun.star.awt.XWindow w = (com.sun.star.awt.XWindow) UnoRuntime
				.queryInterface(com.sun.star.awt.XWindow.class, xPeer);

		w.setVisible(true);


		XPropertySet prop = qi(XPropertySet.class, w);



		// Konvertieren Sie das Fenster in einen Controller
		XController xController = UnoRuntime.queryInterface(XController.class, w);

		// Erhalten Sie das XModel vom XController


		return w;
	}

	public static org.eclipse.swt.graphics.Point GetScreenSize() {
//		com.sun.star.awt.Rectangle posSize = xDesktop.getCurrentFrame().getContainerWindow().getPosSize();
		return new org.eclipse.swt.graphics.Point(1920//posSize.Width
												, 1080//posSize.Height
					);
	}
}