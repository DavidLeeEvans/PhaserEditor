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
package phasereditor.assetpack.ui.preview;

import java.util.List;

import phasereditor.assetpack.core.IAssetFrameModel;
import phasereditor.assetpack.ui.AssetPackUI;
import phasereditor.ui.IFrameProvider;
import phasereditor.ui.ImageProxy;

/**
 * @author arian
 *
 */
public class AssetFramesProvider implements IFrameProvider {
	private List<? extends IAssetFrameModel> _frames;

	public AssetFramesProvider(List<? extends IAssetFrameModel> frames) {
		super();
		_frames = frames;
	}

	@Override
	public int getFrameCount() {
		return _frames.size();
	}

	@Override
	public ImageProxy getFrameImageProxy(int index) {
		return AssetPackUI.getImageProxy(_frames.get(index));
	}

	@Override
	public String getFrameTooltip(int index) {
		var size = getFrameImageProxy(index).getFinalFrameData().srcSize;
		return size.x + "x" + size.y;
	}

	@Override
	public IAssetFrameModel getFrameObject(int index) {
		return _frames.get(index);
	}

	@Override
	public String getFrameLabel(int index) {
		var frame = _frames.get(index);
		if (frame == null) {
			return null;
		}
		return frame.getKey();
	}
}
