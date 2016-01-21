Feature: E2EE

  @C3226 @rc @regression
  Scenario Outline: Verify you can receive encrypted and non-encrypted messages in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When Contact <Contact> sends encrypted message <EncryptedMessage> to user Myself
    And Contact <Contact> sends message <SimpleMessage> to user Myself
    And I tap on contact name <Contact>
    Then I see non-encrypted message <SimpleMessage> 1 time in the conversation view
    And I see encrypted message <EncryptedMessage> 1 time in the conversation view

    Examples:
      | Name      | Contact   | EncryptedMessage | SimpleMessage |
      | user1Name | user2Name | EncryptedYo      | SimpleYo      |

  @C1847 @regression
  Scenario Outline: Verify you can remove extra devices and log in successfully if too many devices are registered for your account
    Given There is 1 user where <Name> is me
    Given User <Name> adds new devices <DeviceToRemove>,Device2,Device3,Device4,Device5,Device6,Device7
    # Workaround for AN-3281
    # Given I sign in using my email or phone number
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    When I see Manage Devices overlay
    And I tap Manage Devices button on Manage Devices overlay
    And I select "<DeviceToRemove>" settings menu item
    And I select "Remove device" settings menu item
    And I see device removal password confirmation dialog
    And I enter <Password> into the device removal password confirmation dialog
    And I tap OK button on the device removal password confirmation dialog
    And I do not see "<DeviceToRemove>" settings menu item
    And I press Back button 3 times
    When I do not see Manage Devices overlay
    Then I see Contact list with no contacts

    Examples:
      | Password      | Name      | DeviceToRemove |
      | user1Password | user1Name | Device1        |

  @C3227 @rc @regression
  Scenario Outline: Verify you can receive encrypted and non-encrypted images in 1:1 chat
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with contacts
    When User <Contact> sends encrypted image <ImageName> to single user conversation Myself
    And User <Contact> sends image <ImageName> to single user conversation Myself
    And I tap on contact name <Contact>
    And I scroll to the bottom of conversation view
    Then I see non-encrypted image 1 time in the conversation view
    And I see encrypted image 1 time in the conversation view

    Examples:
      | Name      | Contact   | ImageName   |
      | user1Name | user2Name | testing.jpg |