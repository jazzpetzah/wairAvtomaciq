Feature: Sync Engine

  Scenario Outline: Sync Engine smoke test
    Given There are 3 users where <Name> is me
    Given Myself is connected to <Contact1>,<Contact2>
    Given Myself has group chat <ChatName> with <Contact1>,<Contact2>
    Given I start all platform clients
    And I sign in to all platform clients and go to conversation <ConversationName>
    When I run serial sync engine test
    Then I collect messages order data
    And I collect builds and devices info
    And I build results report
    And I perform acceptance checks

    Examples: 
      | Name      | Contact1  | Contact2  | ChatName       |
      | user1Name | user2Name | user3Name | SyncEngineTest |
