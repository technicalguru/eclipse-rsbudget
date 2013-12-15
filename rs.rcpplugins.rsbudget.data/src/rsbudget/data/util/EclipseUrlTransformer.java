/**
 * 
 */
package rsbudget.data.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rs.baselib.io.FileFinder;
import rs.baselib.util.IUrlTransformer;

/**
 * Implements the URL Transformer interface.
 * @author ralph
 *
 */
public class EclipseUrlTransformer implements IUrlTransformer {

	private static final Logger log = LoggerFactory.getLogger(EclipseUrlTransformer.class);
	
	/**
	 * Constructor.
	 */
	public EclipseUrlTransformer() {
	}

	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	@Override
	public URL toURL(String url) throws MalformedURLException {
		try {
			URL rc = FileFinder.find(url);
			rc = FileLocator.toFileURL(rc);
			log.trace("Local URL: "+rc);
			return rc;
		} catch (IOException e) {
			if (e instanceof MalformedURLException) throw (MalformedURLException)e;
			throw new RuntimeException("Cannot create a file URL: ", e);
		}
	}

}
