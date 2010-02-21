package org.dyndns.schuschu.xmms2client.backend;

import se.fnord.xmms2.client.internal.OrdinalMap;

public enum FooBackendType {
	FooBackendFilter, FooBackendPlaylist, FooBackendPlaylistSwitch, FooBackendText;
	
	
    private static final OrdinalMap<FooBackendType> ordinal_map = OrdinalMap.populate(FooBackendType.class);

	public static FooBackendType fromOrdinal(int o) {
		return ordinal_map.get(o);
	}
	
	public static FooBackendType fromString(String s){
		return FooBackendType.valueOf(s);
	}


}
