Feature: Self Profile

  @id2264 @regression @rc
  Scenario Outline: I can change my name in portrait mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I tap my name field on Self Profile page
    And I change my name to <NewName> on Self Profile page
    Then I see my name on Self Profile page

    Examples: 
      | Name      | NewName     |
      | user1Name | NewTestName |

  @id2250 @regression @rc
  Scenario Outline: I can change my name in landscape mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with no conversations
    And I see my name on Self Profile page
    And I tap my name field on Self Profile page
    And I change my name to <NewName> on Self Profile page
    Then I see my name on Self Profile page

    Examples: 
      | Name      | NewName     |
      | user1Name | NewTestName |

  @id2288 @regression @rc
  Scenario Outline: Change profile picture using existing from gallery in portrait mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I tap in the center of Self Profile page
    And I remember my current profile picture on Self Profile page
    And I tap Change Picture button on Self Profile page
    And I tap Gallery button on Self Profile page
    And I select a picture from the Gallery
    And I confirm my picture on the Self Profile page
    And I tap in the center of Self Profile page
    Then I verify that my current profile picture is different from the previous one

    Examples: 
      | Name      |
      | user1Name |

  @id2289 @regression @rc
  Scenario Outline: Change profile picture using existing from gallery in landscape mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with no conversations
    And I see my name on Self Profile page
    And I tap in the center of Self Profile page
    And I remember my current profile picture on Self Profile page
    And I tap Change Picture button on Self Profile page
    And I tap Gallery button on Self Profile page
    And I select a picture from the Gallery
    And I confirm my picture on the Self Profile page
    And I tap in the center of Self Profile page
    Then I verify that my current profile picture is different from the previous one

    Examples: 
      | Name      |
      | user1Name |

  @id2833 @regression
  Scenario Outline: User can change profile picture by taking camera picture (portrait)
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I tap in the center of Self Profile page
    And I remember my current profile picture on Self Profile page
    And I tap Change Picture button on Self Profile page
    And I tap Take Photo button on Self Profile page
    And I confirm my picture on the Self Profile page
    And I tap in the center of Self Profile page
    Then I verify that my current profile picture is different from the previous one

    Examples: 
      | Name      |
      | user1Name |

  @id3106 @regression
  Scenario Outline: User can change profile picture by taking camera picture (landscape)
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with no conversations
    When I see my name on Self Profile page
    And I tap in the center of Self Profile page
    And I remember my current profile picture on Self Profile page
    And I tap Change Picture button on Self Profile page
    And I tap Take Photo button on Self Profile page
    And I confirm my picture on the Self Profile page
    And I tap in the center of Self Profile page
    Then I verify that my current profile picture is different from the previous one

    Examples: 
      | Name      |
      | user1Name |

  @id2857 @regression
  Scenario Outline: User can change accent color and it is saved after sign in sign out (portrait)
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see Conversations list with no conversations
    And I tap my avatar on top of Conversations list
    When I change accent color to <AccentColor>
    And I see color <AccentColor> selected on Self Profile page
    And I tap Options button on Self Profile page
    And I tap Sign Out button on Self Profile page
    And I see welcome screen
    And I sign in using my email
    And I see Conversations list with no conversations
    And I tap my avatar on top of Conversations list
    Then I see color <AccentColor> selected on Self Profile page

    Examples: 
      | Name      | AccentColor |
      | user1Name | Violet      |

  @id3109 @regression
  Scenario Outline: User can change accent color and it is saved after sign in sign out (landscape)
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see Conversations list with no conversations
    And I tap my avatar on top of Conversations list
    When I change accent color to <AccentColor>
    And I see color <AccentColor> selected on Self Profile page
    And I tap Options button on Self Profile page
    And I tap Sign Out button on Self Profile page
    And I see welcome screen
    And I sign in using my email
    And I see Conversations list with no conversations
    And I tap my avatar on top of Conversations list
    Then I see color <AccentColor> selected on Self Profile page

    Examples: 
      | Name      | AccentColor |
      | user1Name | Violet      |
