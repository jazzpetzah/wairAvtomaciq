Feature: Sketch

  @C809 @regression @rc @legacy
  Scenario Outline: (CM-717) I can send a sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap Sketch button from cursor toolbar
    And I draw a sketch with <NumColors> colors
    And I send my sketch
    And I tap Image container in the conversation view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 2         |

  @C810 @regression @rc @legacy
  Scenario Outline: (CM-717) I can send sketch on image from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Sketch Image Paint button on Take Picture view
    And I draw a sketch on image with <NumColors> colors
    Then I send my sketch
    And I tap Image container in the conversation view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 2         |

  @C432 @regression
  Scenario Outline: I can send sketch on photo
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Extended cursor camera overlay
    And I tap Sketch Image Paint button on Take Picture view
    And I draw a sketch on image with <NumColors> colors
    Then I send my sketch
    And I tap Image container in the conversation view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |

  @C246278 @regression
  Scenario Outline: I can send an emoji sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I push local file named "<FileName>" to the device
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    When I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    And I see a picture in the conversation view
    And I tap Image container in the conversation view
    And I tap on Fullscreen button on the recent image in the conversation view
    And I take screenshot
    And I tap back button
    And I tap on Sketch button on the recent image in the conversation view
    And I draw an emoji sketch
    And I send my sketch
    And I tap Image container in the conversation view
    And I tap on Fullscreen button on the recent image in the conversation view
    Then I verify the previous and the current screenshots are different

    Examples:
      | Name      | Contact1  | FileName       |
      | user1Name | user2Name | avatarTest.png |

  @C318630 @staging
  Scenario Outline: I can send a text sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I push local file named "<FileName>" to the device
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    When I tap Add picture button from cursor toolbar
    And I tap Gallery button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    And I see a picture in the conversation view
    And I tap Image container in the conversation view
    And I tap on Fullscreen button on the recent image in the conversation view
    And I take screenshot
    And I tap back button
    And I tap on Sketch button on the recent image in the conversation view
    And I type text "<Text>" on sketch
    And I send my sketch
    And I tap Image container in the conversation view
    And I tap on Fullscreen button on the recent image in the conversation view
    Then I verify the previous and the current screenshots are different

    Examples:
      | Name      | Contact1  | Text                     | FileName       |
      | user1Name | user2Name | YoASDFJKSDFKJLSDKFJSDLKF | avatarTest.png |