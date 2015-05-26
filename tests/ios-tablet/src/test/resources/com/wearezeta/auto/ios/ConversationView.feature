Feature: Conversation View

  @staging @id2419
  Scenario Outline: Vefiry sending message [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I type the message
    And I send the message
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |

  @staging @id
  Scenario Outline: Receive message from contact [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    And Contact <Contact> send message to user <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    Then I see message in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id2413 @deployPictures 
  Scenario Outline: Verify sending image [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button on iPad
    And I press Camera Roll button on iPad
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
      
  @staging @id2407 @deployPictures 
  Scenario Outline: Verify sending image [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe the text input cursor
    And I press Add Picture button on iPad
    And I press Camera Roll button on iPad
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see new photo in the dialog

    Examples: 
      | Login      | Password      | Name      | Contact   |
      | user1Email | user1Password | user1Name | user2Name |
