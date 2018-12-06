// The MIT License (MIT)
//
// Copyright (c) 2015, 2018 Arian Fornaris
//
// Permission is hereby granted, free of charge, to any person obtaining a
// copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to permit
// persons to whom the Software is furnished to do so, subject to the
// following conditions: The above copyright notice and this permission
// notice shall be included in all copies or substantial portions of the
// Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
// OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
// NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
// DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
// OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
// USE OR OTHER DEALINGS IN THE SOFTWARE.
package phasereditor.assetpack.ui.editors;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import phasereditor.assetpack.core.AssetPackModel;
import phasereditor.assetpack.ui.AssetPackUI;
import phasereditor.ui.properties.FormPropertySection;

/**
 * @author arian
 *
 */
public abstract class BaseAssetPackEditorSection<T> extends FormPropertySection<T> {

	private AssetPackEditorPropertyPage _page;

	public BaseAssetPackEditorSection(AssetPackEditorPropertyPage page, String name) {
		super(name);

		_page = page;
	}

	public AssetPackEditorPropertyPage getPage() {
		return _page;
	}

	public AssetPackEditor getEditor() {
		return _page.getEditor();
	}

	protected abstract class AbstractBrowseImageListener extends SelectionAdapter {
		protected String dialogName = "";

		@Override
		public void widgetSelected(SelectionEvent e) {
			try {
				var pack = getEditor().getModel();
				var urlFile = pack.getFileFromUrl(getUrl());
				List<IFile> imageFiles = discoverImages(pack);
				var result = AssetPackUI.browseImageUrl(pack, dialogName, urlFile, imageFiles,
						e.display.getActiveShell());

				if (result != null) {
					setUrl(result);
					update_UI_from_Model();
				}

			} catch (CoreException e1) {
				e1.printStackTrace();
				throw new RuntimeException(e1);
			}
		}

		protected List<IFile> discoverImages(AssetPackModel pack) throws CoreException {
			return pack.discoverImageFiles();
		}

		protected abstract void setUrl(String url);

		protected abstract String getUrl();

	}

	protected abstract class AbstractBrowseFileListener extends SelectionAdapter {
		protected String dialogName = "";

		@Override
		public void widgetSelected(SelectionEvent e) {
			var pack = getEditor().getModel();

			IFile urlFile = pack.getFileFromUrl(getUrl());

			try {
				var files = discoverFiles(pack);

				String result = AssetPackUI.browseAssetFile(pack, dialogName, urlFile, files,
						getEditor().getEditorSite().getShell(), null);

				if (result != null) {
					setUrl(result);
				}
			} catch (CoreException e1) {
				e1.printStackTrace();
			}
		}

		protected abstract List<IFile> discoverFiles(AssetPackModel pack) throws CoreException;

		protected abstract void setUrl(String url);

		protected abstract String getUrl();

	}
}
