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
package phasereditor.scene.ui.editor.properties;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import phasereditor.scene.core.FlipComponent;
import phasereditor.ui.EditorSharedImages;

/**
 * @author arian
 *
 */
public class FlipSection extends ScenePropertySection {

	private Action _flipXAction;
	private Action _flipYAction;

	public FlipSection(ScenePropertyPage page) {
		super("Flip", page);
	}

	@Override
	public boolean canEdit(Object obj) {
		return obj instanceof FlipComponent;
	}

	@Override
	public Control createContent(Composite parent) {
		return null;
	}

	@Override
	public void fillToolbar(ToolBarManager manager) {
		manager.add(
				_flipXAction = new Action(getHelp("Phaser.GameObjects.Components.Flip.flipX"), IAction.AS_CHECK_BOX) {

					{
						setImageDescriptor(EditorSharedImages.getImageDescriptor(IMG_SHAPE_FLIP_HORIZONTAL));
					}

					@Override
					public void run() {
						var value = isChecked();

						wrapOperation(() -> {
							getModels().forEach(model -> FlipComponent.set_flipX(model, value));
						});
					}
				});

		manager.add(
				_flipYAction = new Action(getHelp("Phaser.GameObjects.Components.Flip.flipY"), IAction.AS_CHECK_BOX) {

					{
						setImageDescriptor(EditorSharedImages.getImageDescriptor(IMG_SHAPE_FLIP_VERTICAL));
					}

					@Override
					public void run() {
						var value = isChecked();

						wrapOperation(() -> {
							getModels().forEach(model -> FlipComponent.set_flipY(model, value));
						});
					}
				});

	}

	@Override
	@SuppressWarnings("boxing")
	public void user_update_UI_from_Model() {
		var models = getModels();

		{
			// x

			var value = flatValues_to_Boolean(models.stream().map(model -> FlipComponent.get_flipX(model)));

			_flipXAction.setChecked(value != null && value);
		}

		{
			// y

			var value = flatValues_to_Boolean(models.stream().map(model -> FlipComponent.get_flipY(model)));

			_flipYAction.setChecked(value != null && value);
		}
	}

}
