package com.wearezeta.auto.android.pages;

import java.awt.image.BufferedImage;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.wearezeta.auto.android.locators.AndroidLocators;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.ImageUtil;
import com.wearezeta.auto.common.driver.SwipeDirection;
import com.wearezeta.auto.common.locators.ZetaFindBy;
import com.wearezeta.auto.common.locators.ZetaHow;

public class OtherUserPersonalInfoPage extends AndroidPage {

	public static final String REMOVE_FROM_CONVERSATION_BUTTON = "Remove";
	private final double MIN_ACCEPTABLE_IMAGE_VALUE = 0.75;
	public static final String LEAVE_CONVERSATION_BUTTON = "Leave conversation";
	public static final String LEAVE_BUTTON = "LEAVE";
	private static final String AVATAR_WITH_IMAGE = "avatarPictureTestAndroid.png";
	private static final String AVATAR_NO_IMAGE = "avatarTestAndroid.png";
	
	@FindBy(how = How.XPATH, using = AndroidLocators.OtherUserPersonalInfoPage.xpathGroupChatInfoLinearLayout)
	private List<WebElement> linearLayout;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.OtherUserPersonalInfoPage.classNameGridView)
	private WebElement groupChatUsersGrid;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsHeader")
	private WebElement groupChatName;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsHeader")
	private List<WebElement> otherUserName;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsSubHeader")
	private List<WebElement> otherUserMail;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idOtherUserPersonalInfoSingleName")
	private List<WebElement> otherUserSingleName;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idOtherUserPersonalInfoSingleMail")
	private List<WebElement> otherUserSingleMail;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idUserProfileConfirmationMenu")
	private WebElement confirmMenu;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idBlockButton")
	private WebElement blockButton; 
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idRightActionButton")
	private WebElement rightConversationButton; 
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idRenameButton")
	private WebElement renameButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idArchiveButton")
	private WebElement archiveButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idLeaveButton")
	private WebElement leaveButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idSilenceButton")
	private WebElement silenceButton;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idUnblockBtn")
	private WebElement unblockButton; 
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameFrameLayout)
	private WebElement frameLayout;
	
	@FindBy(how = How.CLASS_NAME, using = AndroidLocators.CommonLocators.classNameLoginPage)
	private WebElement backGround;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.CommonLocators.CLASS_NAME, locatorKey = "idConfirmBtn")
	private WebElement confirmBtn;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idLeftActionButton")
	private WebElement addContactBtn;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.OtherUserPersonalInfoPage.CLASS_NAME, locatorKey = "idParticipantsSubHeader")
	private WebElement participantsSubHeader;
	
	@ZetaFindBy(how = ZetaHow.ID, locatorsDb = AndroidLocators.ConnectToPage.CLASS_NAME, locatorKey = "idConnectToHeader")
	private List<WebElement> connectToHeader;
	
	private String url;
	private String path;
	
	public OtherUserPersonalInfoPage(String URL, String path)
			throws Exception {
		super(URL, path);
		this.url = URL;
		this.path = path;
	}

	public void pressRightConversationButton(){
		refreshUITree();//TODO workaround
		wait.until(ExpectedConditions.elementToBeClickable(rightConversationButton));
		rightConversationButton.click();
	}
	
	public ContactListPage pressLeaveButton() throws Exception{
		refreshUITree();
		leaveButton.click();
		return new ContactListPage(url, path);
	}
	
	public void pressSilenceButton() {
		refreshUITree();
		silenceButton.click();
	}
	
	public void clickBlockBtn() {
		refreshUITree();
		rightConversationButton.click();
	}
	
	public AndroidPage clickUnblockBtn() throws Exception {
		unblockButton.click();
		return new DialogPage(url, path);
	}
	
	public boolean isUnblockBtnVisible() {
		return unblockButton.isDisplayed();
	}
	
	@Override
	public AndroidPage returnBySwipe(SwipeDirection direction)
			throws Exception {
		AndroidPage page = null;
		switch (direction){
			case DOWN:
			{
				page = new PeoplePickerPage(url, path);
				break;
			}
			case UP:
			{
				break;
			}
			case LEFT:
			{
				break;
			}
			case RIGHT:
			{
				break;
			}
		}	
		return page;
	}
	
	public boolean isOtherUserNameVisible(String name) {
		refreshUITree();//workaround to refresh UI tree
		String text;
		if(otherUserName.size() > 0){
			text = otherUserName.get(0).getText().toLowerCase();
		}
		else{
			text = otherUserSingleName.get(0).getText().toLowerCase();
		}
		return text.equals(name.toLowerCase());
	}
	
	public boolean isOtherUserMailVisible(String mail) {
		refreshUITree();//workaround to refresh UI tree
		String text;
		if(otherUserName.size() > 0){
			text = otherUserMail.get(0).getText().toLowerCase();
		}
		else{
			text = otherUserSingleMail.get(0).getText().toLowerCase();
		}
		return text.equals(mail.toLowerCase());
	}

	public boolean isRemoveFromConversationAlertVisible() {
		return confirmMenu.isDisplayed();
	}
	
	public OtherUserPersonalInfoPage pressRemoveConfirmBtn() throws Exception
	{
		refreshUITree();//TODO workaround
		wait.until(ExpectedConditions.elementToBeClickable(confirmBtn));
		confirmBtn.click();
		return new OtherUserPersonalInfoPage(url, path);
	}

	public PeoplePickerPage tapAddContactBtn() throws Exception {
		 addContactBtn.click();
		return new PeoplePickerPage(url, path);
	}

	public boolean isBackGroundImageCorrect(String imageName) throws Exception {
		BufferedImage bgImage = null;
		boolean flag = false;
		bgImage = getElementScreenshot(backGround);
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		BufferedImage realImage = ImageUtil.readImageFromFile(path+imageName);
		double score = ImageUtil.getOverlapScore(realImage, bgImage);
		System.out.println(score);
		if (score >= MIN_ACCEPTABLE_IMAGE_VALUE) {
			flag = true;
		}
		return flag;
	}
	
	public boolean isContactExists(String contact) {
		boolean flag = true;
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(groupChatName));
		List<WebElement> cn = driver.findElements(By.xpath(String.format(AndroidLocators.ContactListPage.xpathContacts, contact.toUpperCase())));
		if(cn.isEmpty()){
			flag = false;
		}
		return flag;
	}	
	
	public AndroidPage selectContactByName(String contactName) throws Exception, InterruptedException {
		boolean flag = false;
		refreshUITree();

		for(WebElement user : linearLayout) {
			List<WebElement> elements = user.findElements(By.className(AndroidLocators.CommonLocators.classNameTextView));
			for(WebElement element : elements) {
				if(element.getText() != null && element.getText().equals((contactName.toUpperCase()))){
					user.click();
					flag = true;
					break;
				}
			}
			if(flag){
				break;
			}
		}
		if(connectToHeader.size()>0){
			return new ConnectToPage(url, path);
		}
		else{
			return new OtherUserPersonalInfoPage(url, path);
		}
	}
	
	public void renameGroupChat(String chatName){
		groupChatName.sendKeys(chatName + "\n");
	}
	
	public AndroidPage tapOnContact(String contact) throws Exception {
		refreshUITree();
		wait.until(ExpectedConditions.visibilityOf(groupChatName));
		WebElement cn = driver.findElement(By.xpath(String.format(AndroidLocators.ContactListPage.xpathContacts, contact.toUpperCase())));
		cn.click();
		refreshUITree();
		if(connectToHeader.size()>0){
			return new ConnectToPage(url, path);
		}
		else{
			return new OtherUserPersonalInfoPage(url, path);
		}
	}
	
	public String getSubHeader() {
		return participantsSubHeader.getText();
	}

	public String getConversationName() {
		refreshUITree();
		return groupChatName.getText();
	}
	
	public DialogPage tabBackButton() throws Exception {
		driver.navigate().back();
		return new DialogPage(url, path);
	}

	public boolean isParticipantAvatars(String contact1, String contact2) throws Exception {
		boolean flag1 = false;
		boolean flag2 = false;
		boolean commonFlag = false;
		BufferedImage avatarIcon = null;
		refreshUITree();
		String path = CommonUtils.getImagesPath(CommonUtils.class);
		for(int i = 1; i < linearLayout.size()+1;i++){
			avatarIcon = getElementScreenshot(driver.findElement(By.xpath(String.format(AndroidLocators.OtherUserPersonalInfoPage.xpathGroupChatInfoLinearLayoutId, i))));
			String avatarName = driver.findElement(By.xpath(String.format(AndroidLocators.OtherUserPersonalInfoPage.xpathGroupChatInfoContacts, i))).getText();
			if(avatarName.equalsIgnoreCase(contact1)){
				BufferedImage realImage = ImageUtil.readImageFromFile(path + AVATAR_WITH_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
				flag1 = true;
			}
			if(contact2.toLowerCase().startsWith(avatarName.toLowerCase())) {
				//must be a yellow user with initials AT
				BufferedImage realImage = ImageUtil.readImageFromFile(path + AVATAR_NO_IMAGE);
				double score = ImageUtil.getOverlapScore(realImage, avatarIcon);
				if (score <= MIN_ACCEPTABLE_IMAGE_VALUE) {
					return false;
				}
				flag2 = true;
			}
		}
		if(flag1 && flag2) {
			commonFlag = true;
		}
		
		return commonFlag;
	}

}
