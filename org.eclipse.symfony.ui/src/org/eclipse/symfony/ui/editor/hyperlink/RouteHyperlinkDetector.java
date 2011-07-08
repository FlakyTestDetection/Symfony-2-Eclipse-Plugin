package org.eclipse.symfony.ui.editor.hyperlink;

import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IType;
import org.eclipse.dltk.core.index2.search.ISearchEngine.MatchRule;
import org.eclipse.dltk.core.search.IDLTKSearchScope;
import org.eclipse.dltk.core.search.SearchEngine;
import org.eclipse.dltk.internal.ui.editor.EditorUtility;
import org.eclipse.dltk.internal.ui.editor.ModelElementHyperlink;
import org.eclipse.dltk.ui.actions.OpenAction;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.php.internal.core.model.PhpModelAccess;
import org.eclipse.php.internal.ui.editor.PHPStructuredEditor;
import org.eclipse.symfony.core.log.Logger;
import org.eclipse.symfony.core.model.SymfonyModelAccess;
import org.eclipse.symfony.core.model.ViewPath;
import org.eclipse.symfony.index.dao.Route;

/**
 * 
 * {@link RouteHyperlinkDetector} detects routes and links them
 * to the corresponding action.
 * 
 * 
 * 
 * @author "Robert Gruendler <r.gruendler@gmail.com>"
 *
 */
@SuppressWarnings("restriction")
public class RouteHyperlinkDetector extends StringHyperlinkDetector {

	public RouteHyperlinkDetector() {

	}
	
	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer,
			IRegion region, boolean canShowMultipleHyperlinks) {

		final PHPStructuredEditor editor = org.eclipse.php.internal.ui.util.EditorUtility
				.getPHPEditor(textViewer);
		

		if (editor == null) {
			return null;
		}

		if (region == null) {
			return null;
		}

		IModelElement input = EditorUtility.getEditorInputModelElement(editor,
				false);
		if (input == null) {
			return null;
		}
		
		IDocument document = textViewer.getDocument();
		int offset = region.getOffset();
		
		try {
			
			IRegion wordRegion = findWord(document, offset);
			
			if (wordRegion == null)
				return null;

			
			String routeName = document.get(wordRegion.getOffset(), wordRegion.getLength());
			Route route = SymfonyModelAccess.getDefault().findRoute(routeName, input.getScriptProject());
			
			if (route == null) {
				return null;
			}
						
			ViewPath path = new ViewPath(route.getViewPath());
			IDLTKSearchScope scope = SearchEngine.createSearchScope(input.getScriptProject());
			IType[] types = PhpModelAccess.getDefault().findTypes(null, path.getController() + "Controller", MatchRule.EXACT, 0, 0, scope, null);
			
			
			// it should only exist 1 single route for each project with this service id
			if (types.length != 1) {
				Logger.debugMSG("No route controller found (" + types.length + ")");
				return null;
			}
			
			IType type = types[0];
			IDLTKSearchScope controllerScope = SearchEngine.createSearchScope(type);
			IMethod[] methods= PhpModelAccess.getDefault().findMethods(path.getTemplate(), MatchRule.PREFIX,	0, 0, controllerScope, null);
			
			if (methods.length == 1) {

				IMethod method = methods[0];
				final IHyperlink link;
				
				link = new ModelElementHyperlink(wordRegion, method,
						new OpenAction(editor));			
				
				return new IHyperlink[] { link };
				
			}			
			
		} catch (Exception e) {			
			Logger.logException(e);
		}
		
		return null;
	}
}