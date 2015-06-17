Feature: Self Profile

  @regression @id2589
  Scenario Outline: Verify about screen contains all the required information [PORTRAIT]
    Given There is 1 user where <Name> is me
    Given User me change accent color to <Color>
    Given I Sign in on tablet using my email
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I see that the About page is colored <Color>
    And I see WireWebsiteButton
    And I see TermsButton
    And I see PrivacyPolicyButton
    And I see BuildNumberText
    And I open PrivacyPolicyPage
    And I see PrivacyPolicyPage
    And I close legal page
    Then I see About page
    And I open TermsOfUsePage
    And I see TermsOfUsePage
    And I close legal page
    Then I see About page
    And I open WireWebsite
    Then I see WireWebsitePage

    Examples: 
      | Name      | Contact   | Color  |
      | user1Name | user2Name | Violet |

  @regression @id2589
  Scenario Outline: Verify about screen contains all the required information [LANDSCAPE]
    Given There is 1 user where <Name> is me
    Given User me change accent color to <Color>
    Given I Sign in on tablet using my email
    Given I rotate UI to landscape
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I click on Settings button on personal page
    And I click on About button on personal page
    Then I see About page
    And I see that the About page is colored <Color>
    And I see WireWebsiteButton
    And I see TermsButton
    And I see PrivacyPolicyButton
    And I see BuildNumberText
    And I open PrivacyPolicyPage
    And I see PrivacyPolicyPage
    And I close legal page
    Then I see About page
    And I open TermsOfUsePage
    And I see TermsOfUsePage
    And I close legal page
    Then I see About page
    And I open WireWebsite
    Then I see WireWebsitePage

    Examples: 
      | Name      | Contact   | Color  |
      | user1Name | user2Name | Violet |

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

  @regression @id2586
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

  @staging @id2581 @id2586
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

  @staging @id2581 @id2586
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
