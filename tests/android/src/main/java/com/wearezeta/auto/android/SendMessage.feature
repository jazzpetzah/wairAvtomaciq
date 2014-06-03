Feature: Send Message
  
  Scenario Outline: Send Message to my contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on name <Contact>
    And I see dialog page 
    And I tap on bottom part of the screen
    And I type the message
    And I press send
    Then I see my message in the dialog

    Examples: 
      | Login                           | Password | Name            | Contact |
      | sergeii.khyzhniak@wearezeta.com | 123456   | Sergey Hizhnyak | Piotr   |
