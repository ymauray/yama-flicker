package ch.yama.flickr.impl;

import org.json.JSONObject;

@FunctionalInterface
public interface RequestPaginator {

	boolean apply(JSONObject object);

}
