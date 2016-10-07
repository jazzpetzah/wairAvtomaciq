Feature: Sign In

  @C2098 @e2ee @smoke
  Scenario Outline: Verify current browser is set as permanent device
    Given There are 3 users where <Name> is me
    Given user <Contact1> adds a new device Device1 with label Label1
    Given user <Contact2> adds a new device Device1 with label Label1
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    When I Sign in using login <Email> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact1>
    And Contact <Contact1> sends message <Message1> to user Myself
    And User <Contact1> sends image <ImageName1> to single user conversation Myself
    Then I see text message <Message1>
    And I see sent picture <ImageName1> in the conversation view
    When I open conversation with <GroupChatName>
    And Contact <Contact2> sends message <Message2> to group conversation <GroupChatName>
    And User <Contact2> sends image <ImageName1> to group conversation <GroupChatName>
    Then I see text message <Message2>
    And I see sent picture <ImageName1> in the conversation view
    When I open preferences by clicking the gear button
    And I open devices in preferences
    And I remember the device id of the current device
    And I close preferences
    And I wait for 2 seconds
    And I open preferences by clicking the gear button
    And I open account in preferences
    And I click logout in account preferences
    And I see the clear data dialog
    And I click Logout button on clear data dialog
    And I see Sign In page
    And I Sign in using login <Email> and password <Password>
    Then I am signed in properly
    And I open conversation with <Contact1>
    And I see text message <Message1>
    And I see sent picture <ImageName1> in the conversation view
    And I open conversation with <GroupChatName>
    And I see text message <Message2>
    And I see sent picture <ImageName1> in the conversation view
    When I open preferences by clicking the gear button
    And I select Settings menu item on self profile page
    Then I verify that the device id of the current device is the same
    And I see 0 devices in the devices section

    Examples:
      | Email      | Password      | Name      | Contact1  | Contact2  | GroupChatName | Message1   | Message2     | ImageName1                |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat     | Hello 1:1! | Hello Group! | userpicture_landscape.jpg |

  @C2099 @e2ee @regression
  Scenario Outline: Verify current browser is set as temporary device
    Given There is 1 user where <Name> is me
    Given I switch to Sign In page
    Given I see Sign In page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then I see the history info page
    When I click confirm on history info page
    Then I am signed in properly
    When I open preferences by clicking the gear button
    And I select Settings menu item on self profile page
    And I remember the device id of the current device
    And I click close settings page button
    And I wait for 2 seconds
    And I open preferences by clicking the gear button
    And I select Log out menu item on self profile page
    And I see Sign In page
    And I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    And I see the history info page
    And I click confirm on history info page
    Then I am signed in properly
    When I open preferences by clicking the gear button
    And I select Settings menu item on self profile page
    Then I verify that the device id of the current device is not the same
    And I see 0 devices in the devices section

    Examples:
      | Email      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C1737 @smoke
  Scenario Outline: Verify sign in error appearance in case of wrong credentials
    Given There is 1 user where user1Name is me
    Given I switch to sign in page
    When I enter email "<Email>"
    And I enter password "<Password>"
    And I press Sign In button
    Then the sign in error message reads <Error>
    And the email field on the sign in form is marked as error
    And the password field on the sign in form is marked as error

    Examples: 
      | Email      | Password      | Error                                     |
      | user1Email | wrongPassword | Please verify your details and try again. |

  @C1682 @regression
  Scenario Outline: Verify sign in button is disabled in case of empty credentials
    Given There is 1 user where user1Name is me
    When I switch to sign in page
    Then Sign In button is disabled
    When I enter email "<Email>"
    And I enter password ""
    Then Sign In button is disabled
    When I enter email ""
    And I enter password "<Password>"
    Then Sign In button is disabled

    Examples: 
      | Email      | Password      |
      | user1Email | user1Password |

  @C246200 @regression
  Scenario Outline: Verify you can sign in by phone number with already set password and temporary device
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter password <Password> on phone login page
    And I press Sign In button on phone login page
    And I see the history info page
    And I click confirm on history info page
    Then I am signed in properly
    And I see username <Name> in account preferences
    And I see user phone number <PhoneNumber> in account preferences

    Examples: 
      | Name      | PhoneNumber      | Password   |
      | user1Name | user1PhoneNumber | aqa123456! |

  @C246197 @regression
  Scenario Outline: Verify you can sign in by phone number with already set password and permanent device
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I switch to phone number sign in page
    And I check option to remember me on phone login page
    And I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter password <Password> on phone login page
    And I press Sign In button on phone login page
    Then I am signed in properly
    And I see username <Name> in account preferences
    And I see user phone number <PhoneNumber> in account preferences

    Examples: 
      | Name      | PhoneNumber      | Password   |
      | user1Name | user1PhoneNumber | aqa123456! |

  @C246199 @regression
  Scenario Outline: Verify you can sign by phone number on email signin page
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I enter phone number "<PhoneNumber>"
    And I enter password "<Password>"
    And I check option to remember me
    And I press Sign In button
    Then I am signed in properly
    And I see username <Name> in account preferences
    And I see user phone number <PhoneNumber> in account preferences

    Examples: 
      | Name      | Email      | PhoneNumber      | Password   |
      | user1Name | user1Email | user1PhoneNumber | aqa123456! |

  @C246201 @regression
  Scenario Outline: Verify I see a proper error when I try to sign in with invalid phone number
    Given I switch to sign in page
    When I switch to phone number sign in page
    And I enter country code <CountryCode> on phone number sign in
    And I enter phone number <PhoneNumber> on phone number sign in
    And I click on sign in button on phone number sign in
    Then I see invalid phone number error message saying <Error>

    Examples: 
      | CountryCode | PhoneNumber | Error                                  |
      | +49         | 9999999999  | Sorry. This phone number is forbidden. |
      | +49         | 1qwerqwer   | Invalid Phone Number                   |
      | +49         | 1!@$!@$     | Invalid Phone Number                   |

  @C1789 @mute
  Scenario Outline: Verify you see correct error message when sign in with a phone number with incorrect code
    Given There is 1 user where <Name> is me
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter wrong phone verification code for user <Name>
    Then I see invalid phone code error message saying <Error>

    Examples: 
      | Name      | Error        |
      | user1Name | Invalid Code |

  @C246198 @regression
  Scenario Outline: Verify you are asked to add an email address after sign in with a phone number and unset password
    Given There is 1 user where <Name> is me with phone number only
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter phone verification code for emailless user <Name>
    And I enter email address <EmailOfOtherUser> on add email address dialog
    And I enter password <PasswordOfOtherUser> on add email address dialog
    And I click add button on add email address dialog
    Then I see error message on add email address dialog saying <ErrorAlready>
    And the email field on add email address dialog is marked as error
    When I enter email address <InvalidEmail> on add email address dialog
    And I enter password <PasswordOfOtherUser> on add email address dialog
    And I click add button on add email address dialog
    Then I see error message on add email address dialog saying <ErrorInvalidEmail>
    And the email field on add email address dialog is marked as error
    When I enter email of user <Name> on add email address dialog
    And I enter password <InvalidPassword> on add email address dialog
    And I click add button on add email address dialog
    Then I see error message on add email address dialog saying <ErrorInvalidPassword>
    And the password field on add email address dialog is marked as error
    When I enter email of user <Name> on add email address dialog
    And I enter password <PasswordOfOtherUser> on add email address dialog
    And I click add button on add email address dialog
    Then I verify that an envelope icon is shown

    Examples: 
      | Name      | EmailOfOtherUser      | PasswordOfOtherUser | ErrorAlready                | InvalidEmail | ErrorInvalidEmail                   | InvalidPassword | ErrorInvalidPassword                          |
      | user1Name | qa1+qa1@wearezeta.com | aqa123456!          | Email address already taken | @example.com | Please enter a valid email address. | 123             | Choose a password with at least 8 characters. |

  @C1849 @mute
  Scenario Outline: Verify you can verify added email later when sign in with a phone number
    Given There is 1 user where <Name> is me with phone number only
    Given I switch to sign in page
    When I switch to phone number sign in page
    When I sign in using phone number of user <Name>
    And I click on sign in button on phone number sign in
    And I enter phone verification code for emailless user <Name>
    And I enter email of user <Name> on add email address dialog
    And I enter password <PasswordOfOtherUser> on add email address dialog
    Then I click add button on add email address dialog
    And I verify that an envelope icon is shown
    When I click on Verify later button on Verification page
    Then I am signed in properly
    And I see first time experience with watermark

    Examples: 
      | Name      | PasswordOfOtherUser |
      | user1Name | aqa123456!          |

  @C125734 @regression
  Scenario Outline: Verify you see a description about Wire when being redirected from get.wire.com
    Given I switch to sign in page
    Given I open <Language> login page as if I was redirected from get.wire.com
    Then I see Registration page
    And I verify text about Wire is visible
    And I see intro about Wire saying <TextWire>

    Examples:
      | Language | TextWire                                                                                   |
      | english  | Simple, private & secure messenger for chat, calls, sharing pics, music, videos, GIFs and more.       |
      | german   | Ein moderner und sicherer Messenger für Unterhaltungen, Anrufe, Bilder, Musik, Videos, GIFs und mehr. |
