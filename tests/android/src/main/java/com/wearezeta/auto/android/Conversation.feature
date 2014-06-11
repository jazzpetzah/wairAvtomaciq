
Feature: Conversation
@torun
  Scenario Outline: Send Message to contact
    Given I Sign in using login <Login> and password <Password> 
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message
    And I press send
    Then I see my message in the dialog

    Examples: 
      | Login                           | Password | Name            | Contact |
   	  | sergeii.khyzhniak@wearezeta.com | 123456   | Sergey Hizhnyak | Piotr   |

  Scenario Outline: Send Hello to contact
    Given I Sign in using login <Login> and password <Password> 
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I multi tap on text input
    Then I see Hello message in the dialog

    Examples: 
      | Login                           | Password | Name            | Contact |
      | sergeii.khyzhniak@wearezeta.com | 123456   | Sergey Hizhnyak | Piotr   |

  Scenario Outline: Send Camera picture to contact
    Given I Sign in using login <Login> and password <Password> 
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login                           | Password | Name            | Contact	| 
      | sergeii.khyzhniak@wearezeta.com | 123456   | Sergey Hizhnyak | Volodymyr|
