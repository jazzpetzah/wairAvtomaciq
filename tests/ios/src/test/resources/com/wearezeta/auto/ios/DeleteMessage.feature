Feature: DeleteMessage

  @C111321 @regression @rc @fastLogin
  Scenario Outline: Verify deleting own text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete only for me item from Delete menu
    Then I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C111322 @regression @fastLogin
  Scenario Outline: Verify deleting received text message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact>
    Then I see 1 default message in the conversation view
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete only for me item from Delete menu
    Then I see 0 default messages in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C111323 @regression @rc @fastLogin
  Scenario Outline: Verify deleting the picture, gif from Giphy
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact> sends encrypted image <Picture> to single user conversation <Name>
    When I tap on contact name <Contact>
    Then I see 1 photo in the conversation view
    When I long tap on image in conversation view
    And I tap on Delete badge item
    And I select Delete only for me item from Delete menu
    Then I see 0 photos in the conversation view
    And I type tag for giphy preview <GiphyTag> and open preview overlay
    # Wait for GIF picture to be downloaded
    And I wait for 10 seconds
    And I send gif from giphy preview page
    Then I see 1 photo in the conversation view
    When I long tap on image in conversation view
    And I tap on Delete badge item
    And I select Delete only for me item from Delete menu
    Then I see 0 photos in the conversation view

    Examples:
      | Name      | Contact   | Picture     | GiphyTag |
      | user1Name | user2Name | testing.jpg | hello    |

  @C111324 @regression @fastLogin
  Scenario Outline: Verify deleting soundcloud message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given User Myself sends encrypted message "<SoundCloudLink>" to user <Contact>
    When I tap on contact name <Contact>
    And I long tap on media container in conversation view
    And I tap on Delete badge item
    And I select Delete only for me item from Delete menu
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
    Given User Myself sends encrypted message "Try this app <Link>" to user <Contact>
    When I tap on contact name <Contact>
    Then I see link preview container in the conversation view
    When I long tap on link preview in conversation view
    And I tap on Delete badge item
    And I select Delete only for me item from Delete menu
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
    And I tap file transfer menu item <FileName>
    # Wait to be ready uploading for slower jenkins slaves
    And I wait for 10 seconds
    And I long tap on file transfer placeholder in conversation view
    And I tap on Delete badge item
    And I select Delete only for me item from Delete menu
    Then I do not see file transfer placeholder

    Examples:
      | Name      | Contact   | FileName                   |
      | user1Name | user2Name | FTRANSFER_MENU_DEFAULT_PNG |

  @C123604 @regression @noAcceptAlert @fastLogin
  Scenario Outline: Verify canceling deleting a message
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I see conversations list
    Given User <Contact1> sends 1 encrypted message to user Myself
    When I tap on contact name <Contact1>
    And I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Cancel item from Delete menu
    Then I see 1 default message in the conversation view

    Examples:
      | Name      | Contact1  |
      | user1Name | user2Name |

  @C202306 @staging @fastLogin @torun
  Scenario Outline: Verify I can delete my message everywhere (1:1)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given User <Contact> adds new device <HisDevice>
    Given I sign in using my email or phone number
    Given User Myself sends 1 encrypted message using device <MySecondDevice> to user <Contact>
    Given I see conversations list
    When I tap on contact name <Contact>
    And I type the default message and send it
    Then I see 1 default message in the conversation view
    When I long tap default message in conversation view
    And I tap on Delete badge item
    And I select Delete for everyone item from Delete menu
    Then I see 0 default messages in the conversation view
    And

    Examples:
      | Name      | Contact   | MySecondDevice | HisDevice |
      | user1Name | user2Name | device1        | device2   |