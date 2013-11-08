package cz.czechGeeks.taskManager.server.rest.to;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LoginList")
@XmlAccessorType(XmlAccessType.FIELD)
public class LoginTOList {

	@XmlElement(name = "Login")
	private List<LoginTO> toList;

	public List<LoginTO> getToList() {
		return toList;
	}

	public void setToList(List<LoginTO> toList) {
		this.toList = toList;
	}

}
