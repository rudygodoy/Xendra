//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.

package org.frapuccino.treetable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;

import java.util.EventObject;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeModel;
//
//
// remember the columnof tree  [0]  must be editable, otherwise you can't collapse/expand tree nodes
//
public class TreeTable extends JTable {
	private TreeModel model;
	private CustomTreeTableCellRenderer tree;

	public TreeTable() {
		tree= new CustomTreeTableCellRenderer(this);
		setDefaultRenderer(CustomTreeTableCellRenderer.class, tree);
		setDefaultEditor(CustomTreeTableCellRenderer.class,
			new CustomTreeTableCellEditor());
		tree.setRowHeight(getRowHeight());

		setShowGrid(false);
		tree.setTable(this);

		//				Important: JTable and JTree must share their row selection models. 
		tree.setSelectionModel(new DefaultTreeSelectionModel() {

			{
				// setSelectionModel() of TreeTable
				// listSelectionModel of DefaultTreeSelectionModel
				setSelectionModel(listSelectionModel);
			}
		});

		sizeColumnsToFit(AUTO_RESIZE_NEXT_COLUMN);

		setIntercellSpacing(new Dimension(0, 0));

	}

	public JTree getTree() {
		return tree;
	}

	/* We overide setBounds() of the TreeTableCellRenderer
	 * to move and resize our tree to the right position
	 * in our table row. This method is also used by the UI to
	 * paint the editor. To ensure the editor is never painted by the UI,
	 * we must return -1 for the row containig our tree.
	 */
	public int getEditingRow() {
		if (getColumnClass(editingColumn)
			== CustomTreeTableCellRenderer.class) {
			return -1;
		}

		return editingRow;
	}

	/**
	 * Overridden to message super and forward the method to the tree.
	 * Since the tree is not actually in the component hieachy it will
	 * never receive this unless we forward it in this manner.
	 */
	public void updateUI() {
		super.updateUI();

		if (tree != null) {
			tree.updateUI();
		}

		// Use the tree's default foreground and background colors in the
		// table. 
		LookAndFeel.installColorsAndFont(
			this,
			"Table.background",
			"Table.foreground",
			"Table.font");
	}

	/**
	 * Overridden to pass the new rowHeight to the tree.
	 */
	public void setRowHeight(int rowHeight) {
		super.setRowHeight(rowHeight);

		/*
		if (tree != null) {
		    tree.setRowHeight(rowHeight);
		}
		*/

		if (tree != null && tree.getRowHeight() != rowHeight) {
			tree.setRowHeight(getRowHeight());
		}

	}

	// The editor is needed to forward mouse and key events 
	// inside a table cell to the tree   
	public class CustomTreeTableCellEditor
		extends AbstractCellEditor
		implements TableCellEditor {
		public Component getTableCellEditorComponent(
			JTable table,
			Object value,
			boolean isSelected,
			int r,
			int c) {
			return tree;
		}

		/**
		 * This method is copied from JTreeTable.java
		 * by Philip Milne and Scott Violet
		 *
		 * Overridden to return false, and if the event is a mouse event
		 * it is forwarded to the tree.<p>
		 * The behavior for this is debatable, and should really be offered
		 * as a property. By returning false, all keyboard actions are
		 * implemented in terms of the table. By returning true, the
		 * tree would get a chance to do something with the keyboard
		 * events. For the most part this is ok. But for certain keys,
		 * such as left/right, the tree will expand/collapse where as
		 * the table focus should really move to a different column. Page
		 * up/down should also be implemented in terms of the table.
		 * By returning false this also has the added benefit that clicking
		 * outside of the bounds of the tree node, but still in the tree
		 * column will select the row, whereas if this returned true
		 * that wouldn't be the case.
		 * <p>By returning false we are also enforcing the policy that
		 * the tree will never be editable (at least by a key sequence).
		 */
		public boolean isCellEditable(EventObject e) {
			if (e instanceof MouseEvent) {
				for (int counter= 0; counter < getColumnCount(); counter++) {

					if (getColumnClass(counter)
						== CustomTreeTableCellRenderer.class) {
						MouseEvent me= (MouseEvent) e;

						// transform the x value according to current column position
						int transX= me.getX() - getCellRect(0, counter, true).x;

						// @author: fdietz
						// HACK: when expanding a treenode everything works fine,
						// but when collapsing, the mouse-event "released" is used
						// -> changed this to "pressed" which makes sure the underlying
						// -> JTree really collapses the treenode
						int id = me.getID();
						if ( id == MouseEvent.MOUSE_RELEASED) id = MouseEvent.MOUSE_PRESSED;  
							
						MouseEvent newMouseEvent=
							new MouseEvent(
								tree,
								id,
								me.getWhen(),
								me.getModifiers(),
								transX,
								me.getY(),
								me.getClickCount(),
								me.isPopupTrigger());

						tree.dispatchEvent(newMouseEvent);

						// update the table
						//TreeTable.this.revalidate();

						break;
						//return true;
					}
				}
			}

			return false;
		}
	}
}
