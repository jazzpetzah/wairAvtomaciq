Feature: Extended Cursor Camera

  @C183899 @regression @rc
  Scenario Outline: Check Gallery Keyboard UI and interaction with buttons
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    #C183865
    Then I see extended cursor camera overlay
    #C183866
    And I see thumbnails in extended cursor camera overlay
    And I see Take Photo button on Extended cursor camera overlay
    And I see Switch Camera button on Extended cursor camera overlay
    And I see Camera button on Extended cursor camera overlay
    And I see External Video button on Extended cursor camera overlay
    #C183867
    When I swipe left on Extended cursor camera overlay
    Then I see Back button on Extended cursor camera overlay
    #C183869
    When I tap Back button on Extended cursor camera overlay
    Then I see thumbnails in extended cursor camera overlay
    And I see Take Photo button on Extended cursor camera overlay
    And I see Switch Camera button on Extended cursor camera overlay
    And I see Camera button on Extended cursor camera overlay
    And I see External Video button on Extended cursor camera overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |


  @C183870 @regression
  Scenario Outline: When select picture from thumbnail and tap cancel in preview -  Gallery Keyboard should be still opened
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    And I select thumbnail in row 1 and col 1 on Extended cursor camera overlay
    And I tap Cancel button on Take Picture view
    Then I see extended cursor camera overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C183871 @regression
  Scenario Outline: When I tap enlarge button - full camera view appears
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    And I tap Camera button on Extended cursor camera overlay
    Then I see Take Photo button on Take Picture view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |


  @C183872 @regression @rc
  Scenario Outline: When I tap shoot button - Picture taken and preview appears
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Extended cursor camera overlay
    # Wait until preview loaded
    And I wait for 3 seconds
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C183885 @C183886 @regression
  Scenario Outline: When device locked/unlocked with opened Gallery Keyboard - keyboard should be dismissed
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    # C183885
    When I tap Add picture button from cursor toolbar
    And I see extended cursor camera overlay
    And I lock the device
    And I wait for 2 seconds
    And I unlock the device
    # Wait until UI loaded
    And I wait for 5 seconds
    Then I do not see extended cursor camera overlay
    # C183886
    When I tap Add picture button from cursor toolbar
    And I see extended cursor camera overlay
    And I tap conversation name from top toolbar
    And I press Back button
    Then I do not see extended cursor camera overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C183887 @regression
  Scenario Outline: When I swipe to conversations list with opened Gallery Keyboard and then back to conversation - Keyboard should be closed
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    And I see extended cursor camera overlay
    And I swipe right
    And I see Conversations list with conversations
    And I tap on conversation name <Contact>
    Then I do not see extended cursor camera overlay

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C183877 @regression
  Scenario Outline: I can retake picture from mini camera (dismiss one in preview and shoot another)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Extended cursor camera overlay
    And I tap Cancel button on Take Picture view
    Then I see extended cursor camera overlay
    When I tap Take Photo button on Extended cursor camera overlay
    And I tap Confirm button on Take Picture view
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   |
      | user1Name | user2Name |

  @C183881 @regression @rc
  Scenario Outline: I can draw a sketch on picture selected from thumbnail
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    And I select thumbnail in row 1 and col 1 on Extended cursor camera overlay
    And I tap Sketch Image Paint button on Take Picture view
    And I draw a sketch with <NumColors> colors
    And I send my sketch
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   | NumColors |
      | user1Name | user2Name | 2         |

  @C183882 @regression @rc
  Scenario Outline: I can draw a sketch on camera photo (mini camera)
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    Given I tap on conversation name <Contact>
    When I tap Add picture button from cursor toolbar
    And I tap Take Photo button on Extended cursor camera overlay
    And I tap Sketch Image Paint button on Take Picture view
    And I draw a sketch with <NumColors> colors
    And I send my sketch
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact   | NumColors |
      | user1Name | user2Name | 2         |

  @C183883 @staging
  Scenario Outline: (AN-4336) I can draw a sketch on camera photo from full screen camera
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact1>
    Given I sign in using my email or phone number
    Given I accept First Time overlay as soon as it is visible
    Given I see Conversations list with conversations
    When I tap on conversation name <Contact1>
    And I tap Add picture button from cursor toolbar
    And I tap Camera button on Extended cursor camera overlay
    And I tap Take Photo button on Take Picture view
    And I tap Sketch Image Paint button on Take Picture view
    And I draw a sketch on image with <NumColors> colors
    And I send my sketch
    Then I see a picture in the conversation view

    Examples:
      | Name      | Contact1  | NumColors |
      | user1Name | user2Name | 6         |