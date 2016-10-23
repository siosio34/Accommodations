package net.daum.android.map.openapi.search;

import java.util.List;

public interface OnFinishSearchListener {
	public void onSuccess(List<Marker> itemList);
	public void onFail();
}

