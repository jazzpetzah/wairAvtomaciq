Feature: Delete Message

  @C111321 @regression @rc @fastLogin
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    And I remember the state of the recent message from user Myself in the local database
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 default messages in the conversation view
    And I verify the remembered message has been deleted from the local database

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C111322 @regression @fastLogin
  Scenario Outline: Verify deleting received text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 default message to conversation Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the conversation view
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C111323 @regression @rc @fastLogin
  Scenario Outline: Verify deleting the picture, gif from Giphy
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User <Contact> sends 1 image file <Picture> to conversation Myself
    Given I see conversations list
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view
    When I long tap on image in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    # Wait for animation
    And I wait for 2 seconds
    Then I see 0 photos in the conversation view
    When I type the "<GiphyTag>" message
    And I tap GIF button from input tools
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I select the first item from Giphy grid
    And I tap Send button on Giphy preview page
    Then I see 1 photo in the conversation view
    When I long tap on image in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I see 0 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     | GiphyTag |
      | user1Name | user2Name | testing.jpg | hello    |

  @C111324 @regression @fastLogin
  Scenario Outline: Verify deleting soundcloud message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends 1 "<SoundCloudLink>" message to conversation <Contact>
    When I tap on contact name <Contact>
    And I long tap on media container in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I do not see media container in the conversation view

    Examples:
      | Name      | Contact   | SoundCloudLink                                   |
      | user1Name | user2Name | https://soundcloud.com/sodab/256-ra-robag-wruhme |

  @C167037 @regression @fastLogin
  Scenario Outline: Verify deleting messages containing links
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User Myself sends 1 "Try this app <Link>" message to conversation <Contact>
    When I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I do not see link preview container in the conversation view

    Examples:
      | Name      | Contact   | Link                  |
      | user1Name | user2Name | https://www.wire.com/ |

  @C111325 @rc @regression @fastLogin
  Scenario Outline: Verify deleting shared file
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I tap File Transfer button from input tools
    # Wait for transition
    And I wait for 5 seconds
    And I tap file transfer menu item <FileName>
    # Wait to be ready uploading for slower jenkins slaves
    And I wait for 10 seconds
    And I long tap on file transfer placeholder in conversation view
    And I tap on Delete badge item
    And I select Delete for Me item from Delete menu
    Then I do not see file transfer placeholder

    Examples:
      | Name      | Contact   | FileName                   |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG |

  @C123604 @regression @fastLogin
  Scenario Outline: Verify canceling deleting a message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 default message to conversation Myself
    When I tap on contact name <Contact1>
    And I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Cancel item from Delete menu
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C111327 @regression @fastLogin @rc
  Scenario Outline: Verify deleting is synchronised across own devices when they are online
    Given There are 3 users where <Name> is me
    Given Users add the following devices: {"Myself": [{"name": "<Device>"}], "<Contact1>": [{"name": "<ContactDevice>"}]}
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <GroupChatName> with <Contact1>,<Contact2>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message using device <Device> to user <Contact1>
    Given User <Contact1> sends 1 encrypted message using device <ContactDevice> to group conversation <GroupChatName>
    Given I see conversations list
    Given I tap on contact name <Contact1>
    When User Myself deletes the recent message from user <Contact1>
    Then I see 0 default messages in the conversation view
    When I navigate back to conversations list
    And I tap on group chat with name <GroupChatName>
    And User Myself deletes the recent message from group conversation <GroupChatName>
    Then I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact1  | Contact2  | Device  | ContactDevice | GroupChatName |
      | user1Name | user2Name | user3Name | Device1 | Device2       | MyGroup       |