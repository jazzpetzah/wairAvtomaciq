Feature: Search

  @C2803 @regression @id3289
  Scenario Outline: Verify starting a call with action button [PORTRAIT]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Call action button on People picker page
    Then I see Calling overlay

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2804 @rc @regression @id3290
  Scenario Outline: Verify starting a call with action button [LANDSAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until <Contact> exists in backend search results
    When I open search UI
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I tap Call action button on People picker page
    Then I see Calling overlay

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2805 @regression @id3291
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [PORTRAIT]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    And I tap Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I see 1 photo in the conversation view
    When I navigate back to conversations list
    Then I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2806 @rc @regression @id3292
  Scenario Outline: Verify sharing a photo to a newly created group conversation with action button [LANDSAPE]
    Given There are 4 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    Then I tap on first 3 top connections
    And I tap Send image action button on People picker page
    And I press Camera Roll button
    And I choose a picture from camera roll
    And I confirm my choice
    Then I see group chat page with users <Contact1>,<Contact2>,<Contact3>
    And I see 1 photo in the conversation view
    And I see in conversations list group chat with <Contact1>,<Contact2>,<Contact3>

    Examples: 
      | Name      | Contact1  | Contact2  | Contact3  |
      | user1Name | user2Name | user3Name | user4Name |

  @C2808 @regression @id3296
  Scenario Outline: Verify action buttons appear after selecting person from Top People [LANDSCAPE]
    Given There are 2 users where <Name> is me
    Given Myself is connected to <Contact>
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    And I tap on first 1 top connections
    Then I see Open conversation action button on People picker page

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
    When I open search UI
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    Then I see Open conversation action button on People picker page

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |

  @C2812 @regression @id3300
  Scenario Outline: Verify button Open is changed on Create after checking second person [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    When I tap on 1st top connection contact
    Then I see Open conversation action button on People picker page
    When I tap on 2nd top connection contact
    Then I see Create conversation action button on People picker page

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
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    When I tap on 1st top connection contact
    Then I see Open conversation action button on People picker page
    When I tap on 1st top connection contact
    Then I do not see Open conversation action button on People picker page

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
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    And I tap on Search input on People picker page
    And I tap on 1st top connection contact
    And I see Open conversation action button on People picker page
    And I tap on 2nd top connection contact
    Then I see Create conversation action button on People picker page
    When I press backspace button
    And I press backspace button
    Then I do not see Create conversation action button on People picker page
    When I press backspace button
    And I press backspace button
    Then I do not see Open conversation action button on People picker page

    Examples: 
      | Name      |
      | user1Name |

  @C2817 @regression @id3821
  Scenario Outline: Verify opening conversation with action button [PORTRAIT]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I tap Open conversation action button on People picker page
    Then I see conversation view page

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
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see top people list on People picker page
    And I tap on 1st top connection contact
    And I tap Open conversation action button on People picker page
    Then I see conversation view page

    Examples: 
      | Name      |
      | user1Name |

  @C2802 @regression @id4120
  Scenario Outline: Verify action buttons appear after choosing user from search results [LANDSCAPE]
    Given There are 3 users where <Name> is me
    Given Myself is connected to all other users
    Given I rotate UI to landscape
    Given I Sign in on tablet using my email
    Given I see conversations list
    Given I wait until my Top People list is not empty on the backend
    When I open search UI
    And I see Invite more people button
    And I tap on 1st top connection contact
    And I do not see Invite more people button
    And I see Open conversation action button on People picker page
    And I tap on 1st top connection contact
    And I do not see Open conversation action button on People picker page
    And I see Invite more people button
    And I tap on Search input on People picker page
    And I input in People picker search field user name <Contact>
    And I tap on conversation <Contact> in search result
    And I do not see Invite more people button
    And I see Open conversation action button on People picker page
    And I press backspace button
    And I press backspace button
    And I do not see Open conversation action button on People picker page
    Then I see Invite more people button

    Examples: 
      | Name      | Contact   |
      | user1Name | user2Name |