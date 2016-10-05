package ch.yama.flickr.model;

import org.json.JSONObject;

public class Photoset {

	private final long m_id;
	private final String m_title;

	public Photoset(JSONObject object) {
		m_id = object.getLong("id");
		m_title = object.getJSONObject("title").getString("_content");
	}

	public long getId() {
		return m_id;
	}

	public String getTitle() {
		return m_title;
	}

}
