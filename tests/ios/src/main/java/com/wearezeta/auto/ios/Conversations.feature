Feature: Send Message
  
  @torun
  Scenario Outline: Send Message to user from my contact list
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message and send it
    Then I see my message in the dialog

    Examples: 
    
    |	Login						|	Password	|	Name			|	Contact	|
    |	piotr.iazadji@wearezeta.com	|	asdfer123	|	Piotr Iazadji	|	Maxim	|