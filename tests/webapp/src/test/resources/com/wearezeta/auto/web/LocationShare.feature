Feature: Location Share

  @C150033 @regression
  Scenario Outline: Verify you can see location sent from mobile and delete it from conversation view
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And User <Contact1> sends location <LocationName> with <Latitude> and <Longitude> to group conversation <ChatName> via device Device1
    Then I see location message <LocationName> with <Latitude> and <Longitude> in the conversation view
    When I click context menu of the latest message
    And I click to delete message for me in context menu
    And I click confirm to delete message for me
    Then I do not see location message <LocationName> with <Latitude> and <Longitude> in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | Latitude | Longitude | LocationName |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | 12.94    | 54.29     | Stralsund    |