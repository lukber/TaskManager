@XmlJavaTypeAdapters(  
 @XmlJavaTypeAdapter(value=TimestampAdapter.class,type=Timestamp.class))
package cz.czechGeeks.taskManager.server.rest;
import java.sql.Timestamp;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;

import cz.czechGeeks.taskManager.server.rest.adapter.TimestampAdapter;

