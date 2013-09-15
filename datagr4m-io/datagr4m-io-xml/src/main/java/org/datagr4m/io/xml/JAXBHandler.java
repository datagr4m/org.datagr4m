package org.datagr4m.io.xml;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamSource;

/**
 * A {@link JAXBHandler} is a utility for easily reading and writing XML files
 * into or from a Jaxb generated class file.
 * 
 * @author Martin Pernollet
 */
public class JAXBHandler {
	public static JAXBHandler xmlHandlerTopology() {
		return new JAXBHandler("org.datagr4m.io.xml.generated.topology");
	}
	public static JAXBHandler xmlHandlerLayout() {
		return new JAXBHandler("org.datagr4m.io.xml.generated.layout");
	}
	public static JAXBHandler xmlHandlerProjection() {
		return new JAXBHandler("org.datagr4m.io.xml.generated.dataprism");
	}

	/** Expect a package name or a list of package name */
	public JAXBHandler(String packge) {
		initJaxb(packge);
	}

	/**
	 * Expect a root and package name (e.g. "com.netlight.io.xml.generated", and
	 * "settings")
	 */
	public JAXBHandler(String root, String packge) {
		initJaxb(root + "." + packge);
	}

	/*
	 * public JAXBHandler(String root, List<String> packs){ //StringBuffer sb =
	 * new StringBuf
	 * 
	 * 
	 * //initJaxb(root + "." + pack); }
	 */

	protected void initJaxb(String packages) {
		try {
			context = JAXBContext.newInstance(packages);
			unmarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
			// m.setProperty("com.sun.xml.bind.xmlDeclaration", Boolean.FALSE);
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			// System.out.println("jaxb init:\n" + packages);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

	public <T> T unmarshall(String file, Class<T> clazz) throws JAXBException {
		return unmarshall(new StreamSource(new File(file)), clazz);
	}

	public <T> T unmarshall(StreamSource source, Class<T> clazz)
			throws JAXBException {
		JAXBElement<T> root = unmarshaller.unmarshal(source, clazz);
		return root.getValue();
	}

	public java.lang.Object unmarshall(String file) throws Exception {
		JAXBElement<?> jElem;
		// java.lang.Object o = unmarshaller.unmarshal(new File(file));
		StringReader reader = new java.io.StringReader(readAsString(file));
		Object o = unmarshaller.unmarshal(reader);

		if (o instanceof JAXBElement<?>) {
			jElem = (JAXBElement<?>) o;
			return jElem.getValue();
		} else {
			return o;
		}
	}

	public java.lang.Object unmarshall(File file) throws Exception {
		JAXBElement<?> jElem;
		java.lang.Object o = unmarshaller.unmarshal(file);

		if (o instanceof JAXBElement<?>) {
			jElem = (JAXBElement<?>) o;
			return jElem.getValue();
		} else {
			return o;
		}
	}

	public void marshall(java.lang.Object obj, String file)
			throws JAXBException, FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(file);
		marshaller.marshal(obj, fos);
	}

	public String marshall(java.lang.Object obj) throws JAXBException {
		return marshall(true);
	}

	public String marshall(java.lang.Object obj, boolean format)
			throws JAXBException {
		StringWriter writer = new StringWriter();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, format);
		marshaller.marshal(obj, writer);
		return writer.toString();
	}

	// @SuppressWarnings("unchecked")
	public void rescueMarshall(java.lang.Object obj, String file)
			throws JAXBException, FileNotFoundException {
		FileOutputStream fos = new FileOutputStream(file);
		JAXBElement elem = new JAXBElement(new QName("", ""), obj.getClass(),
				obj);
		marshaller.marshal(elem, fos);
	}

	// JAXB utilities
	protected JAXBContext context;
	protected Unmarshaller unmarshaller;
	protected Marshaller marshaller;

	public static String readAsString(String filename) throws Exception {
		StringBuffer sb = new StringBuffer();
		File file = new File(filename);
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream bis = new BufferedInputStream(fis);
		DataInputStream dis;
		for (dis = new DataInputStream(bis); dis.available() != 0; sb
				.append((new StringBuilder()).append(dis.readLine())
						.append("\n").toString()))
			;
		fis.close();
		bis.close();
		dis.close();
		return sb.toString();
	}
}
