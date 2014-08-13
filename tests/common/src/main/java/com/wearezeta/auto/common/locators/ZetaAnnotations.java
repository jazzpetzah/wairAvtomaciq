package com.wearezeta.auto.common.locators;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ByIdOrName;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.pagefactory.ByAll;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class ZetaAnnotations {

  private Field field;

  public ZetaAnnotations(Field field) {
    this.field = field;
  }

  public boolean isLookupCached() {
    return (field.getAnnotation(CacheLookup.class) != null);
  }

  public By buildBy() {
    assertValidAnnotations();

    By ans = null;

    FindBys findBys = field.getAnnotation(FindBys.class);
    if (findBys != null) {
      ans = buildByFromFindBys(findBys);
    }

    FindAll findAll = field.getAnnotation(FindAll.class);
    if (ans == null && findAll != null) {
      ans = buildBysFromFindByOneOf(findAll);
    }

    FindBy findBy = field.getAnnotation(FindBy.class);
    if (ans == null && findBy != null) {
      ans = buildByFromFindBy(findBy);
    }
    
    ZetaFindBy zetaFindBy = field.getAnnotation(ZetaFindBy.class);
    if (ans == null && zetaFindBy != null) {
        ans = buildByFromZetaFindBy(zetaFindBy);
      }

    if (ans == null) {
      ans = buildByFromDefault();
    }

    if (ans == null) {
      throw new IllegalArgumentException("Cannot determine how to locate element " + field);
    }

    return ans;
  }

  protected By buildByFromDefault() {
    return new ByIdOrName(field.getName());
  }

  protected By buildByFromFindBys(FindBys findBys) {
    assertValidFindBys(findBys);

    FindBy[] findByArray = findBys.value();
    By[] byArray = new By[findByArray.length];
    for (int i = 0; i < findByArray.length; i++) {
      byArray[i] = buildByFromFindBy(findByArray[i]);
    }

    return new ByChained(byArray);
  }

  protected By buildBysFromFindByOneOf(FindAll findBys) {
    assertValidFindAll(findBys);

    FindBy[] findByArray = findBys.value();
    By[] byArray = new By[findByArray.length];
    for (int i = 0; i < findByArray.length; i++) {
      byArray[i] = buildByFromFindBy(findByArray[i]);
    }

    return new ByAll(byArray);
  }

  protected By buildByFromFindBy(FindBy findBy) {
    assertValidFindBy(findBy);

    By ans = buildByFromShortFindBy(findBy);
    if (ans == null) {
      ans = buildByFromLongFindBy(findBy);
    }

    return ans;
  }

  protected By buildByFromZetaFindBy(ZetaFindBy findBy) {
	    How how = findBy.how();
	    String locatorsDb = findBy.locatorsDb();
	    String locatorKey = findBy.locatorKey();

		String locatorValue = getLocatorValue(locatorsDb, locatorKey);
	    switch (how) {
	      case CLASS_NAME:
	        return By.className(locatorValue);

	      case CSS:
	        return By.cssSelector(locatorValue);

	      case ID:
	        return By.id(locatorValue);

	      case ID_OR_NAME:
	        return new ByIdOrName(locatorValue);

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

	      default:
	        // Note that this shouldn't happen (eg, the above matches all
	        // possible values for the How enum)
	        throw new IllegalArgumentException("Cannot determine how to locate element " + field);
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
	
  protected By buildByFromLongFindBy(FindBy findBy) {
    How how = findBy.how();
    String using = findBy.using();

    switch (how) {
      case CLASS_NAME:
        return By.className(using);

      case CSS:
        return By.cssSelector(using);

      case ID:
        return By.id(using);

      case ID_OR_NAME:
        return new ByIdOrName(using);

      case LINK_TEXT:
        return By.linkText(using);

      case NAME:
        return By.name(using);

      case PARTIAL_LINK_TEXT:
        return By.partialLinkText(using);

      case TAG_NAME:
        return By.tagName(using);

      case XPATH:
        return By.xpath(using);

      default:
        // Note that this shouldn't happen (eg, the above matches all
        // possible values for the How enum)
        throw new IllegalArgumentException("Cannot determine how to locate element " + field);
    }
  }

  protected By buildByFromShortFindBy(FindBy findBy) {
    if (!"".equals(findBy.className()))
      return By.className(findBy.className());

    if (!"".equals(findBy.css()))
      return By.cssSelector(findBy.css());

    if (!"".equals(findBy.id()))
      return By.id(findBy.id());

    if (!"".equals(findBy.linkText()))
      return By.linkText(findBy.linkText());

    if (!"".equals(findBy.name()))
      return By.name(findBy.name());

    if (!"".equals(findBy.partialLinkText()))
      return By.partialLinkText(findBy.partialLinkText());

    if (!"".equals(findBy.tagName()))
      return By.tagName(findBy.tagName());

    if (!"".equals(findBy.xpath()))
      return By.xpath(findBy.xpath());

    // Fall through
    return null;
  }

  private void assertValidAnnotations() {
    FindBys findBys = field.getAnnotation(FindBys.class);
    FindAll findAll = field.getAnnotation(FindAll.class);
    FindBy findBy = field.getAnnotation(FindBy.class);
    if (findBys != null && findBy != null) {
      throw new IllegalArgumentException("If you use a '@FindBys' annotation, " +
           "you must not also use a '@FindBy' annotation");
    }
    if (findAll != null && findBy != null) {
      throw new IllegalArgumentException("If you use a '@FindAll' annotation, " +
           "you must not also use a '@FindBy' annotation");
    }
    if (findAll != null && findBys != null) {
      throw new IllegalArgumentException("If you use a '@FindAll' annotation, " +
           "you must not also use a '@FindBys' annotation");
    }
  }

  private void assertValidFindBys(FindBys findBys) {
    for (FindBy findBy : findBys.value()) {
      assertValidFindBy(findBy);
    }
  }

  private void assertValidFindAll(FindAll findBys) {
    for (FindBy findBy : findBys.value()) {
      assertValidFindBy(findBy);
    }
  }

  private void assertValidFindBy(FindBy findBy) {
    if (findBy.how() != null) {
      if (findBy.using() == null) {
        throw new IllegalArgumentException(
            "If you set the 'how' property, you must also set 'using'");
      }
    }

    Set<String> finders = new HashSet<String>();
    if (!"".equals(findBy.using())) finders.add("how: " + findBy.using());
    if (!"".equals(findBy.className())) finders.add("class name:" + findBy.className());
    if (!"".equals(findBy.css())) finders.add("css:" + findBy.css());
    if (!"".equals(findBy.id())) finders.add("id: " + findBy.id());
    if (!"".equals(findBy.linkText())) finders.add("link text: " + findBy.linkText());
    if (!"".equals(findBy.name())) finders.add("name: " + findBy.name());
    if (!"".equals(findBy.partialLinkText()))
      finders.add("partial link text: " + findBy.partialLinkText());
    if (!"".equals(findBy.tagName())) finders.add("tag name: " + findBy.tagName());
    if (!"".equals(findBy.xpath())) finders.add("xpath: " + findBy.xpath());

    // A zero count is okay: it means to look by name or id.
    if (finders.size() > 1) {
      throw new IllegalArgumentException(
          String.format("You must specify at most one location strategy. Number found: %d (%s)",
              finders.size(), finders.toString()));
    }
  }
}
