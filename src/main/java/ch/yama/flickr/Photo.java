package ch.yama.flickr;

import org.json.JSONObject;

public class Photo {

	private final String m_id;
	private final String m_owner;
	private final String m_secret;
	private final String m_server;
	private final int m_farm;
	private final String m_title;
	private final boolean m_public;
	private final boolean m_friend;
	private final boolean m_family;
	private final long m_dateUpload;
	private final String m_dateTaken;
	private final int m_dateTakenGranularity;
	private final boolean m_dateTakenUnknown;

	Photo(JSONObject object) {
		this.m_id = object.getString("id");
		this.m_owner = object.getString("owner");
		this.m_secret = object.getString("secret");
		this.m_server = object.getString("server");
		this.m_farm = object.getInt("farm");
		this.m_title = object.getString("title");
		this.m_public = object.getInt("ispublic") == 0 ? false : true;
		this.m_friend = object.getInt("isfriend") == 0 ? false : true;
		this.m_family = object.getInt("isfamily") == 0 ? false : true;
		this.m_dateUpload = object.getLong("dateupload");
		this.m_dateTaken = object.getString("datetaken");
		this.m_dateTakenGranularity = object.getInt("datetakengranularity");
		this.m_dateTakenUnknown = object.getInt("datetakenunknown") == 0 ? false : true;
	}

	public String getTitle() {
		return this.m_title;
	}

	public String getDateTaken() {
		return m_dateTaken;
	}

	public String getId() {
		return m_id;
	}
}
