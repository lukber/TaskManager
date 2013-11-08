package cz.czechGeeks.taskManager.server.rest.adapter;

import java.sql.Timestamp;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class TimestampAdapter extends XmlAdapter<Date, Timestamp> {
	public Date marshal(Timestamp v) {
		return new Date(v.getTime());
	}

	public Timestamp unmarshal(Date v) {
		return new Timestamp(v.getTime());
	}

}
