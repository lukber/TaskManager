package cz.czechGeeks.taskManager.server.rest.to;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TaskList")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskTOList {
	
	@XmlElement(name="Task")
	private List<TaskTO> toList;

	public List<TaskTO> getToList() {
		return toList;
	}

	public void setToList(List<TaskTO> toList) {
		this.toList = toList;
	}

}
