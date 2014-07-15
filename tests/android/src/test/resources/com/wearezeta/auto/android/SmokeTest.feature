Feature: SmokeTest
@smoke
  Scenario Outline: Open/Close People picker
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe down contact list
    And I see People picker page
    And I press Clear button
    Then Contact list appears with my name <Name>

    Examples: 
      | Login   | Password    | Name    |
      | aqaUser | aqaPassword | aqaUser |

@mute
@regression
  Scenario Outline: Send Long Message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type long message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |


@mute
@regression
  Scenario Outline: Send Upper and Lower case to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type Upper/Lower case message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
     
@mute
@regression 
  Scenario Outline: Send special chars message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type <Message> message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     | Message                         |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |ÄäÖöÜüß simple message in english|
   
@mute
@regression
  Scenario Outline: Send emoji message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type <Message> message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     | Message |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |:) ;) :( |
  
@mute
@smoke 
  Scenario Outline: ZClient change name
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on my name
    And I change <Name> to <NewName> 
    And I swipe right to contact list
    Then I see contact list loaded with User name <NewName>
    When I tap on my name <NewName>
    Then I see my new name <NewName> and return old <Name>

    Examples: 
      | Login   | Password    | Name    | NewName     |
      | aqaUser | aqaPassword | aqaUser | NewTestName |
      
 
@smoke
  Scenario Outline: Check contact personal info
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe up on dialog page
    Then I see <Contact> user name and email

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
