Feature: Rich Media

  @id2830 @regression
  Scenario Outline: Send GIF format pic (portrait)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <GifName> to single user conversation <Name>
    Given I rotate UI to portrait
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    When I scroll to the bottom of the conversation view
    Then I see a new picture in the conversation view
    And I see the picture in the conversation view is animated
    When I tap the new picture in the conversation view
    Then I see the picture in the preview is animated

    Examples: 
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |

  @id3141 @regression
  Scenario Outline: Send GIF format pic (landscape)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given Contact <Contact> sends image <GifName> to single user conversation <Name>
    Given I rotate UI to landscape
    Given I sign in using my email
    Given I see the conversations list with conversations
    And I see the conversation <Contact> in my conversations list
    And I tap the conversation <Contact>
    And I see the conversation view
    When I scroll to the bottom of the conversation view
    Then I see a new picture in the conversation view
    And I see the picture in the conversation view is animated
    When I tap the new picture in the conversation view
    Then I see the picture in the preview is animated

    Examples: 
      | Name      | Contact   | GifName      |
      | user1Name | user2Name | animated.gif |