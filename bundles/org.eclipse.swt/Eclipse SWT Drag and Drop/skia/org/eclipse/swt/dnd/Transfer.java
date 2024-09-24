package org.eclipse.swt.dnd;

public abstract class Transfer {


	abstract protected void javaToNative (Object object, TransferData transferData);

	abstract protected Object nativeToJava(TransferData transferData);

}
