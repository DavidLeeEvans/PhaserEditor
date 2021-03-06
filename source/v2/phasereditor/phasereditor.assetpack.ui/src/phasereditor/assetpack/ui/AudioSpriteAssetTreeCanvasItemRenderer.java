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
package phasereditor.assetpack.ui;

import org.eclipse.swt.graphics.GC;

import phasereditor.assetpack.core.AudioSpriteAssetModel;
import phasereditor.audio.core.AudioCore;
import phasereditor.ui.TreeCanvas.TreeCanvasItem;

/**
 * @author arian
 *
 */
public class AudioSpriteAssetTreeCanvasItemRenderer extends AudioAssetTreeCanvasItemRenderer {

	private double _duration;

	public AudioSpriteAssetTreeCanvasItemRenderer(TreeCanvasItem item) {
		super(item);

		var file = getAudioFile(item.getData());
		if (file == null) {
			_duration = 0;
		} else {
			_duration = AudioCore.getSoundDuration(file);
		}
	}

	public double getDuration() {
		return _duration;
	}

	@Override
	protected void renderImage(GC gc, int dstX, int dstY, int dstWidth, int dstHeight) {
		if (_duration == 0) {
			return;
		}

		var asset = (AudioSpriteAssetModel) _item.getData();

		var sprites = asset.getSpriteMap();

		var proxy = getImageProxy();
		var b = proxy.getFinalFrameData().src;

		var count = sprites.size();
		var spacing = 3;

		if (spacing * count > dstWidth / 3) {
			spacing = 0;
		}

		var dstWidth2 = dstWidth / (double) count - spacing;

		var lastDstX = dstX;

		gc.setAlpha(150);
		for (var sprite : sprites) {

			var startFactor = sprite.getStart() / _duration;
			var endFactor = sprite.getEnd() / _duration;

			var srcX1 = startFactor * b.width;
			var srcX2 = endFactor * b.width;
			var srcWidth = srcX2 - srcX1;

			proxy.paint(gc, (int) srcX1, 0, (int) srcWidth, b.height, lastDstX, dstY, (int) dstWidth2, dstHeight);

			lastDstX += dstWidth2 + spacing;
		}
		gc.setAlpha(255);
	}

}
