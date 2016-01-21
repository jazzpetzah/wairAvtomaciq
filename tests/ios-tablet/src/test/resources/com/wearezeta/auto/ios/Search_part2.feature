Feature: Search

  @C2803 @regression @id3289
  Scenario Outline: Verify starting a call with action button [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2804 @regression @id3290
  Scenario Outline: Verify starting a call with action button [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    Then I see user <Contact> found on People picker page
    When I tap on connected user <Contact> on People picker page
    And I see call action button on People picker page
    And I click call action button on People picker page
    Then I see mute call, end call buttons
    And I see calling to contact <Contact> message

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2805 @regression @id3291
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I click Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I see new photo in the dialog
    When I navigate back to conversations view
    Then I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2806 @regression @id3292
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [LANDSAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    When I see Send image action button on People picker page
    And I click Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll on iPad popover
    And I press Confirm button on iPad popover
    Then I see group chat page with 3 users <Contact1> <Contact2> <Contact3>
    And I see new photo in the dialog
    And I see in contact list group chat with <Contact1> <Contact2> <Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2807 @regression @id3295
  Scenario Outline: Verify action buttons appear after selecting person from Top People [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 1 top connections
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2808 @regression @id3296
  Scenario Outline: Verify action buttons appear after selecting person from Top People [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on first 1 top connections
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2809 @regression @id3297
  Scenario Outline: Verify action buttons appear after choosing user from search results [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2810 @regression @id3298
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I see action buttons appeared on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2811 @regression @id3299
  Scenario Outline: Verify button Open is changed on Create after checking second person [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @C2812 @regression @id3300
  Scenario Outline: Verify button Open is changed on Create after checking second person [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see open conversation action button on People picker page
    When I tap on 2nd top connection contact
    Then I see Create Conversation button on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @C2813 @regression @id3301
  Scenario Outline: Verify action buttons disappear by unchecking the avatar [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons appeared on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @C2814 @regression @id3302
  Scenario Outline: Verify action buttons disappear by unchecking the avatar [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons appeared on People picker page
    When I tap on 1st top connection contact
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @C2815 @regression @id3819
  Scenario Outline: Verify action buttons disappear by deleting token from a search field [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see action buttons appeared on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see open conversation action button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @C2816 @regression @id3820
  Scenario Outline: Verify action buttons disappear by deleting token from a search field [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see action buttons appeared on People picker page
    And I tap on 2nd top connection contact
    And I see Create Conversation button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see open conversation action button on People picker page
    And I press backspace button
    And I press backspace button
    Then I see action buttons disappeared on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @C2817 @regression @id3821
  Scenario Outline: Verify opening conversation with action button [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I click open conversation action button on People picker page
    Then I see dialog page

    Examples: 
      | Name      |
      | user1Name |

  @C2818 @regression @id3822
  Scenario Outline: Verify opening conversation with action button [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I see open conversation action button on People picker page
    And I click open conversation action button on People picker page
    Then I see dialog page

    Examples: 
      | Name      |
      | user1Name |

  @C2801 @staging @id4119
  Scenario Outline: Verify action buttons appear after choosing user from search results [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see Invite more people button
    And I tap on 1st top connection contact
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I tap on 1st top connection contact
    And I see action buttons disappeared on People picker page
    And I see Invite more people button
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I press backspace button
    And I press backspace button
    And I see action buttons disappeared on People picker page
    Then I see Invite more people button

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2802 @staging @id4120
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    When I open search by taping on it
    And I see People picker page
    And I re-enter the people picker if top people list is not there
    And I see Invite more people button
    And I tap on 1st top connection contact
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I tap on 1st top connection contact
    And I see action buttons disappeared on People picker page
    And I see Invite more people button
    And I input in People picker search field user name <Contact>
    And I see user <Contact> found on People picker page
    And I tap on connected user <Contact> on People picker page
    And I DONT see Invite more people button
    And I see action buttons appeared on People picker page
    And I press backspace button
    And I press backspace button
    And I see action buttons disappeared on People picker page
    Then I see Invite more people button

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |