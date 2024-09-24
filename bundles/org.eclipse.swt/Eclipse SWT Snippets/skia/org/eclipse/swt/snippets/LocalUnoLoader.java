package org.eclipse.swt.snippets;

import java.lang.RuntimeException;

import com.sun.star.awt.*;
import com.sun.star.comp.helper.*;
import com.sun.star.frame.*;
import com.sun.star.lang.*;
import com.sun.star.uno.*;
import com.sun.star.uno.Exception;

public class LocalUnoLoader {

	private static boolean loaded = false;

	static XComponentContext xContext;
	static XMultiComponentFactory xMCF;
	static XMultiServiceFactory xMSF;
	static XToolkit xToolkit;
	static XDesktop xDesktop;

	private static <T> T qi(Class<T> aType, Object o) {
		return UnoRuntime.queryInterface(aType, o);
	}

	static {
		LocalUnoLoader.init();
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
		return new org.eclipse.swt.graphics.Point(xToolkit.getWorkArea().Width
												, xToolkit.getWorkArea().Height
					);
	}
}