Feature: DeleteMessage

  @C111321 @regression
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the dialog
    When I long tap default message in conversation view
    And I tap on Delete badge item
    Then I see 0 default messages in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C111322 @regression
  Scenario Outline: Verify deleting received text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the dialog
    When I long tap default message in conversation view
    And I tap on Delete badge item
    Then I see 0 default messages in the dialog

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C111323 @staging
  Scenario Outline: Verify deleting the picture, gif from Giphy
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    When I tap on contact name <Contact>
    Then I see 1 photo in the dialog
    When I long tap on image in the conversation
    And I tap on Delete badge item
    Then I see 0 photos in the dialog
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I send gif from giphy preview page
    Then I see 1 photo in the dialog
    When I long tap on image in the conversation
    And I tap on Delete badge item
    Then I see 0 photos in the dialog

    Examples:
      | Name      | Contact   | Picture     | GiphyTag |
      | user1Name | user2Name | testing.jpg | hello    |

  @torun @C111324 @staging
  Scenario Outline: Verify deleting soundcloud message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I long tap on media container in the conversation
    And I tap on Delete badge item
    Then I do not see the media container in the conversation view

    Examples:
      | Name      | Contact   | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |