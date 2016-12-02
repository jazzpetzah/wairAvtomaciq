Feature: Sound

  @C2326 @smoke
  Scenario Outline: I want to hear a sound when I receive a new message in a conversation
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given user <Contact1> adds a new device Device1 with label Label1
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact2>
    Then Soundfile new_message did not start playing
    When Contact <Contact1> sends message Hello1 via device Device1 to user Myself
    Then I see unread dot in conversation <Contact1>
    And Soundfile new_message did start playing

    Examples: 
      | Login      | Password      | Name      | Contact1  | Contact2  |
      | user1Email | user1Password | user1Name | user2Name | user3Name |

  @C2327 @smoke
  Scenario Outline: I want to hear a sound when someone calls me
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given <Contact> starts instance using <CallBackend>
    Given I switch to Sign In page
    Given I Sign in using login <Login> and password <Password>
    And I am signed in properly
    When I open conversation with <Contact>
    Then Soundfile ringing_from_them did not start playing in loop
    When <Contact> calls me
    Then I see the incoming call controls for conversation <Contact>
    Then Soundfile ringing_from_them did start playing in loop
    When <Contact> stops calling me
    Then I do not see the incoming call controls for conversation <Contact>
    And Soundfile ringing_from_them did stop playing

    Examples: 
      | Login      | Password      | Name      | Contact   | CallBackend |
      | user1Email | user1Password | user1Name | user2Name | zcall       |