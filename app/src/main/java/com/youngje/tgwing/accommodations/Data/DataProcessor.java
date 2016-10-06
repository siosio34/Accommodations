package com.youngje.tgwing.accommodations.Data;

import com.youngje.tgwing.accommodations.Marker;

import org.json.JSONException;

import java.util.List;


/**
 * Created by joyeongje on 2016. 10. 6..
 */

public interface DataProcessor {

    List<Marker> load(String rawData, DataFormat.DATATYPE datatype) throws JSONException;
}
