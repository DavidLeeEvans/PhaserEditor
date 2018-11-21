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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.ToolItem;

import phasereditor.scene.core.GameObjectEditorComponent;
import phasereditor.scene.core.GroupModel;
import phasereditor.scene.core.ObjectModel;
import phasereditor.scene.core.ParentComponent;
import phasereditor.scene.core.SceneModel;
import phasereditor.scene.core.VariableComponent;
import phasereditor.scene.ui.SceneUI;
import phasereditor.scene.ui.editor.SceneEditor;
import phasereditor.scene.ui.editor.properties.OrderAction.OrderActionValue;
import phasereditor.scene.ui.editor.undo.GroupListSnapshotOperation;
import phasereditor.ui.EditorSharedImages;

/**
 * @author arian
 *
 */
public class EditorSection extends ScenePropertySection {

	private Button _typeBtn;
	private Scale _transpScale;
	private List<JFaceOrderAction> _orderActions;
	private IAction _addToGroupAction;
	private IAction _removeFromGroupAction;
	private Label _groupsLabel;

	public EditorSection(ScenePropertyPage page) {
		super("Editor", page);
	}

	@Override
	public boolean canEdit(Object obj) {
		return obj instanceof GameObjectEditorComponent;
	}

	@Override
	public Control createContent(Composite parent) {

		createActions();

		Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(2, false));

		{
			label(comp, "Type", "*(Editor) The Phaser type of this object." +

					"\n\nClick on the next button to morhp to other type.");

			_typeBtn = new Button(comp, SWT.NONE);
			_typeBtn.setToolTipText("Click to morph to other type.");
			_typeBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			_typeBtn.addSelectionListener(SelectionListener.widgetSelectedAdapter(this::populateTypeList));
		}

		{
			label(comp, "Transparency", "*(Editor) Transparency of the object when is renderer in the editor.");

			_transpScale = new Scale(comp, 0);
			_transpScale.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			_transpScale.setMinimum(0);
			_transpScale.setMinimum(100);
		}

		{
			label(comp, "Order", "*(Editor) The display depth order.");

			var manager = new ToolBarManager();

			for (var action : _orderActions) {
				manager.add(action);
			}

			var toolbar = manager.createControl(comp);
			toolbar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}

		{
			label(comp, "Groups", "*(Editor) The object's groups.");

			var rightComp = new Composite(comp, 0);
			rightComp.setLayout(new GridLayout(2, false));
			rightComp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			_groupsLabel = new Label(rightComp, 0);
			_groupsLabel.setText("[group1, group2]");
			_groupsLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			var manager = new ToolBarManager();

			manager.add(_addToGroupAction);
			manager.add(_removeFromGroupAction);

			manager.createControl(rightComp);

		}

