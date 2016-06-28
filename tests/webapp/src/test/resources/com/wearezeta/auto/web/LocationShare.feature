Feature: Location Share

  @C150033 @staging
  Scenario Outline: Verify you can see location sent from mobile and delete it from conversation view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And User <Contact1> sends location <LocationName> with <Longitude> and <Latitude> to group conversation <ChatName> via device Device1
    Then I see location message <LocationName> with <Longitude> and <Latitude> in the conversation view
    When I click to delete the latest message
    And I click confirm to delete message
    Then I do not see location message <LocationName> with <Longitude> and <Latitude> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | Longitude  | Latitude  | LocationName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | 54.2954593 | 12.945651 | Stralsund    |