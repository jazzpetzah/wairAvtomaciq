Feature: Settings

  @C669 @regression @rc
  Scenario Outline: Open and Close settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    Then I see settings page
    When I press back button
    Then I see Conversations list with no conversations

    Examples:
      | Name      |
      | user1Name |

  @C670 @regression @rc
  Scenario Outline: Check About page in settings menu
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "About" settings menu item
    Then I see "Wire website" settings menu item

    Examples:
      | Name      |
      | user1Name |

  @C679 @regression @rc @legacy
  Scenario Outline: Change user picture with gallery image
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I take screenshot
    And I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Picture" settings menu item
    And I tap Gallery Camera button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I press Back button
    And I press Back button
    Then I verify the previous and the current screenshots are different

    Examples:
      | Name      |
      | user1Name |

  @C691 @regression @rc @legacy
  Scenario Outline: I can change my name
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "<Name>" settings menu item
    And I commit my new name "<NewName>"
    Then I see "<NewName>" settings menu item

    Examples:
      | Name      | NewName     |
      | user1Name | NewTestName |

  @C678 @regression @rc
  Scenario Outline: Change user picture using camera
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I take screenshot
    And I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Picture" settings menu item
    And I tap Take Photo button on Take Picture view
    And I tap Confirm button on Take Picture view
    And I press Back button
    And I press Back button
    Then I verify the previous and the current screenshots are different

    Examples:
      | Name      |
      | user1Name |

  @C150018 @rc @regression @useSpecialEmail
  Scenario Outline: Verify you can add an email from settings
    Given There is 1 user with phone number only where <Name> is me
    Given I sign in using my phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Add email" settings menu item
    And I start listening for confirmation email <NewEmail> with mailbox password <Password>
    And I commit my new email "<NewEmail>" with password <Password>
    And I verify email <NewEmail>
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    When I sign in using my email
      # Workaround
    And I accept First Time overlay as soon as it is visible
    Then I see Conversations list with no conversations

    Examples:
      | Name      | NewEmail   | Password      |
      | user1Name | user1Email | user1Password |

  @C150020 @rc @regression @useSpecialEmail
  Scenario Outline: Verify you can change an email from settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "<CurrentEmail>" settings menu item
    And I start listening for confirmation email <NewEmail> with mailbox password <Password>
    And I commit my new email "<NewEmail>"
    And I verify email <NewEmail>
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    When I sign in using my email
      # Workaround
    And I accept First Time overlay as soon as it is visible
    Then I see Conversations list with no conversations

    Examples:
      | Name      | CurrentEmail | NewEmail   | Password    |
      | user1Name | user1Email   | user2Email | NewPassword |

  @C165103 @regression
  Scenario Outline: Verify I can delete multiple devices without filling password every time
    Given There is 1 user where <Name> is me
    Given User <Name> adds new devices Device1,<DeviceToRemove>,<DeviceToRemoveWithoutPassword>,<OtherDevice>,Device5
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Devices" settings menu item
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
    Then I do not see Manage Devices overlay
    And I see Conversations list with no conversations

    Examples:
      | Name      | Password      | DeviceToRemoveWithoutPassword | DeviceToRemove | OtherDevice |
      | user1Name | user1Password | Device2                       | Device3        | Device4     |

  @C150017 @regression
  Scenario Outline: Verify you can add a phone number from settings
    Given There is 1 user with email address only where <Name> is me
    Given I sign in using my email
    Given I postpone Add Phone Number action
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "Add phone number" settings menu item
    And I commit my new phone number "<NewNumber>"
    And I commit verification code for phone number <NewNumber>
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    When I sign in using my phone number
    Then I see Conversations list with no conversations

    Examples:
      | Name      | NewNumber        |
      | user1Name | user1PhoneNumber |

  @C150019 @regression
  Scenario Outline: Verify you can change a phone number from settings
    Given There is 1 user where <Name> is me
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with no conversations
    When I tap conversations list settings button
    And I select "Account" settings menu item
    And I select "<CurrentNumber>" settings menu item
    And I commit my new phone number "<NewNumber>"
    And I commit verification code for phone number <NewNumber>
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    When I sign in using my phone number
    Then I see Conversations list with no conversations

    Examples:
      | Name      | CurrentNumber    | NewNumber        |
      | user1Name | user1PhoneNumber | user2PhoneNumber |

  @C250836 @regression
  Scenario Outline: I can enable/disable send button in Settings
    Given There is 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap conversations list settings button
    # Disable at first
    And I select "Options" settings menu item
    And I select "Send button" settings menu item
    And I press Back button 2 times
    And I see Conversations list
    And I tap on conversation name <Contact>
    And I type the message "<Message1>" and send it by keyboard Send button
    Then I see the message "<Message1>" in the conversation view
    # Enable now
    When I navigate back from conversation
    And I tap conversations list settings button
    And I select "Options" settings menu item
    And I select "Send button" settings menu item
    And I press Back button 2 times
    And I see Conversations list
    And I tap on conversation name <Contact>
    And I type the message "<Message2>" and send it by cursor Send button
    Then I see the message "<Message2>" in the conversation view

    Examples:
      | Name      | Contact   | Message1 | Message2 |
      | user1Name | user2Name | Yo       | NoPb     |