		return comp;
	}

	@Override
	public void fillToolbar(ToolBarManager manager) {
		for (var action : _orderActions) {
			manager.add(action);
		}

		manager.add(new Separator());

		manager.add(_addToGroupAction);
		manager.add(_removeFromGroupAction);

	}

	abstract class GroupMenuAction extends Action {

		public GroupMenuAction(String text, String icon) {
			super(text);

			setImageDescriptor(EditorSharedImages.getImageDescriptor(icon));
		}

		@SuppressWarnings({ "cast", "rawtypes", "unchecked" })
		@Override
		public void runWithEvent(Event event) {

			var manager = new MenuManager();

			var editor = getEditor();

			var groups = ParentComponent.get_children(editor.getSceneModel().getGroupsModel());

			fillMenu(manager, editor, (List<GroupModel>) (List) groups);

			var menu = manager.createContextMenu(((ToolItem) event.widget).getParent());
			menu.setVisible(true);

		}

		protected abstract void fillMenu(MenuManager manager, SceneEditor editor, List<GroupModel> groups);
	}

	abstract class GroupAction extends Action {

		private GroupModel _group;

		public GroupAction(GroupModel group, String icon) {

			super(VariableComponent.get_variableName(group), EditorSharedImages.getImageDescriptor(icon));

			_group = group;
		}

		@Override
		public void runWithEvent(Event event) {
			var editor = getEditor();

			var before = GroupListSnapshotOperation.takeSnapshot(editor);

			var groupChildren = ParentComponent.get_children(_group);

			performGroupOperation(groupChildren);

			var after = GroupListSnapshotOperation.takeSnapshot(editor);

			editor.executeOperation(new GroupListSnapshotOperation(before, after, getText()));

			editor.setDirty(true);
			editor.getScene().redraw();
			editor.refreshOutline();

			editor.updatePropertyPagesContentWithSelection();

		}

		protected abstract void performGroupOperation(List<ObjectModel> groupChildren);
	}

	class AddToGroupMenuAction extends GroupMenuAction {

		public AddToGroupMenuAction() {
			super("Add selected objects to a group.", IMG_ADD_TO_GROUP);
		}

		@Override
		protected void fillMenu(MenuManager manager, SceneEditor editor, List<GroupModel> groups) {

			groups.stream().filter(group -> {

				// do not include groups that contains one of the selected models

				var children = ParentComponent.get_children(group);
				for (var model : getModels()) {
					if (children.contains(model)) {
						return false;
					}
				}
				return true;
			}).forEach(group -> {
				manager.add(new GroupAction(group, IMG_ADD) {

					@Override
					protected void performGroupOperation(List<ObjectModel> groupChildren) {
						groupChildren.addAll(getModels());
					}

				});
			});

		}
	}

	class RemoveFromGroupMenuAction extends GroupMenuAction {

		public RemoveFromGroupMenuAction() {
			super("Remove selcted objects from a group.", IMG_REMOVE_FROM_GROUP);
		}

		@Override
		protected void fillMenu(MenuManager manager, SceneEditor editor, List<GroupModel> groups) {
			groups.stream().filter(group -> {

				// just accepts the groups that contains all the selected objects.

				return ParentComponent.get_children(group).containsAll(getModels());
			}).forEach(group -> {
				manager.add(new GroupAction(group, IMG_DELETE) {

					@Override
					protected void performGroupOperation(List<ObjectModel> groupChildren) {
						groupChildren.removeAll(getModels());
					}

				});
			});
		}

	}

	private void createActions() {
		var editor = getEditor();

		_orderActions = new ArrayList<>();
		_orderActions.add(new JFaceOrderAction(editor, OrderActionValue.UP));
		_orderActions.add(new JFaceOrderAction(editor, OrderActionValue.DOWN));
		_orderActions.add(new JFaceOrderAction(editor, OrderActionValue.TOP));
		_orderActions.add(new JFaceOrderAction(editor, OrderActionValue.BOTTOM));

		_addToGroupAction = new AddToGroupMenuAction();

		_removeFromGroupAction = new RemoveFromGroupMenuAction();
	}

	class MorphAction extends Action {
		private String _toType;

		public MorphAction(String toType) {
			super("Morph To " + toType);
			_toType = toType;
		}

		@Override
		public void run() {
			SceneUI.action_MorphObjectsToNewType(getEditor(), getModels(), _toType);
		}

	}

	@SuppressWarnings({ "unused", "boxing" })
	private void populateTypeList(SelectionEvent e) {
		var models = getModels();

		var manager = new MenuManager();

		for (var type : SceneModel.GAME_OBJECT_TYPES) {

			var allow = models.stream()

					.map(m -> m.allowMorphTo(type))

					.reduce(true, (a, b) -> a && b);

			if (allow) {
				manager.add(new MorphAction(type));
			}
		}

		if (manager.getSize() > 0) {
			var menu = manager.createContextMenu(_typeBtn);
			menu.setVisible(true);
		}
	}

	@SuppressWarnings("boxing")
	@Override
	public void update_UI_from_Model() {
		var models = getModels();

		_typeBtn.setText(flatValues_to_String(models.stream().map(model -> model.getType())));

		_transpScale.setSelection(flatValues_to_int(
				models.stream()
						.map(model -> (int) (GameObjectEditorComponent.get_gameObjectEditorTransparency(model) * 100)),
				100));
		{

			var groups = ParentComponent.get_children(getEditor().getSceneModel().getGroupsModel());

			var str = groups.stream().filter(group -> ParentComponent.get_children(group).containsAll(models))
					.map(group -> VariableComponent.get_variableName(group)).collect(Collectors.joining(","));

			_groupsLabel.setText("[" + str + "]");

			_removeFromGroupAction.setEnabled(str.length() > 2);

			_addToGroupAction.setEnabled(groups.stream().filter(group -> {
				// do not include groups that contains one of the selected models
				var children = ParentComponent.get_children(group);
				for (var model : getModels()) {
					if (children.contains(model)) {
						return false;
					}
				}
				return true;
			}).count() > 0);

		}

		listenFloat(_transpScale, value -> {

			models.stream().forEach(model -> GameObjectEditorComponent.set_gameObjectEditorTransparency(model, value));

			getEditor().setDirty(true);
		}, models);
	}

}
