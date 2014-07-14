Feature: Conversation

@mute
@smoke @regression
  Scenario Outline: Send Message to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I tap on text input
    And I type the message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

@mute	  
@smoke @regression
  Scenario Outline: Send Hello to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I multi tap on text input
    Then I see Hello message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

@mute
@smoke @regression
  Scenario Outline: Send Camera picture to contact
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact>
    And I see dialog page
    And I swipe on text input
    And I press Add Picture button
    And I press "Take Photo" button
    And I press "Confirm" button
    Then I see new photo in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |

@smoke @regression
  Scenario Outline: Start group chat with users from contact list
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <Contact1>
    And I see dialog page
    And I swipe up on dialog page
    And I see <Contact1> user profile page
    And I press add contact button

    #And I see People picker page
    #And I input in People picker search field user name <Contact2>
    #And I see user <Contact2> found on People picker page
    #And I tap on user name found on People picker page <Contact2>
    #And I see Add to conversation button
    #And I click on Add to conversation button
    #Then I see group chat page with users <Contact1> <Contact2>
    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 |

@mute
@smoke @regression
  Scenario Outline: Send message to group chat
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I see dialog page
    And I tap on text input
    And I type the message and send it
    Then I see my message in the dialog

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName     |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | SendMessGroupChat |

@mute
@smoke @regression
  Scenario Outline: Leave group conversation
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on group dialog page
    And I press Leave conversation button
    And I confirm leaving
    And I tap on contact name <GroupChatName>
    Then I see that <Name> is not present on group chat page

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName  |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 | aqaContact2 | LeaveGroupChat |

@mute
@smoke @regression
  Scenario Outline: Remove from group chat
    Given I have group chat with name <GroupChatName> with <Contact1> and <Contact2>
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on contact name <GroupChatName>
    And I swipe up on group dialog page
    And I select contact <Contact2>
    And I click Remove
    And I confirm remove
    And I press back on group chat info page
    Then I see that <Contact2> is not present on group chat page

    Examples: 
      | Login   | Password    | Name    | Contact1    | Contact2    | GroupChatName       |
      | aqaUser | aqaPassword | aqaUser | aqaContact2 | aqaContact1 | RemoveFromGroupChat |

@mute
@smoke @regression
  Scenario Outline: Accept connection request
    Given connection request is sended to me
    And I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I tap on my name <Name>
    And I see connection request from <Contact>
    And I confirm connection request
    And I swipe from Instructions page to Contact list page
    Then I see contact list loaded with User name <Contact>

    Examples: 
      | Login   | Password    | Name    | Contact     |
      | aqaUser | aqaPassword | aqaUser | yourContact |

@smoke @regression
  Scenario Outline: Mute conversation
    Given I Sign in using login <Login> and password <Password>
    And I see Contact list with my name <Name>
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is muted
    When I swipe right on a <Contact1>
    And I click mute conversation
    Then Contact <Contact1> is not muted

    Examples: 
      | Login   | Password    | Name    | Contact1    |
      | aqaUser | aqaPassword | aqaUser | aqaContact1 |
