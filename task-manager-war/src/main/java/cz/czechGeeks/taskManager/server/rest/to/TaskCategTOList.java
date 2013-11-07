package cz.czechGeeks.taskManager.server.rest.to;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TaskCategList")
@XmlAccessorType(XmlAccessType.FIELD)
public class TaskCategTOList {
	
	@XmlElement(name="TaskCateg")
	private List<TaskCategTO> toList;

	public List<TaskCategTO> getToList() {
		return toList;
	}

	public void setToList(List<TaskCategTO> toList) {
		this.toList = toList;
	}

}
