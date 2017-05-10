/*******************************************************************************
 * This file is part of the Symfony eclipse plugin.
 * 
 * (c) Robert Gruendler <r.gruendler@gmail.com>
 * 
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.dubture.symfony.core.index;

import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.index2.IIndexingRequestor;
import org.eclipse.php.internal.core.index.PHPIndexingParser;

import com.dubture.symfony.core.builder.SymfonyBuildParticipant;

/**
 * 
 * Can eventually be uses to natively parse xml/yml config
 * files.
 * 
 * Does nothing currently though...
 * 
 * XML/Yaml parsing currently takes place in the
 * {@link SymfonyBuildParticipant}
 * 
 * @author Robert Gruendler <r.gruendler@gmail.com>
 *
 */
@SuppressWarnings("restriction")
public class SymfonyIndexingParser extends PHPIndexingParser {
	
	@Override
	public void parseSourceModule(ISourceModule module,
			IIndexingRequestor requestor) {

		super.parseSourceModule(module, requestor);
		
	}

}
