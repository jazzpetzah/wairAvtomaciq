Feature: Invintations

  @id4158 @regression
  Scenario Outline: Invite people button present in the list if user has up to 5 contacts
    Given There are 7 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>,<Contact3>,<Contact4>,<Contact5>
    Given I sign in using my email or phone number
    Given I see Contact list with contacts
    And I wait until <Contact6> exists in backend search results
    Then I see invite more people button
    Given <Contact6> sent connection request to <Name>
    When <Name> accept all requests
    Then I do not see invite more people button
    When I swipe right on a <Contact1>
    And I select DELETE from conversation settings menu
    And I press DELETE on the confirm alert
    Then I see invite more people button

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  | Contact4  | Contact5  | Contact6  |
      | user1Name | user2Name | user3Name | user4Name | user5Name | user6Name | user7Name |