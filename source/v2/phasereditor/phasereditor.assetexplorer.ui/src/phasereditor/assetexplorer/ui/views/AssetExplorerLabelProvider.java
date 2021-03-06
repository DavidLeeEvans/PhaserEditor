// The MIT License (MIT)
//
// Copyright (c) 2015, 2016 Arian Fornaris
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
package phasereditor.assetexplorer.ui.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import phasereditor.assetexplorer.ui.views.newactions.NewWizardLancher;
import phasereditor.assetpack.ui.AssetLabelProvider;
import phasereditor.scene.core.SceneFile;
import phasereditor.ui.EditorSharedImages;
import phasereditor.ui.IEditorSharedImages;

class AssetExplorerLabelProvider extends LabelProvider {

	public AssetExplorerLabelProvider() {
	}

	@Override
	public String getText(Object element) {
		
		if (element instanceof SceneFile) {
			return ((SceneFile) element).getFile().getName();
		}

		return AssetLabelProvider.GLOBAL_16.getText(element);
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof SceneFile) {
			return getCanvasImage();
		}
		
		if (element instanceof NewWizardLancher) {
			return EditorSharedImages.getImage(IEditorSharedImages.IMG_ADD);
		}

		return AssetLabelProvider.GLOBAL_16.getImage(element);
	}

	public static Image getCanvasImage() {
		return EditorSharedImages.getImage(IEditorSharedImages.IMG_CANVAS);
	}
}