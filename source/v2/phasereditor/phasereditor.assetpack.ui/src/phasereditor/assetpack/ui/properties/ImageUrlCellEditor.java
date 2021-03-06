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
package phasereditor.assetpack.ui.properties;

import java.util.List;
import java.util.function.Function;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import phasereditor.assetpack.core.AssetModel;
import phasereditor.assetpack.core.AssetPackModel;
import phasereditor.assetpack.ui.AssetPackUI;

/**
 * @author arian
 *
 */
public class ImageUrlCellEditor extends DialogCellEditor {

	private AssetModel _asset;
	private Function<AssetModel, String> _getUrl;
	
	
	public ImageUrlCellEditor(Composite parent, AssetModel asset, Function<AssetModel, String> getUrl) {
		super(parent);
		_asset = asset;
		_getUrl = getUrl;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		try {
			AssetPackModel pack = _asset.getPack();
			IFile urlFile = _asset.getFileFromUrl(_getUrl.apply(_asset));
			List<IFile> imageFiles = discoverImageFiles(pack);
			String result = AssetPackUI.browseImageUrl(pack, "", urlFile, imageFiles,
					cellEditorWindow.getShell());
			if (result == null) {
				result = _getUrl.apply(_asset);
			}
			return result;
		} catch (CoreException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("static-method")
	protected List<IFile> discoverImageFiles(AssetPackModel pack) throws CoreException {
		return pack.discoverImageFiles();
	}

}
