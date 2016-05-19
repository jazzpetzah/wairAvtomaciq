Feature: Settings

  @C669 @id67 @regression @rc
  Scenario Outline: Open and Close settings page
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I tap options button
    And I tap settings button
    Then I see settings page
    When I press back button
    Then I see personal info page

    Examples:
      | Name      |
      | user1Name |

  @C376 @id71 @regression
  Scenario Outline: Can not open Settings page when editing user name
    Given There are 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    And I tap conversations list settings button
    And I tap on my name
    And I see edit name field with my name
    When I tap options button
    Then I do not see ABOUT item in Options menu

    Examples:
      | Name      |
      | user1Name |

  @C670 @id92 @regression @rc
  Scenario Outline: Check About page in settings menu
    Given There is 1 user where <Name> is me
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Contact list with no contacts
    When I tap conversations list settings button
    And I tap options button
    And I tap about button
    Then I see About page
    When I tap on About page
    Then I see personal info page

    Examples:
      | Name      |
      | user1Name |

  @C129781 @staging
  Scenario Outline: Deny permissions scenario
    Given There is 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given I see welcome screen
    When I switch to email sign in screen
    And I have entered login <Login>
    And I have entered password <Password>
    And I press Log in button
    And I accept First Time overlay as soon as it is visible
    Then I dismiss security alert
    And I see Contact list with contacts
    When User <Contact1> send message "<Message>" to user Myself
    And I tap new message notification "<Message>"
    Then I see the message "<Message>" in the conversation view
    When I tap on text input
    And I type the message "<Message>" and send it
    Then I see the message "<Message>" in the conversation view
    When I tap Ping button from cursor toolbar
    Then I see Ping message "<PingMsg1>" in the conversation view
    And I tap Ping button from cursor toolbar
    Then I see Ping message "<PingMsg2>" in the conversation view
    When I tap Add picture button from cursor toolbar
    Then I dismiss security alert
    And I press "Gallery" button
    When I tap by coordinates 25:25
    Then I wait for 2 seconds
    Then I dismiss security alert
    When I press "Confirm" button
    And I tap Sketch button from cursor toolbar
    And I draw a sketch with 2 colors
    And I send my sketch
    And I tap the recent picture in the conversation view
    Then I swipe down from 50%
    When I tap Audio Call button from top toolbar
    Then I dismiss security alert
    And I do not see ongoing call
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    When I tap Video Call button from top toolbar
    Then I dismiss security alert
    And I dismiss security alert if it is visible
    And I do not see ongoing video call
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    When I tap on text input
    And I type the message "https://www.youtube.com/watch?v=wTcNtgA6gHs" and send it
    Then I see Play button on Youtube container
    When I tap on text input
    And I type the message "<Message>"
    And I click on the GIF button
    Then I see giphy preview page
    When I click on the giphy send button
    And I scroll to the bottom of conversation view
    Then I see a picture in the conversation view
    Given <Contact1> starts instance using <CallBackend>
    When <Contact1> calls me
    Then I wait for 5 seconds
    And I dismiss security alert
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    Then I do not see incoming call
    And <Contact1> stops calling me
    Given <Contact2> starts instance using <CallBackend2>
    When <Contact2> starts a video call to me
    Then I wait for 7 seconds
    And I dismiss security alert
    And I dismiss security alert if it is visible
    And I see alert message containing "Calling not available" in the title
    And I tap OK button on the alert
    And I do not see ongoing video call
    And <Contact2> stops calling me

    Examples:
      | Login      | Password      | Name      | Contact1   | Contact2  | Message | PingMsg1   | PingMsg2         | CallBackend | CallBackend2 |
      | user1Email | user1Password | user1Name | user2Name  | user3Name | Yo      | YOU PINGED | YOU PINGED AGAIN | autocall    | chrome       |
