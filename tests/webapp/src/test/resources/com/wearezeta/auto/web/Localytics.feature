Feature: Localytics

  @torun
  Scenario Outline: Verify stats sent by the application
    # Given There is 1 user where <Name> is me
    # Given There are 3 users where <Name> is me
    # Given Myself is connected to <Contact1>,<Contact2>
    # Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I take snapshot of regFailed:reason=The given e-mail address or phone number is in use. attribute count
    Given I see invitation page
    Given I enter invitation code
    Given I switch to Registration page
    When I enter user name <Name> on Registration page 
    And I enter user email <ExistingEmail> on Registration page 
    And I enter user password <Password> on Registration page 
    And I submit registration form
    # Given I Sign in using login <Login> and password <Password>
    Then I verify the count of regFailed:reason=The given e-mail address or phone number is in use. attribute has been increased within 900 seconds

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | ExistingEmail                 |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | mykola.mokhnach@wearezeta.com |
