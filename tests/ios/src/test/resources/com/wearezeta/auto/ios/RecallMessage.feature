Feature: Recall Message

  @C202306 @staging @fastLogin
  Scenario Outline: Verify I can delete my message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <HisDevice>
    Given User Myself adds new device <MySecondDevice>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    When User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    And I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for everyone item from Delete menu
    Then I see 0 default messages in the conversation view
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in 15 seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in 3 seconds

    Examples:
      | Name      | Contact   | HisDevice | MySecondDevice |
      | user1Name | user2Name | device1   | device2        |

  @C202318 @staging @fastLogin
  Scenario Outline: Verify delete everywhere works for file sharing
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <HisDevice>
    Given User Myself adds new device <MySecondDevice>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    And I tap file transfer menu item <FileName>
    # Wait to be ready uploading for slower jenkins slaves
    And I wait for 10 seconds
    And User <Contact> remembers the recent message from user Myself via device <HisDevice>
    And User Myself remembers the recent message from user <Contact> via device <MySecondDevice>
    And I long tap on file transfer placeholder in conversation view
    And I tap on Delete badge item
    And I select Delete for everyone item from Delete menu
    Then I do not see file transfer placeholder
    And User <Contact> sees the recent message from user Myself via device <HisDevice> is changed in 15 seconds
    And User Myself sees the recent message from user <Contact> via device <MySecondDevice> is changed in 3 seconds

    Examples:
      | Name      | Contact   | HisDevice | MySecondDevice | FileName                   |
      | user1Name | user2Name | device1   | device2        | FTRANSFER_MENU_DEFAULT_PNG |
