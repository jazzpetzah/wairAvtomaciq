Feature: Localytics

  @torun
  Scenario Outline: Verify stats sent by the application
    Given There is 1 user where <Name> is me
    # Given There are 3 users where <Name> is me
    # Given Myself is connected to <Contact1>,<Contact2>
    # Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    And I take snapshot of session event count
    Given I Sign in using login <Login> and password <Password>
    Then I verify the count of session event has been increased within 60 seconds

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat |
