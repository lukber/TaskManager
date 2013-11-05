package cz.czechGeeks.taskManager.server.util;

import java.lang.annotation.Annotation;

import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import cz.czechGeeks.taskManager.server.exception.CantFindEjbException;

/**
 * EJB locator - singleton
 * 
 * @author lukasb
 */
public enum ServiceLocator {
	INSTANCE;

	private final InitialContext context;

	private ServiceLocator() {
		try {
			this.context = new InitialContext();
		} catch (NamingException e) {
			throw new RuntimeException("Can't initialize InitialContext.", e);
		}
	}

	/**
	 * Lookup EJB service
	 * 
	 * @param beanClass
	 * @return interface of EJB
	 */
	public <T> T getService(Class<T> beanClass) {
		String beanName = getBeanName(beanClass);
		String serviceName = getServiceName(beanName);
		try {
			@SuppressWarnings("unchecked")
			T lookup = (T) context.lookup(serviceName);
			return lookup;
		} catch (NamingException e) {
			throw new CantFindEjbException("Can't locate EJB service: " + serviceName, e);
		}
	}

	private static <T> String getBeanName(Class<T> beanClass) {
		Annotation statelessAnnotation = getAnnotation(beanClass, Stateless.class);
		if (statelessAnnotation != null) {
			String name = ((Stateless) statelessAnnotation).name();
			if (name != null && !name.isEmpty()) {
				return name;
			}
		}

		return beanClass.getSimpleName();
	}

	private static <T> Annotation getAnnotation(Class<T> beanClass, final Class<? extends Annotation> annotationClass) {
		for (Annotation annotation : beanClass.getDeclaredAnnotations()) {
			if (annotation.annotationType().equals(annotationClass)) {
				return annotation;
			}
		}
		return null;
	}

	private String getServiceName(String beanClassName) {
		StringBuilder b = new StringBuilder();
		b.append("java:app/task-manager-ejb/");
		b.append(beanClassName);
		String serviceName = b.toString();
		return serviceName;
	}
}
