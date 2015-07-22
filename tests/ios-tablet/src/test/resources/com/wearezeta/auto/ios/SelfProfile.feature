Feature: Self Profile

  @regression @id2586
  Scenario Outline: Self profile. Verify max limit in 64 chars [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>
    Then I see Contact list with my name <NewUsername1>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>

    Examples: 
      | Name      | NewUsername                                                          | NewUsername1                                                     | Contact   |
      | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 | user2Name |

  @regression @id3157
  Scenario Outline: Self profile. Verify max limit in 64 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>
    When I rotate UI to landscape
    Then I see Contact list with my name <NewUsername1>
    And I tap to edit my name
    And I change name <Name> to <NewUsername>
    Then I see my new name <NewUsername1>

    Examples: 
      | Name      | NewUsername                                                          | NewUsername1                                                     | Contact   |
      | user1Name | mynewusernamewithmorethan64characters3424245345345354353452345234535 | mynewusernamewithmorethan64characters342424534534535435345234523 | user2Name |

  @regression @id2581
  Scenario Outline: I verify I am unable to enter a name using only spaces or more than 80 chars [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I attempt to change name using only spaces
    And I see error message asking for more characters
    And I attempt to enter an 80 char name
    Then I verify my new name is only first 64 chars

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @regression @id3158
  Scenario Outline: I verify I am unable to enter a name using only spaces or more than 80 chars [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I attempt to change name using only spaces
    And I see error message asking for more characters
    And I attempt to enter an 80 char name
    Then I verify my new name is only first 64 chars

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @staging @id2574
  Scenario Outline: Change your profile picture [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples: 
      | Name      | Picture                   | Contact   |
      | user1Name | userpicture_ios_check.png | user2Name |

  @staging @id3159 
  Scenario Outline: Change your profile picture [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    And I tap on personal screen
    And I press Camera button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    And I return to personal page
    Then I see changed user picture <Picture>

    Examples: 
      | Name      | Picture                             | Contact   |
      | user1Name | userpicture_ios_check_landscape.png | user2Name |
