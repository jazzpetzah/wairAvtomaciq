Feature: Sketch

  @C951 @rc @regression @fastLogin
  Scenario Outline: I can send a sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact1>
    And I tap Sketch button from input tools
    And I draw a random sketch
    And I tap Send button on Sketch page
    # Wait for animation
    And I wait for 3 seconds
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C952 @173061 @rc @regression @fastLogin
  Scenario Outline: Verify drawing on the image from gallery
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap Add Picture button from input tools
    And I accept alert if visible
    And I accept alert if visible
    And I select the first picture from Keyboard Gallery
    And I tap Sketch button on Picture preview page
    And I draw a random sketch
    And I tap Send button on Sketch page
    Then I see 1 photo in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C954 @regression @fastLogin
  Scenario Outline: Verify drawing on image by Sketch on image button tap
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap on image in conversation view
    And I tap Sketch button on image
    And I draw a random sketch
    And I tap Send button on Sketch page
    Then I see 2 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     |
      | user1Name | user2Name | testing.jpg |

  @C318640 @regression @fastLogin
  Scenario Outline: Verify you can add a smile on sketch
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User adds the following device: {"<Contact>": [{}]}
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I tap on image in conversation view
    Given I tap Fullscreen button on image
    Given I remember current picture preview state
    Given I tap close fullscreen page button
    Given I tap Sketch button on image
    When I tap Emoji button on Sketch page
    And I tap "<EmojiChar>" key on Emoji Keyboard
    And I tap Send button on Sketch page
    And I see 2 photos in the conversation view
    And I tap on image in conversation view
    And I tap Fullscreen button on image
    Then I verify that current picture preview similarity score is less than <MaxScore> within <Timeout> seconds
    And I verify that current picture preview similarity score is more than <MinScore> within 1 second

    Examples:
      | Name      | Contact   | Picture     | Timeout | MaxScore | MinScore | EmojiChar |
      | user1Name | user2Name | testing.jpg | 7       | 0.95     | 0.8      | 😀        |
