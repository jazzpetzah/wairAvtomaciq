Feature: Self Profile

  @C744 @id2264 @regression @rc
  Scenario Outline: I can change my name in portrait mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with no conversations
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I tap my name field on Self Profile page
    And I change my name to <NewName> on Self Profile page
    Then I see my name on Self Profile page

    Examples:
      | Name      | NewName     |
      | user1Name | NewTestName |

  @C734 @id2250 @regression @rc @rc44
  Scenario Outline: I can change my name in landscape mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with no conversations
    And I see my name on Self Profile page
    And I tap my name field on Self Profile page
    And I change my name to <NewName> on Self Profile page
    Then I see my name on Self Profile page

    Examples:
      | Name      | NewName     |
      | user1Name | NewTestName |

  @C754 @id2288 @regression @rc @rc44
  Scenario Outline: Change profile picture using existing from gallery in portrait mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with no conversations
    When I tap my avatar on top of conversations list
    And I see my name on Self Profile page
    And I tap in the center of Self Profile page
    And I remember my current profile picture on Self Profile page
    And I tap Change Picture button on Self Profile page
    And I tap Gallery button on Self Profile page
    And I confirm my picture on the Self Profile page
    And I tap in the center of Self Profile page
    Then I verify that my current profile picture is different from the previous one

    Examples:
      | Name      |
      | user1Name |

  @C755 @id2289 @regression @rc @rc44
  Scenario Outline: Change profile picture using existing from gallery in landscape mode
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
    Given I see the conversations list with no conversations
    And I see my name on Self Profile page
    And I tap in the center of Self Profile page
    And I remember my current profile picture on Self Profile page
    And I tap Change Picture button on Self Profile page
    And I tap Gallery button on Self Profile page
    And I confirm my picture on the Self Profile page
    And I tap in the center of Self Profile page
    Then I verify that my current profile picture is different from the previous one

    Examples:
      | Name      |
      | user1Name |

  @C484 @id2833 @regression
  Scenario Outline: User can change profile picture by taking camera picture (portrait)
    Given There is 1 user where <Name> is me
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
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

  @C513 @id3106 @regression
  Scenario Outline: User can change profile picture by taking camera picture (landscape)
    Given There is 1 user where <Name> is me
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I accept First Time overlay as soon as it is visible
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
