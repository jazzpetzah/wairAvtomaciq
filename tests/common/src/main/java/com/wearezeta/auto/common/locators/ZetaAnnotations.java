package com.wearezeta.auto.common.locators;

import static io.appium.java_client.remote.MobilePlatform.ANDROID;
import static io.appium.java_client.remote.MobilePlatform.IOS;

import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.Annotations;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import com.wearezeta.auto.common.CommonUtils;

import io.appium.java_client.MobileBy;
import io.appium.java_client.pagefactory.AndroidFindAll;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AndroidFindBys;
import io.appium.java_client.pagefactory.iOSFindAll;
import io.appium.java_client.pagefactory.iOSFindBy;
import io.appium.java_client.pagefactory.iOSFindBys;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ZetaAnnotations extends Annotations{

	private final static List<String> METHODS_TO_BE_EXCLUDED_WHEN_ANNOTATION_IS_READ = new ArrayList<String>() {
		private static final long serialVersionUID = 1L;
		{
			List<String> objectClassMethodNames = getMethodNames(Object.class
					.getDeclaredMethods());
			addAll(objectClassMethodNames);
			List<String> annotationClassMethodNames = getMethodNames(Annotation.class
					.getDeclaredMethods());
			annotationClassMethodNames.removeAll(objectClassMethodNames);
			addAll(annotationClassMethodNames);
		}
	};
	private final static Class<?>[] DEFAULT_ANNOTATION_METHOD_ARGUMENTS = new Class<?>[] {};
	private static List<String> getMethodNames(Method[] methods) {
		List<String> names = new ArrayList<String>();
		for (Method m : methods) {
			names.add(m.getName());
		}
		return names;
	}

	private static enum Strategies {
		BYUIAUTOMATOR("uiAutomator") {
			@Override
			By getBy(Annotation annotation) {
				String value = getValue(annotation, this);
				if (annotation.annotationType().equals(AndroidFindBy.class)) {
					return MobileBy.AndroidUIAutomator(value);
				}
				if (annotation.annotationType().equals(iOSFindBy.class)) {
					return MobileBy.IosUIAutomation(value);
				}
				return super.getBy(annotation);
			}
		},
		BYACCESSABILITY("accessibility") {
			@Override
			By getBy(Annotation annotation) {
				return MobileBy.AccessibilityId(getValue(annotation, this));
			}
		},
		BYCLASSNAME("className") {
			@Override
			By getBy(Annotation annotation) {
				return By.className(getValue(annotation, this));
			}
		},
		BYID("id") {
			@Override
			By getBy(Annotation annotation) {
				return By.id(getValue(annotation, this));
			}
		},
		BYTAG("tagName") {
			@Override
			By getBy(Annotation annotation) {
				return By.tagName(getValue(annotation, this));
			}
		},
		BYNAME("name") {
			@Override
			By getBy(Annotation annotation) {
				return By.name(getValue(annotation, this));
			}
		},
		BYXPATH("xpath") {
			@Override
			By getBy(Annotation annotation) {
				return By.xpath(getValue(annotation, this));
			}
		};

		private final String valueName;

		private String returnValueName() {
			return valueName;
		}

		private Strategies(String valueName) {
			this.valueName = valueName;
		}

		private static String[] strategiesNames() {
			Strategies[] strategies = values();
			String[] result = new String[strategies.length];
			int i = 0;
			for (Strategies strategy : values()) {
				result[i] = strategy.valueName;
				i++;
			}
			return result;
		}

		private static String getValue(Annotation annotation,
				Strategies strategy) {
			try {
				Method m = annotation.getClass().getMethod(strategy.valueName,
						DEFAULT_ANNOTATION_METHOD_ARGUMENTS);
				return m.invoke(annotation, new Object[] {}).toString();
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}

		By getBy(Annotation annotation) {
			return null;
		}
	}

	private final Field mobileField;
	private final String platform;

	ZetaAnnotations(Field field, String platform) {
		super(field);
		mobileField = field;
		this.platform = String.valueOf(platform).
				toUpperCase().trim();
	}

	private static void checkDisallowedAnnotationPairs(Annotation a1,
			Annotation a2) throws IllegalArgumentException {
		if (a1 != null && a2 != null) {
			throw new IllegalArgumentException(
					"If you use a '@" + a1.getClass().getSimpleName() + "' annotation, "
							+ "you must not also use a '@" + a2.getClass().getSimpleName() + "' annotation");
		}
	}

	private void assertValidAnnotations() {
		AndroidFindBy androidBy = mobileField
				.getAnnotation(AndroidFindBy.class);
		AndroidFindBys androidBys = mobileField
				.getAnnotation(AndroidFindBys.class);
		AndroidFindAll androidFindAll = mobileField.
				getAnnotation(AndroidFindAll.class);

		iOSFindBy iOSBy = mobileField.getAnnotation(iOSFindBy.class);
		iOSFindBys iOSBys = mobileField.getAnnotation(iOSFindBys.class);
		iOSFindAll iOSFindAll = mobileField.getAnnotation(iOSFindAll.class);

		checkDisallowedAnnotationPairs(androidBy, androidBys);
		checkDisallowedAnnotationPairs(androidBy, androidFindAll);
		checkDisallowedAnnotationPairs(androidBys, androidFindAll);

		checkDisallowedAnnotationPairs(iOSBy, iOSBys);
		checkDisallowedAnnotationPairs(iOSBy, iOSFindAll);
		checkDisallowedAnnotationPairs(iOSBys, iOSFindAll);
	}

	private static Method[] prepareAnnotationMethods(
			Class<? extends Annotation> annotation) {
		List<String> targeAnnotationMethodNamesList = getMethodNames(annotation.getDeclaredMethods());
		targeAnnotationMethodNamesList.removeAll(METHODS_TO_BE_EXCLUDED_WHEN_ANNOTATION_IS_READ);
		Method[] result = new Method[targeAnnotationMethodNamesList.size()];
		for (String methodName: targeAnnotationMethodNamesList){
			try {
				result[targeAnnotationMethodNamesList.indexOf(methodName)] = annotation.getMethod(methodName, DEFAULT_ANNOTATION_METHOD_ARGUMENTS);
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			}
		}
		return result;
	}

	// I suppose that only @AndroidFindBy and @iOSFindBy will be here
	private static String getFilledValue(Annotation mobileBy) {
		Method[] values = prepareAnnotationMethods(mobileBy.getClass());
		for (Method value : values) {
			try {
				String strategyParameter = value.invoke(mobileBy,
						new Object[] {}).toString();
				if (!"".equals(strategyParameter)) {
					return value.getName();
				}
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
		throw new IllegalArgumentException("@"
				+ mobileBy.getClass().getSimpleName() + ": one of "
				+ Strategies.strategiesNames().toString() + " should be filled");
	}

	private By getMobileBy(Annotation annotation, String valueName) {
		Strategies strategies[] = Strategies.values();
		for (Strategies strategy : strategies) {
			if (strategy.returnValueName().equals(valueName)) {
				return strategy.getBy(annotation);
			}
		}
		throw new IllegalArgumentException("@"
				+ annotation.getClass().getSimpleName()
				+ ": There is an unknown strategy " + valueName);
	}

	@SuppressWarnings("unchecked")
	private <T extends By> T getComplexMobileBy(Annotation[] annotations, Class<T> requiredByClass) {
		;
		By[] byArray = new By[annotations.length];
		for (int i = 0; i < annotations.length; i++) {
			byArray[i] = getMobileBy(annotations[i],
					getFilledValue(annotations[i]));
		}
		try {
			Constructor<?> c = requiredByClass.getConstructor(By[].class);
			Object[] values = new Object[]{byArray};
			return (T) c.newInstance(values);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public By buildBy() {
		assertValidAnnotations();

		AndroidFindBy androidBy = mobileField
				.getAnnotation(AndroidFindBy.class);
		if (androidBy != null && ANDROID.toUpperCase().equals(platform)) {
			return getMobileBy(androidBy, getFilledValue(androidBy));
		}

		AndroidFindBys androidBys = mobileField
				.getAnnotation(AndroidFindBys.class);
		if (androidBys != null && ANDROID.toUpperCase().equals(platform)) {
			return getComplexMobileBy(androidBys.value(), ByChained.class);
		}

		AndroidFindAll androidFindAll = mobileField.getAnnotation(AndroidFindAll.class);
		if (androidFindAll != null && ANDROID.toUpperCase().equals(platform)) {
			return getComplexMobileBy(androidFindAll.value(), ByAll.class);
		}

		iOSFindBy iOSBy = mobileField.getAnnotation(iOSFindBy.class);
		if (iOSBy != null && IOS.toUpperCase().equals(platform)) {
			return getMobileBy(iOSBy, getFilledValue(iOSBy));
		}

		iOSFindBys iOSBys = mobileField.getAnnotation(iOSFindBys.class);
		if (iOSBys != null && IOS.toUpperCase().equals(platform)) {
			return getComplexMobileBy(iOSBys.value(), ByChained.class);
		}

		iOSFindAll iOSFindAll = mobileField.getAnnotation(iOSFindAll.class);
		if (iOSFindAll != null && IOS.toUpperCase().equals(platform)) {
			return getComplexMobileBy(iOSFindAll.value(), ByAll.class);
		}		
		
		ZetaFindBy zetaFindBy = mobileField.getAnnotation(ZetaFindBy.class);
		if (zetaFindBy != null) {
			try {
				return buildByFromZetaFindBy(zetaFindBy);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return super.buildBy();
	}

	protected By buildByFromZetaFindBy(ZetaFindBy findBy) throws Exception{
		ZetaHow how = findBy.how();
		String locatorsDb = findBy.locatorsDb();
		String locatorKey = findBy.locatorKey();

		if(CommonUtils.getAndroidApiLvl(ZetaAnnotations.class) < 43){
			if(how == ZetaHow.ID){
				how = ZetaHow.XPATH;
			}
			locatorKey = "xpath" + locatorKey.substring(2) + "42";
		}

		String locatorValue = getLocatorValue(locatorsDb, locatorKey);
		switch (how) {
		case CLASS_NAME:
			return By.className(locatorValue);

		case CSS:
			return By.cssSelector(locatorValue);

		case ID:
			return By.id(locatorValue);

		case LINK_TEXT:
			return By.linkText(locatorValue);

		case NAME:
			return By.name(locatorValue);

		case PARTIAL_LINK_TEXT:
			return By.partialLinkText(locatorValue);

		case TAG_NAME:
			return By.tagName(locatorValue);

		case XPATH:
			return By.xpath(locatorValue);

		case UIAUTOMATOR:
			return MobileBy.AndroidUIAutomator(locatorValue);

		default:
			// Note that this shouldn't happen (eg, the above matches all
					// possible values for the How enum)
					throw new IllegalArgumentException("Cannot determine how to locate element " + mobileField);
		}
	}
	public String getLocatorValue(String db, String key) {
		try {
			Class<?> locatorsClass = Class.forName(db);
			Field valueField = locatorsClass.getDeclaredField(key);
			return (String) valueField.get(null);
		} catch (ClassNotFoundException e) {
			System.out.println("Can't find locators class for name " + db);
		} catch (NoSuchFieldException e) {
			System.out.println("No locator with name " + key + " in " + db + " class.");
		} catch (IllegalAccessException e) {
			System.out.println("Error while trying to access locator with name " + key + ": " + e.getMessage());
		}
		return key;

	}
}
