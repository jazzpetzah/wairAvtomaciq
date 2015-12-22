Feature: Sign In

  @C382 @id326 @regression
  Scenario Outline: Sign in to Wire by mail
    Given There is 1 user where <Name> is me
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    Then I see Contact list with no contacts

    Examples:
      | Login      | Password      | Name      |
      | user1Email | user1Password | user1Name |

  @C433 @id3245 @regression
  Scenario Outline: Sign in to Wire by phone
    Given There are 1 users where <Name> is me
    When I sign in using my phone number
    Then I see Contact list with no contacts

    Examples:
      | Name      |
      | user1Name |

  @C337 @id209 @regression
  Scenario Outline: (AN-3115) I can change sign in user
    Given There are 2 users where <Name> is me
    Given I sign in using my email or phone number
    Given I see Contact list with no contacts
    When I tap on my avatar
    Then I see personal info page
    When I tap options button
    And I tap settings button
    And I select "Account" settings menu item
    And I select "Log out" settings menu item
    And I confirm sign out
    Then I see welcome screen
    When User <Name2> is me
    And I sign in using my email or phone number
    Then I see Contact list with no contacts
    When I tap on my avatar
    Then I see personal info page

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
    Then I see error message "<ErrMessage>"

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
    Then I see error message "<ErrMessage>"
    When I accept the error message
    And I disable Airplane mode on the device
    And I press Log in button
    Then I see Contact list with contacts

    Examples:
      | Name      | Email      | Password      | Contact   | ErrMessage                                           |
      | user1Name | user1Email | user1Password | user2Name | Please check your Internet connection and try again. |