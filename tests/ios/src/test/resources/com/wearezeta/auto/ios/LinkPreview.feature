Feature: Link Preview

  @C167029 @rc @regression @fastLogin
  Scenario Outline: Verify preview is shown for sent link (link only)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <DeviceName1>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<Link>" message and send it
    # This is to make the keyboard invisible as sometimes the keyboard is still visible after posting the link
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    # This is to make sure the image appears in the preview (we had this bug) and good enough check here once in the test suite
    Then I see link preview image in the conversation view
    # Wait for the delivery
    And I wait for 8 seconds
    And I see "<DeliveredLabel>" on the message toolbox in conversation view

    Examples:
      | Name      | Contact   | Link                 | DeviceName1 | DeliveredLabel |
      | user1Name | user2Name | https://www.wire.com | devcie1     | Delivered      |

  @C167030 @C167031 @C167032 @regression @fastLogin
  Scenario Outline: Verify preview is shown for mixed link and text
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    # Check link + text
    And I type the "<Link> <Text>" message and send it
    # This is to make the keyboard invisible
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    And I see the conversation view contains message <Link> <Text>
    # Check text + link
    And I type the "<Text1> <Link>" message and send it
    # This is to make the keyboard invisible
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see the conversation view contains message <Text1>
    And I do not see the conversation view contains message <Text1> <Link>
    And I see link preview container in the conversation view
    # Check text + link + text
    When I type the "<Text1> <Link> <Text>" message and send it
    # This is to make the keyboard invisible
    And I navigate back to conversations list
    And I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    And I see the conversation view contains message <Text1> <Link> <Text>

    Examples:
      | Name      | Contact   | Link             | Text    | Text1      |
      | user1Name | user2Name | https://wire.com | My text | Text first |

  @C169224 @regression @fastLogin
  Scenario Outline: Verify preview is shown for shortened URL
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<Shortenlink>" message and send it
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Shortenlink           |
      | user1Name | user2Name | https://goo.gl/pywMuA |

  @C167039 @rc @regression @fastLogin
  Scenario Outline: Verify preview is shown for different formats of link
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself send encrypted message "<Link>" to user <Contact>
    Given I see conversations list
    Given I tap on contact name <Contact>
    # Wait for link preview to be loaded
    When I wait for <LinkLoadTimeout> seconds
    Then I see link preview container in the conversation view
    When User Myself deletes the recent message from user <Contact>
    Then I do not see link preview container in the conversation view
    When User Myself send encrypted message "<Link1>" to user <Contact>
    # Wait for link preview to be loaded
    And I wait for <LinkLoadTimeout> seconds
    Then I see link preview container in the conversation view
    When User Myself deletes the recent message from user <Contact>
    Then I do not see link preview container in the conversation view
    When User Myself send encrypted message "<Link2>" to user <Contact>
    # Wait for link preview to be loaded
    And I wait for <LinkLoadTimeout> seconds
    Then I see link preview container in the conversation view
    When User Myself deletes the recent message from user <Contact>
    Then I do not see link preview container in the conversation view
    When User Myself send encrypted message "<Link3>" to user <Contact>
    # Wait for link preview to be loaded
    And I wait for <LinkLoadTimeout> seconds
    Then I see link preview container in the conversation view
    When User Myself deletes the recent message from user <Contact>
    Then I do not see link preview container in the conversation view
    When I type the "<Link4>" message and send it
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                | Link1                | Link2                   | Link3               | Link4               | LinkLoadTimeout |
      | user1Name | user2Name | http://facebook.com | https://facebook.com | http://www.facebook.com | Http://facebook.com | HTTP://FACEBOOK.COM | 3                |

  @C167038 @rc @regression @fastLogin
  Scenario Outline: Verify copying link preview
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact>,<Contact1>
    Given I sign in using my email or phone number
    Given User <Contact> sends encrypted message "<Link>" to user Myself
    Given I see conversations list
    Given I tap on contact name <Contact>
    Given I long tap on link preview in conversation view
    Given I tap on Copy badge item
    Given I navigate back to conversations list
    Given I tap on contact name <Contact1>
    Given I tap on text input
    Given I long tap on text input
    Given I tap on Paste badge item
    Given I tap Send Message button in conversation view
    Given I navigate back to conversations list
    When I tap on contact name <Contact1>
    Then I see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Contact1  | Link             |
      | user1Name | user2Name | user3Name | https://wire.com |

  @C167033 @regression @fastLogin
  Scenario Outline: Verify preview is shown without picture when there are none
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the "<Link>" message and send it
    Then I see link preview container in the conversation view
    And I do not see link preview image in the conversation view

    Examples:
      | Name      | Contact   | Link                                               |
      | user1Name | user2Name | https://en.wikipedia.org/wiki/Provincial_Secretary |

  @C167041 @regression @fastLogin
  Scenario Outline: Verify link preview isn't shown for YouTube, SoundCloud, Vimeo, Giphy
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And User <Contact> sends encrypted message "<YouTubeLink>" to user <Name>
    # Wait for the message to be received and rendered
    And I wait for 5 seconds
    Then I see the conversation view contains message <YouTubeLink>
    And I do not see link preview container in the conversation view
    When User <Contact> sends encrypted message "<SoundCloudLink>" to user <Name>
    # Wait for the message to be received and rendered
    And I wait for 5 seconds
    Then I see the conversation view contains message <SoundCloudLink>
    And I do not see link preview container in the conversation view
    When User <Contact> sends encrypted message "<VimeoLink>" to user <Name>
    # Wait for the message to be delivered
    And I wait for 5 seconds
    Then I see the conversation view contains message <VimeoLink>
    And I do not see link preview container in the conversation view
    When I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    Then I see 1 photo in the conversation view
    And I see last message in the conversation view is expected message <GiphyTag> · via giphy.com
    And I do not see link preview container in the conversation view

    Examples:
      | Name      | Contact   | YouTubeLink                                | SoundCloudLink                                   | VimeoLink                   | GiphyTag |
      | user1Name | user2Name | http://www.youtube.com/watch?v=Bb1RhktcugU | https://soundcloud.com/sodab/256-ra-robag-wruhme | https://vimeo.com/129426512 | hi       |