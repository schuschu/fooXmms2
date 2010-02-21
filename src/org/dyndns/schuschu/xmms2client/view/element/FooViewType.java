package org.dyndns.schuschu.xmms2client.view.element;

import se.fnord.xmms2.client.internal.OrdinalMap;

public enum FooViewType {
	FooButton, FooCombo, FooLabel, FooList, FooTable, 
	SashForm, Shell, Composite;
	
	
    private static final OrdinalMap<FooViewType> ordinal_map = OrdinalMap.populate(FooViewType.class);

	public static FooViewType fromOrdinal(int o) {
		return ordinal_map.get(o);
	}
	
	public static FooViewType fromString(String s){
		return FooViewType.valueOf(s);
	}


}
