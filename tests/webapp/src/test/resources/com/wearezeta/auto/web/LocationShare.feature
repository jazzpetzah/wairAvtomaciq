Feature: Location Share

  @C150024 @staging
  Scenario Outline: Verify you can see location shared from mobile
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    Given I am signed in properly
    Given I see Contact list with name <ChatName>
    When I open conversation with <ChatName>
    And User <Contact1> sends location <LocationName> with <Longitude> and <Latitude> to group conversation <ChatName> via device Device1
    #Then I see location message in the conversation view

    Examples:
      | Login      | Password      | Name      | Contact1  | Contact2  | ChatName  | Longitude  | Latitude   | LocationName     |
      | user1Email | user1Password | user1Name | user2Name | user3Name | GroupChat | 52.5162731 | 13.3601958 | Brandenburg Gate |