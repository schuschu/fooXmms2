package org.dyndns.schuschu.xmms2client.view.element;

import java.util.Vector;

import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceBackend;
import org.dyndns.schuschu.xmms2client.interfaces.FooInterfaceViewElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class FooTable implements FooInterfaceViewElement{
	
	private Table table;
	private FooInterfaceBackend backend;
	
	public FooTable(Composite parent, int style){
		table = new Table(parent, style);
	}
	
	@Override
	public void addKeyListener(KeyListener key) {
		table.addKeyListener(key);		
	}

	@Override
	public void addMouseListener(MouseListener mouse) {
		table.addMouseListener(mouse);		
	}

	@Override
	public FooInterfaceBackend getBackend() {
		return backend;
	}

	@Override
	public int[] getIndices() {
		return table.getSelectionIndices();
	}

	@Override
	public Control getReal() {
		return table;
	}

	@Override
	public void removeKeyListener(KeyListener key) {
		table.removeKeyListener(key);
		
	}

	@Override
	public void removeMouseListener(MouseListener mouse) {
		table.removeMouseListener(mouse);
		
	}

	@Override
	public void setBackend(FooInterfaceBackend backend) {
		this.backend=backend;
		
	}

	@Override
	public void setContent(Vector<String> content) {
		
		table.removeAll();
	    TableColumn column = new TableColumn(table, SWT.NONE);
				
		Color red = table.getDisplay().getSystemColor(SWT.COLOR_RED);
		
		int pos = backend.getCurrentPos();
				
		for(int i = 0; i<content.size();i++){
			
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(content.get(i));
			if(i==pos){
			    item.setBackground(red);
			}

		}
	    column.pack();
	}

	@Override
	public void setSelection(int[] indices) {
		table.setSelection(indices);
		
	}

	@Override
	public void setSingleSelectionMode() {
		// TODO find out if swt can do this
		
	}

	@Override
	public void setLayoutData(Object layoutData) {
		table.setLayoutData(layoutData);		
	}

}
