Feature: Sign In

  @C382 @id326 @regression
  Scenario Outline: Sign in to Wire by mail
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    And I accept First Time overlay as soon as it is visible
    Then I see Contact list with no contacts

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C43808 @id3245 @rc @regression
  Scenario Outline: Sign in to Wire by phone
    Given There are 1 users where <Name> is me
    When I sign in using my phone number
    Then I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |

  @C337 @id209 @regression
  Scenario Outline: I can change sign in user
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Log out" settings menu item
    And I confirm sign out
    When User <Name2> is me
    And I sign in using my email or phone number
    And I accept First Time overlay as soon as it is visible
    Then I see Contact list with no contacts

    Examples:
      | Name      | Name2     |
      | user1Name | user2Name |

  @C707 @id1413 @regression @rc
  Scenario Outline: User should be notified if the details he entered on the sign in screen are incorrect
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then I see alert message containing "<ErrMessage>" in the body

    Examples:
      | Login | Password  | ErrMessage                          |
      | aaa   | aaabbbccc | Please enter a valid email address. |

  @C668 @id52 @regression @rc
  Scenario Outline: (CM-623) Verify Sign In progress behaviour while there are problems with internet connectivity
    Given There are 2 users where <Name> is me
    Given <Contact> is connected to Myself
    Given I see welcome screen
    Given I switch to email sign in screen
    When I enable Airplane mode on the device
    And I have entered login <Email>
    And I have entered password <Password>
    And I press Log in button
    Then I see alert message containing "<ErrMessage>" in the body
    When I disable Airplane mode on the device
    And I accept the error message
    And I press Log in button
    And I accept First Time overlay as soon as it is visible
    Then I see Contact list with contacts

    Examples:
      | Name      | Email      | Password      | Contact   | ErrMessage                                           |
      | user1Name | user1Email | user1Password | user2Name | Please check your Internet connection and try again. |

  @C43807 @rc @regression
  Scenario Outline: Verify sign in with email address only
    Given There is 1 user with email address only where <Name> is me
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    And I input a new phone number for user <Name>
    And I input the verification code
    Then I see Contact list with no contacts

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C43810 @rc @regression
  Scenario Outline: Verify you can skip phone number input
    Given There is 1 user with email address only where <Name> is me
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    And I postpone Add Phone Number action
    And I accept First Time overlay as soon as it is visible
    Then I see Contact list with no contacts

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C131213 @rc @rc42 @regression
  Scenario Outline: Verify you see first time usage overlay on first login by mail
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I see First Time overlay
    Given I tap Got It button on First Time overlay
    Given I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |

  @C3230 @regression @C145960
  Scenario Outline: AN-4162 When I see too many devices screen - I can remove devices without filling password every time and log in successfully
    Given There is 1 user where <Name> is me
    Given User <Name> adds new devices <DeviceToRemove>,<DeviceToRemoveWithoutPassword>,<OtherDevice>,Device4,Device5,Device6,Device7
    # Workaround for AN-3281
    # Given I sign in using my email or phone number
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    When I see Manage Devices overlay
    And I tap Manage Devices button on Manage Devices overlay
    And I select "<DeviceToRemove>" settings menu item
    And I select "Remove device" settings menu item
    And I enter <Password> into the device removal password confirmation dialog
    And I tap OK button on the device removal password confirmation dialog
    # Delete device will take time, should verify at first it already return back to device list view, also the list is already updated
    And I wait for 5 seconds
    And I see "<DeviceToRemoveWithoutPassword>" settings menu item
    And I do not see "<DeviceToRemove>" settings menu item
    # C145960
    And I select "<DeviceToRemoveWithoutPassword>" settings menu item
    And I select "Remove device" settings menu item
    And I see "<OtherDevice>" settings menu item
    And I do not see "<DeviceToRemoveWithoutPassword>" settings menu item
    And I press Back button 2 times
    When I do not see Manage Devices overlay
    Then I see Contact list with no contacts

    Examples:
      | Password      | Name      | DeviceToRemoveWithoutPassword | DeviceToRemove | OtherDevice |
      | user1Password | user1Name | Device1                       |  Device2       | Device3     |